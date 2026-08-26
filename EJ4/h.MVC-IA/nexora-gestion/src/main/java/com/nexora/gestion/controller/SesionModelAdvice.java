package com.nexora.gestion.controller;

import com.nexora.gestion.config.SesionConstantes;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * ============================================================================
 * "SesionModelAdvice" (Capa de Controlador — transversal)
 * ============================================================================
 * @ControllerAdvice + @ModelAttribute aplica estos atributos al Model de
 * TODAS las respuestas de TODOS los @Controller de la aplicación, sin tener
 * que repetir "model.addAttribute(...)" en cada método. Así, cualquier
 * plantilla Thymeleaf (por ejemplo, el fragmento de navbar) puede leer
 * "sessionNombre" y "sessionTipo" para decidir si mostrar "Iniciar sesión"
 * o el nombre del usuario logueado + botón de "Cerrar sesión".
 * ============================================================================
 */
@ControllerAdvice
public class SesionModelAdvice {

    @ModelAttribute("sessionNombre")
    public String sessionNombre(HttpSession session) {
        Object valor = session.getAttribute(SesionConstantes.SESSION_NOMBRE);
        return valor != null ? valor.toString() : null;
    }

    @ModelAttribute("sessionTipo")
    public String sessionTipo(HttpSession session) {
        Object valor = session.getAttribute(SesionConstantes.SESSION_TIPO);
        return valor != null ? valor.toString() : null;
    }
}
