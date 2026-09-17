package com.learnhub.dto;

import java.time.Instant;

/** DTO de lectura del historial; la entidad de Envers no llega a Thymeleaf. */
public record AuditEntryDTO(
        String entidad,
        Long idEntidad,
        Long revision,
        Instant fecha,
        String usuario,
        String operacion
) {}
