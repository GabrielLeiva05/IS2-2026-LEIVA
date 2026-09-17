package com.learnhub.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Convierte errores de negocio no manejados por un controller en una vista
 * amigable, evitando filtrar stack traces al navegador.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
