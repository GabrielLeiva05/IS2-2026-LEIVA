package com.learnhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTO de Aula; gradoId representa la FK sin exponer una entidad JPA. */
public record AulaDTO(
        Long id,
        @NotBlank @Size(max = 20) String division,
        @NotNull Long gradoId,
        Integer cantidadAlumnos
) {}
