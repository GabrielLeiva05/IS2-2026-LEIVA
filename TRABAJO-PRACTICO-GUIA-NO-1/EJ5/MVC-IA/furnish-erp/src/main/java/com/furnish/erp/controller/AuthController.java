package com.furnish.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ============================================================================
 * Controlador: AuthController
 * ============================================================================
 * Solo expone la VISTA del formulario de login (GET /login). El
 * procesamiento del POST /login (validar usuario/contraseña) NO lo maneja
 * este controlador: lo intercepta directamente el filtro de Spring Security
 * configurado en SecurityConfig (loginProcessingUrl), que es quien contiene
 * la lógica de autenticación real.
 * ============================================================================
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // templates/login.html
    }
}
