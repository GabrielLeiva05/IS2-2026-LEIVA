package com.nexora.gestion.controller;

import com.nexora.gestion.config.SesionConstantes;
import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.service.AdministradorService;
import com.nexora.gestion.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * "UsuarioController" (Capa de Controlador) — Panel de administración
 * ============================================================================
 * Rutas bajo "/admin/usuarios/**". Protegidas por {@code SesionInterceptor}:
 * solo un Administrador autenticado puede acceder (ver WebConfig +
 * SesionInterceptor). Implementa la parte de "gestionarUsuario()" del
 * diagrama UML (listar/bloquear/desbloquear/eliminar) y delega
 * "desbloquearUsuario()" en AdministradorService, tal como lo define el
 * diagrama (esa operación pertenece conceptualmente a Administrador).
 * ============================================================================
 */
@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    public UsuarioController(UsuarioService usuarioService, AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.administradorService = administradorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/list"; // -> templates/usuarios/list.html
    }

    @PostMapping("/{id}/bloquear")
    public String bloquear(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.bloquear(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario bloqueado correctamente.");
        } catch (RecursoNoEncontradoException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/desbloquear")
    public String desbloquear(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Long adminId = (Long) session.getAttribute(SesionConstantes.SESSION_ID);
            administradorService.desbloquearUsuario(adminId, id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario desbloqueado correctamente.");
        } catch (RecursoNoEncontradoException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
