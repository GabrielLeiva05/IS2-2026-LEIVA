package com.nexora.gestion.controller;

import com.nexora.gestion.config.SesionConstantes;
import com.nexora.gestion.dto.LoginForm;
import com.nexora.gestion.dto.RegistroUsuarioForm;
import com.nexora.gestion.exception.CredencialesInvalidasException;
import com.nexora.gestion.exception.EmailDuplicadoException;
import com.nexora.gestion.exception.UsuarioBloqueadoException;
import com.nexora.gestion.model.Administrador;
import com.nexora.gestion.model.Usuario;
import com.nexora.gestion.service.AdministradorService;
import com.nexora.gestion.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * "AuthController" (Capa de Controlador)
 * ============================================================================
 * Controla el flujo de autenticación (login/logout) y registro público de
 * Usuarios. NO decide reglas de negocio (nunca compara contraseñas ni
 * cuenta intentos fallidos acá): delega esa responsabilidad íntegramente en
 * UsuarioService/AdministradorService y solo reacciona al resultado
 * (éxito -> guarda datos en sesión y redirige; excepción -> muestra el
 * mensaje de error correspondiente en la vista).
 * ============================================================================
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    public AuthController(UsuarioService usuarioService, AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.administradorService = administradorService;
    }

    // ------------------------------------------------------------------
    // LOGIN
    // ------------------------------------------------------------------

    @GetMapping("/login")
    public String mostrarLogin(@ModelAttribute("loginForm") LoginForm loginForm,
                                @RequestParam(required = false) String error,
                                Model model) {
        if ("acceso-restringido".equals(error)) {
            model.addAttribute("mensajeError", "Debés iniciar sesión para acceder a esa sección.");
        }
        return "login"; // -> templates/login.html
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            if (SesionConstantes.TIPO_ADMINISTRADOR.equals(loginForm.getTipoCuenta())) {
                Administrador admin = administradorService.iniciarSesion(loginForm.getCorreo(), loginForm.getPassword());
                session.setAttribute(SesionConstantes.SESSION_ID, admin.getId());
                session.setAttribute(SesionConstantes.SESSION_TIPO, SesionConstantes.TIPO_ADMINISTRADOR);
                session.setAttribute(SesionConstantes.SESSION_NOMBRE, admin.getNombreCompleto());
                return "redirect:/admin/productos";
            } else {
                Usuario usuario = usuarioService.iniciarSesion(loginForm.getCorreo(), loginForm.getPassword());
                session.setAttribute(SesionConstantes.SESSION_ID, usuario.getId());
                session.setAttribute(SesionConstantes.SESSION_TIPO, SesionConstantes.TIPO_USUARIO);
                session.setAttribute(SesionConstantes.SESSION_NOMBRE, usuario.getNombreCompleto());
                return "redirect:/";
            }
        } catch (UsuarioBloqueadoException | CredencialesInvalidasException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "login";
        }
    }

    // ------------------------------------------------------------------
    // REGISTRO (alta pública de Usuario)
    // ------------------------------------------------------------------

    @GetMapping("/registro")
    public String mostrarRegistro(@ModelAttribute("registroForm") RegistroUsuarioForm registroForm) {
        return "registro"; // -> templates/registro.html
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("registroForm") RegistroUsuarioForm form,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registro";
        }
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(form.getNombre());
            usuario.setApellido(form.getApellido());
            usuario.setDocumento(form.getDocumento());
            usuario.setFechaDeNacimiento(form.getFechaDeNacimiento());
            usuario.setCorreo(form.getCorreo());
            usuario.setPassword(form.getPassword());
            usuarioService.registrar(usuario);

            redirectAttributes.addFlashAttribute("mensajeExito",
                    "¡Cuenta creada correctamente! Ya podés iniciar sesión.");
            return "redirect:/login";
        } catch (EmailDuplicadoException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "registro";
        }
    }

    // ------------------------------------------------------------------
    // LOGOUT
    // ------------------------------------------------------------------

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
