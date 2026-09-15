package com.nexora.gestion.exception;

/**
 * Excepción genérica para cuando se busca una entidad por id y no existe
 * (equivalente conceptual a un 404, pero manejada dentro de la app MVC
 * con redirecciones y mensajes flash en vez de un status HTTP puro).
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
