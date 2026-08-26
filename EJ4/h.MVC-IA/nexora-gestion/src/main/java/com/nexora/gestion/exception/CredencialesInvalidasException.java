package com.nexora.gestion.exception;

/**
 * Excepción de negocio lanzada por la capa de Servicio cuando el correo no
 * existe o la contraseña no coincide durante un inicio de sesión.
 * Se usa RuntimeException (unchecked) para no ensuciar las firmas de los
 * métodos de Service/Controller con "throws" explícitos.
 */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
