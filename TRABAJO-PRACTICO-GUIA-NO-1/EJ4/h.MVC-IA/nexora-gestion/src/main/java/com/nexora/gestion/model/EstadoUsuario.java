package com.nexora.gestion.model;

/**
 * ============================================================================
 * ENUM "estadoUsuario" (Modelo)
 * ============================================================================
 * Corresponde directamente al "ENUM estadoUsuario" del diagrama UML, con sus
 * dos valores literales: ACTIVO y BLOQUEADO.
 *
 * Se mapea en la entidad {@link Usuario} usando @Enumerated(EnumType.STRING)
 * para que en la base de datos se guarde el nombre legible ("ACTIVO",
 * "BLOQUEADO") en lugar del índice numérico (0, 1), lo cual es una buena
 * práctica de ORM: hace que los datos sean legibles y resistentes a cambios
 * de orden en el enum.
 * ============================================================================
 */
public enum EstadoUsuario {
    ACTIVO,
    BLOQUEADO
}
