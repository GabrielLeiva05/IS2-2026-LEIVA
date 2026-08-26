package com.nexora.gestion.exception;

/**
 * Excepción de negocio lanzada al intentar registrar un Usuario o
 * Administrador con un correo que ya existe en la base de datos.
 */
public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
