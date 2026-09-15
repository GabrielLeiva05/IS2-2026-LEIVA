package com.furnish.erp.controller;

import com.furnish.erp.dto.UsuarioForm;
import com.furnish.erp.service.EmpleadoService;
import com.furnish.erp.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador del módulo de administración de Usuarios (acceso al sistema).
 * Restringido a ROLE_ADMIN a nivel de configuración (ver SecurityConfig:
 * "/usuarios/**" -> hasRole("ADMIN")).
 */
@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        model.addAttribute("empleados", empleadoService.listarActivos());
        return "usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        var usuario = usuarioService.buscarPorId(id);
        UsuarioForm form = new UsuarioForm();
        form.setIdUsuario(usuario.getIdUsuario());
        form.setUsername(usuario.getUsername());
        form.setRol(usuario.getRol());
        form.setHabilitado(usuario.isHabilitado());
        form.setIdEmpleado(usuario.getEmpleado() != null ? usuario.getEmpleado().getIdEmpleado() : null);
        model.addAttribute("usuarioForm", form);
        model.addAttribute("empleados", empleadoService.listarActivos());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuarioForm") UsuarioForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleados", empleadoService.listarActivos());
            return "usuarios/form";
        }
        if (form.getIdUsuario() == null) {
            usuarioService.registrarUsuario(form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario creado correctamente.");
        } else {
            usuarioService.editarUsuario(form.getIdUsuario(), form);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado correctamente.");
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminarUsuario(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }
}
