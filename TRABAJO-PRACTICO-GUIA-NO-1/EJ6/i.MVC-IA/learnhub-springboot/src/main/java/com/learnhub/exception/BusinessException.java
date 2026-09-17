package com.learnhub.exception;

/** Excepción controlada para reglas de negocio violadas. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}
