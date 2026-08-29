package com.furnish.erp.exception;

/**
 * Se lanza desde la capa Service cuando se busca una entidad por id y no
 * existe (o fue eliminada lógicamente). El GlobalExceptionHandler la
 * traduce a una respuesta HTTP 404 con una vista de error amigable.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
