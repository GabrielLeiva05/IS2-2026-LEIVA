package com.furnish.erp.exception;

/**
 * Excepción genérica de REGLAS DE NEGOCIO. Se lanza únicamente desde la
 * capa Service (por ejemplo: "no se puede eliminar un producto con stock
 * positivo", "no se puede cambiar el estado de una orden ya cancelada",
 * "el DNI ya está registrado", etc.). El controlador la captura (o la deja
 * subir al GlobalExceptionHandler) y muestra el mensaje al usuario mediante
 * un flash message, sin exponer detalles técnicos.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
