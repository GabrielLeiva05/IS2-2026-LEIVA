package com.furnish.erp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * ============================================================================
 * Manejador global de excepciones para toda la capa Controlador.
 * ============================================================================
 * @ControllerAdvice intercepta las excepciones lanzadas por CUALQUIER
 * @Controller de la aplicación (equivalente a un "try/catch" transversal),
 * evitando repetir manejo de errores en cada método de cada controlador.
 *
 * Devuelve siempre una VISTA Thymeleaf (no JSON), ya que esta es una
 * aplicación MVC tradicional server-side rendering.
 * ============================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("mensaje", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ModelAndView handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.warn("Regla de negocio violada en {}: {}", request.getRequestURI(), ex.getMessage());
        // Se muestra una vista dedicada con el mensaje de negocio y un botón
        // para volver, en vez de redirigir "a ciegas" (más simple y explícito
        // que depender del header Referer, que no siempre está presente).
        ModelAndView mv = new ModelAndView("error/negocio");
        mv.addObject("mensaje", ex.getMessage());
        return mv;
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDenied(Exception ex) {
        return new ModelAndView("error/403");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneric(Exception ex) {
        log.error("Error inesperado", ex);
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("mensaje", "Ocurrió un error inesperado. Contacte al administrador.");
        return mv;
    }
}
