package com.nexora.gestion.exception;

/**
 * Excepción de negocio lanzada cuando un Usuario intenta iniciar sesión
 * estando con estado BLOQUEADO (regla decidida en UsuarioServiceImpl).
 */
public class UsuarioBloqueadoException extends RuntimeException {
    public UsuarioBloqueadoException(String mensaje) {
        super(mensaje);
    }
}
