package com.learnhub.controller;

import com.learnhub.dto.CambiarPasswordDTO;
import com.learnhub.dto.NotaDTO;
import com.learnhub.dto.ProfesorFormDTO;
import com.learnhub.exception.BusinessException;
import com.learnhub.service.MateriaService;
import com.learnhub.service.NotaService;
import com.learnhub.service.ProfesorService;
import com.learnhub.enumeration.Sexo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/** Área privada de la cuenta del docente/profesor. */
@Controller
@RequestMapping("/docente")
@RequiredArgsConstructor
public class DocenteController {
    private final ProfesorService profesorService;
    private final MateriaService materiaService;
    private final NotaService notaService;

    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model) {
        model.addAttribute("profesor", profesorService.buscarPorEmail(authentication.getName()));
        return "docente/perfil";
    }

    @GetMapping("/editar")
    public String editar(Authentication authentication, Model model) {
        var p = profesorService.buscarPorEmail(authentication.getName());
        model.addAttribute("form", new ProfesorFormDTO(p.nombre(), p.apellido(), p.especialidad(), p.sexo(), p.fechaNacimiento()));
        model.addAttribute("sexos", Sexo.values());
        return "docente/editar";
    }

    @PostMapping("/editar")
    public String editar(Authentication authentication,
                         @Valid @ModelAttribute("form") ProfesorFormDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            return "docente/editar";
        }
        try {
            profesorService.editarProfesor(authentication.getName(), dto);
            return "redirect:/docente/perfil?actualizado";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("sexos", Sexo.values());
            return "docente/editar";
        }
    }

    /** Equivalente a listarClasesDictadas() del diagrama: las materias que dicta el profesor autenticado. */
    @GetMapping("/materias")
    public String misMaterias(Authentication authentication, Model model) {
        var profesor = profesorService.buscarPorEmail(authentication.getName());
        model.addAttribute("materias", materiaService.listarPorProfesor(profesor.id()));
        return "docente/materias";
    }

    @GetMapping("/materia/{id}/notas")
    public String notasDeMiMateria(@PathVariable Long id, Authentication authentication, Model model) {
        var materia = validarMateriaPropia(id, authentication);
        model.addAttribute("materia", materia);
        model.addAttribute("notas", notaService.listarPorMateria(id));
        model.addAttribute("nota", new NotaDTO(null, LocalDate.now(), null, id));
        return "docente/materia-notas";
    }

    @PostMapping("/materia/{id}/notas")
    public String registrarNotaPropia(@PathVariable Long id, Authentication authentication,
                                      @Valid @ModelAttribute("nota") NotaDTO dto,
                                      BindingResult result, Model model) {
        var materia = validarMateriaPropia(id, authentication);
        // El id de materia siempre se fuerza desde la ruta validada: nunca se confía
        // en el campo oculto del formulario para evitar que un docente cargue notas
        // en una materia ajena manipulando el POST.
        dto = new NotaDTO(dto.id(), dto.fecha(), dto.valor(), id);
        if (result.hasErrors()) {
            model.addAttribute("materia", materia);
            model.addAttribute("notas", notaService.listarPorMateria(id));
            return "docente/materia-notas";
        }
        try {
            notaService.registrarNota(dto);
            return "redirect:/docente/materia/" + id + "/notas";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("materia", materia);
            model.addAttribute("notas", notaService.listarPorMateria(id));
            return "docente/materia-notas";
        }
    }

    @GetMapping("/nota/{id}/editar")
    public String editarNotaPropiaForm(@PathVariable Long id, Authentication authentication, Model model) {
        validarNotaPropia(id, authentication);
        model.addAttribute("nota", notaService.obtenerDTO(id));
        return "docente/nota-form";
    }

    @PostMapping("/nota/{id}/editar")
    public String editarNotaPropia(@PathVariable Long id, Authentication authentication,
                                   @Valid @ModelAttribute("nota") NotaDTO dto,
                                   BindingResult result, Model model) {
        validarNotaPropia(id, authentication);
        // La materia de la nota no puede reasignarse desde el área docente: se
        // conserva la original consultada por el propio servicio.
        Long materiaOriginal = notaService.obtenerDTO(id).materiaId();
        dto = new NotaDTO(dto.id(), dto.fecha(), dto.valor(), materiaOriginal);
        if (result.hasErrors()) return "docente/nota-form";
        try {
            NotaDTO actualizada = notaService.editarNota(id, dto);
            return "redirect:/docente/materia/" + actualizada.materiaId() + "/notas";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "docente/nota-form";
        }
    }

    /** Verifica que la materia exista y pertenezca al profesor autenticado; devuelve sus datos. */
    private com.learnhub.dto.MateriaDTO validarMateriaPropia(Long materiaId, Authentication authentication) {
        var profesor = profesorService.buscarPorEmail(authentication.getName());
        var materia = materiaService.obtenerDTO(materiaId);
        if (!materia.profesorId().equals(profesor.id())) {
            throw new BusinessException("No tenés permiso para operar sobre esta materia.");
        }
        return materia;
    }

    /** Verifica que la nota pertenezca a una materia dictada por el profesor autenticado. */
    private void validarNotaPropia(Long notaId, Authentication authentication) {
        var profesor = profesorService.buscarPorEmail(authentication.getName());
        Long profesorIdDeLaNota = notaService.profesorIdDeNota(notaId);
        if (!profesorIdDeLaNota.equals(profesor.id())) {
            throw new BusinessException("No tenés permiso para operar sobre esta nota.");
        }
    }

    @GetMapping("/password")
    public String password(Model model) {
        model.addAttribute("cambioPassword", new CambiarPasswordDTO(null, null, null));
        return "docente/password";
    }

    @PostMapping("/password")
    public String cambiarPassword(Authentication authentication,
                                  @Valid @ModelAttribute("cambioPassword") CambiarPasswordDTO dto,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) return "docente/password";
        try {
            profesorService.cambiarPassword(authentication.getName(), dto);
            model.addAttribute("exito", "Contraseña actualizada correctamente.");
            model.addAttribute("cambioPassword", new CambiarPasswordDTO(null, null, null));
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "docente/password";
    }
}
