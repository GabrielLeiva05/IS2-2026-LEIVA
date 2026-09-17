package com.learnhub.controller;

import com.learnhub.dto.*;
import com.learnhub.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * Controller administrativo (rol ADMIN) para el CRUD MVC completo del diagrama.
 *
 * <p>El controller sólo coordina formularios y DTOs; las reglas permanecen
 * en los servicios. Los errores de negocio no capturados localmente son
 * resueltos por {@link com.learnhub.exception.GlobalExceptionHandler}.</p>
 */
@Controller
@RequestMapping("/gestion")
@RequiredArgsConstructor
public class GestionController {
    private final ProfesorService profesorService;
    private final GradoService gradoService;
    private final AulaService aulaService;
    private final AlumnoService alumnoService;
    private final MateriaService materiaService;
    private final NotaService notaService;

    @GetMapping
    public String inicio(Model model) {
        model.addAttribute("cantProfesores", profesorService.listarProfesores().size());
        model.addAttribute("cantGrados", gradoService.listar().size());
        model.addAttribute("cantAlumnos", alumnoService.listarAlumnos().size());
        model.addAttribute("cantMaterias", materiaService.listarTodas().size());
        return "gestion/index";
    }

    // ==================== PROFESORES ====================

    @GetMapping("/profesores")
    public String profesores(Model model) {
        model.addAttribute("profesores", profesorService.listarTodos());
        return "gestion/profesores";
    }

    @GetMapping("/profesor/{id}/editar")
    public String editarProfesorForm(@PathVariable Long id, Model model) {
        ProfesorDTO p = profesorService.obtenerPorId(id);
        model.addAttribute("profesorId", id);
        model.addAttribute("form", new ProfesorFormDTO(p.nombre(), p.apellido(), p.especialidad(), p.sexo(), p.fechaNacimiento()));
        model.addAttribute("sexos", com.learnhub.enumeration.Sexo.values());
        return "gestion/profesor-form";
    }

    @PostMapping("/profesor/{id}/editar")
    public String editarProfesor(@PathVariable Long id, @Valid @ModelAttribute("form") ProfesorFormDTO dto,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("profesorId", id);
            model.addAttribute("sexos", com.learnhub.enumeration.Sexo.values());
            return "gestion/profesor-form";
        }
        try {
            profesorService.editarProfesorPorId(id, dto);
            return "redirect:/gestion/profesores";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("profesorId", id);
            model.addAttribute("sexos", com.learnhub.enumeration.Sexo.values());
            return "gestion/profesor-form";
        }
    }

    @PostMapping("/profesor/{id}/eliminar")
    public String eliminarProfesor(@PathVariable Long id) {
        profesorService.eliminarProfesorPorId(id);
        return "redirect:/gestion/profesores";
    }

    @GetMapping("/profesor/{id}/materias")
    public String materiasDelProfesor(@PathVariable Long id, Model model) {
        model.addAttribute("profesor", profesorService.obtenerPorId(id));
        model.addAttribute("materias", materiaService.listarPorProfesor(id));
        model.addAttribute("nombresAlumnos", nombresDeAlumnosPorId());
        return "gestion/profesor-materias";
    }

    /** Mapa id -> "Nombre Apellido" usado por las vistas que sólo tienen el id de un alumno. */
    private java.util.Map<Long, String> nombresDeAlumnosPorId() {
        return alumnoService.listarTodos().stream()
                .collect(java.util.stream.Collectors.toMap(AlumnoDTO::id, a -> a.nombre() + " " + a.apellido()));
    }

    // ==================== GRADOS Y AULAS ====================

    @GetMapping("/grados")
    public String grados(Model model) {
        model.addAttribute("grados", gradoService.listar());
        return "gestion/grados";
    }

    @GetMapping("/grado/nuevo")
    public String gradoForm(Model model) {
        model.addAttribute("grado", new GradoDTO(null, null));
        return "gestion/grado-form";
    }

    @PostMapping("/grado")
    public String crearGrado(@Valid @ModelAttribute("grado") GradoDTO dto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) return "gestion/grado-form";
        try { gradoService.crear(dto); return "redirect:/gestion/grados"; }
        catch (RuntimeException ex) { model.addAttribute("error", ex.getMessage()); return "gestion/grado-form"; }
    }

    @GetMapping("/grado/{id}/aulas")
    public String aulasDelGrado(@PathVariable Long id, Model model) {
        model.addAttribute("grado", gradoService.obtenerDTO(id));
        model.addAttribute("aulas", aulaService.listarPorGrado(id));
        return "gestion/grado-aulas";
    }

    @GetMapping("/aula/nueva")
    public String aulaForm(@RequestParam(required = false) Long gradoId, Model model) {
        model.addAttribute("aula", new AulaDTO(null, null, gradoId, 0));
        model.addAttribute("grados", gradoService.listar());
        return "gestion/aula-form";
    }

    @PostMapping("/aula")
    public String crearAula(@Valid @ModelAttribute("aula") AulaDTO dto,
                            BindingResult result, Model model) {
        if (result.hasErrors()) { model.addAttribute("grados", gradoService.listar()); return "gestion/aula-form"; }
        try {
            AulaDTO creada = aulaService.crear(dto);
            return "redirect:/gestion/grado/" + creada.gradoId() + "/aulas";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("grados", gradoService.listar());
            return "gestion/aula-form";
        }
    }

    @GetMapping("/aula/{id}")
    public String aulaDetalle(@PathVariable Long id, Model model) {
        AulaDTO aula = aulaService.obtenerDTO(id);
        model.addAttribute("aula", aula);
        model.addAttribute("grado", gradoService.obtenerDTO(aula.gradoId()));
        model.addAttribute("alumnosDelAula", aulaService.alumnosDelAula(id));
        model.addAttribute("alumnosDisponibles", aulaService.alumnosDisponibles(id));
        return "gestion/aula-detalle";
    }

    @PostMapping("/aula/{id}/alumnos")
    public String asociarAlumno(@PathVariable Long id, @RequestParam Long alumnoId, Model model) {
        try {
            aulaService.asociarAlumno(id, alumnoId);
        } catch (RuntimeException ex) {
            AulaDTO aula = aulaService.obtenerDTO(id);
            model.addAttribute("aula", aula);
            model.addAttribute("grado", gradoService.obtenerDTO(aula.gradoId()));
            model.addAttribute("alumnosDelAula", aulaService.alumnosDelAula(id));
            model.addAttribute("alumnosDisponibles", aulaService.alumnosDisponibles(id));
            model.addAttribute("error", ex.getMessage());
            return "gestion/aula-detalle";
        }
        return "redirect:/gestion/aula/" + id;
    }

    // ==================== ALUMNOS ====================

    @GetMapping("/alumnos")
    public String alumnos(Model model) {
        model.addAttribute("alumnos", alumnoService.listarTodos());
        return "gestion/alumnos";
    }

    @GetMapping("/alumno/nuevo")
    public String alumnoForm(Model model) {
        model.addAttribute("alumno", new AlumnoDTO(null, null, null, LocalDate.of(2000,1,1), Set.of(), false));
        cargarDatosFormularioAlumno(model);
        model.addAttribute("accion", "/gestion/alumno");
        return "gestion/alumno-form";
    }

    @PostMapping("/alumno")
    public String crearAlumno(@Valid @ModelAttribute("alumno") AlumnoDTO dto,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("alumno", dto);
            cargarDatosFormularioAlumno(model);
            model.addAttribute("accion", "/gestion/alumno");
            return "gestion/alumno-form";
        }
        try { alumnoService.registrarAlumno(dto); return "redirect:/gestion/alumnos"; }
        catch (RuntimeException ex) {
            model.addAttribute("alumno", dto);
            model.addAttribute("error", ex.getMessage());
            cargarDatosFormularioAlumno(model);
            model.addAttribute("accion", "/gestion/alumno");
            return "gestion/alumno-form";
        }
    }

    @GetMapping("/alumno/{id}/editar")
    public String editarAlumnoForm(@PathVariable Long id, Model model) {
        model.addAttribute("alumno", alumnoService.obtenerDTO(id));
        cargarDatosFormularioAlumno(model);
        model.addAttribute("accion", "/gestion/alumno/" + id + "/editar");
        return "gestion/alumno-form";
    }

    @PostMapping("/alumno/{id}/editar")
    public String editarAlumno(@PathVariable Long id, @Valid @ModelAttribute("alumno") AlumnoDTO dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("alumno", dto);
            cargarDatosFormularioAlumno(model);
            model.addAttribute("accion", "/gestion/alumno/" + id + "/editar");
            return "gestion/alumno-form";
        }
        try { alumnoService.editarAlumno(id, dto); return "redirect:/gestion/alumnos"; }
        catch (RuntimeException ex) {
            model.addAttribute("alumno", dto);
            model.addAttribute("error", ex.getMessage());
            cargarDatosFormularioAlumno(model);
            model.addAttribute("accion", "/gestion/alumno/" + id + "/editar");
            return "gestion/alumno-form";
        }
    }

    private void cargarDatosFormularioAlumno(Model model) {
        model.addAttribute("todasLasAulas", aulaService.listarTodas());
        model.addAttribute("nombresGrados", gradoService.listar().stream()
                .collect(java.util.stream.Collectors.toMap(GradoDTO::id, GradoDTO::nivel)));
    }

    @PostMapping("/alumno/{id}/eliminar")
    public String eliminarAlumno(@PathVariable Long id) {
        alumnoService.eliminarAlumno(id);
        return "redirect:/gestion/alumnos";
    }

    // ==================== MATERIAS ====================

    @GetMapping("/materias")
    public String materias(Model model) {
        model.addAttribute("materias", materiaService.listarTodas());
        model.addAttribute("profesores", profesorService.listarProfesores());
        model.addAttribute("alumnos", alumnoService.listarAlumnos());
        model.addAttribute("nombresAlumnos", nombresDeAlumnosPorId());
        model.addAttribute("nombresProfesores", profesorService.listarTodos().stream()
                .collect(java.util.stream.Collectors.toMap(ProfesorDTO::id, p -> p.nombre() + " " + p.apellido())));
        return "gestion/materias";
    }

    @GetMapping("/materia/nueva")
    public String materiaForm(Model model) {
        model.addAttribute("materia", new MateriaDTO(null, null, null, null, false));
        model.addAttribute("profesores", profesorService.listarProfesores());
        model.addAttribute("alumnos", alumnoService.listarAlumnos());
        model.addAttribute("accion", "/gestion/materia");
        return "gestion/materia-form";
    }

    @PostMapping("/materia")
    public String crearMateria(@Valid @ModelAttribute("materia") MateriaDTO dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("profesores", profesorService.listarProfesores());
            model.addAttribute("alumnos", alumnoService.listarAlumnos());
            model.addAttribute("accion", "/gestion/materia");
            return "gestion/materia-form";
        }
        try { materiaService.registrarMateria(dto); return "redirect:/gestion/materias"; }
        catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("profesores", profesorService.listarProfesores());
            model.addAttribute("alumnos", alumnoService.listarAlumnos());
            model.addAttribute("accion", "/gestion/materia");
            return "gestion/materia-form";
        }
    }

    @GetMapping("/materia/{id}/editar")
    public String editarMateriaForm(@PathVariable Long id, Model model) {
        model.addAttribute("materia", materiaService.obtenerDTO(id));
        model.addAttribute("profesores", profesorService.listarProfesores());
        model.addAttribute("alumnos", alumnoService.listarAlumnos());
        model.addAttribute("accion", "/gestion/materia/" + id + "/editar");
        return "gestion/materia-form";
    }

    @PostMapping("/materia/{id}/editar")
    public String editarMateria(@PathVariable Long id, @Valid @ModelAttribute("materia") MateriaDTO dto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("profesores", profesorService.listarProfesores());
            model.addAttribute("alumnos", alumnoService.listarAlumnos());
            model.addAttribute("accion", "/gestion/materia/" + id + "/editar");
            return "gestion/materia-form";
        }
        try { materiaService.editarMateria(id, dto); return "redirect:/gestion/materias"; }
        catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("profesores", profesorService.listarProfesores());
            model.addAttribute("alumnos", alumnoService.listarAlumnos());
            model.addAttribute("accion", "/gestion/materia/" + id + "/editar");
            return "gestion/materia-form";
        }
    }

    @PostMapping("/materia/{id}/eliminar")
    public String eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
        return "redirect:/gestion/materias";
    }

    @GetMapping("/materia/{id}/notas")
    public String notasDeMateria(@PathVariable Long id, Model model) {
        MateriaDTO materia = materiaService.obtenerDTO(id);
        ProfesorDTO profesor = profesorService.obtenerPorId(materia.profesorId());
        AlumnoDTO alumno = alumnoService.obtenerDTO(materia.alumnoId());
        model.addAttribute("materia", materia);
        model.addAttribute("nombreProfesor", profesor.nombre() + " " + profesor.apellido());
        model.addAttribute("nombreAlumno", alumno.nombre() + " " + alumno.apellido());
        model.addAttribute("notas", notaService.listarPorMateria(id));
        model.addAttribute("nota", new NotaDTO(null, LocalDate.now(), null, id));
        return "gestion/materia-notas";
    }

    // ==================== NOTAS ====================

    @GetMapping("/nota/nueva")
    public String notaForm(@RequestParam(required = false) Long materiaId, Model model) {
        model.addAttribute("nota", new NotaDTO(null, LocalDate.now(), null, materiaId));
        model.addAttribute("materias", materiaService.listarTodas());
        model.addAttribute("accion", "/gestion/nota");
        return "gestion/nota-form";
    }

    @PostMapping("/nota")
    public String crearNota(@Valid @ModelAttribute("nota") NotaDTO dto,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("materias", materiaService.listarTodas());
            model.addAttribute("accion", "/gestion/nota");
            return "gestion/nota-form";
        }
        try {
            notaService.registrarNota(dto);
            return "redirect:/gestion/materia/" + dto.materiaId() + "/notas";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("materias", materiaService.listarTodas());
            model.addAttribute("accion", "/gestion/nota");
            return "gestion/nota-form";
        }
    }

    @GetMapping("/nota/{id}/editar")
    public String editarNotaForm(@PathVariable Long id, Model model) {
        model.addAttribute("nota", notaService.obtenerDTO(id));
        model.addAttribute("materias", materiaService.listarTodas());
        model.addAttribute("accion", "/gestion/nota/" + id + "/editar");
        return "gestion/nota-form";
    }

    @PostMapping("/nota/{id}/editar")
    public String editarNota(@PathVariable Long id, @Valid @ModelAttribute("nota") NotaDTO dto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("materias", materiaService.listarTodas());
            model.addAttribute("accion", "/gestion/nota/" + id + "/editar");
            return "gestion/nota-form";
        }
        try {
            NotaDTO actualizada = notaService.editarNota(id, dto);
            return "redirect:/gestion/materia/" + actualizada.materiaId() + "/notas";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("materias", materiaService.listarTodas());
            model.addAttribute("accion", "/gestion/nota/" + id + "/editar");
            return "gestion/nota-form";
        }
    }
}
