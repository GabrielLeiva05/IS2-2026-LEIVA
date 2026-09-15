package com.nexora.gestion.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ============================================================================
 * "GlobalExceptionHandler" (Capa de Controlador — transversal)
 * ============================================================================
 * @ControllerAdvice + @ExceptionHandler centraliza el manejo de excepciones
 * que NO fueron atrapadas puntualmente por un try/catch dentro de un
 * Controller específico (los flujos "esperables", como credenciales
 * inválidas o stock insuficiente, sí se atrapan localmente en cada
 * Controller para dar una mejor experiencia de usuario -mostrar el error
 * en el mismo formulario-). Esta clase es una RED DE SEGURIDAD para
 * cualquier error inesperado, mostrando una página de error genérica en
 * lugar de una stacktrace cruda de Spring.
 * ============================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarRecursoNoEncontrado(RecursoNoEncontradoException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error"; // -> templates/error.html
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String manejarErroresDeNegocio(RuntimeException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String manejarErrorGenerico(Exception ex, Model model) {
        // El usuario ve un mensaje amigable; el detalle real queda en la consola
        // para poder diagnosticar cualquier problema que no sea de negocio.
        log.error("Error no controlado en Nexora Gestión", ex);
        model.addAttribute("mensaje", "Ocurrió un error inesperado. Por favor, intentá nuevamente.");
        return "error";
    }
}
