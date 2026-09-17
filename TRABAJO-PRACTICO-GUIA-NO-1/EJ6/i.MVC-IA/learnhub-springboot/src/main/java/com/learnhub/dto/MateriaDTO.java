package com.learnhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTO de Materia con IDs para sus asociaciones. */
public record MateriaDTO(
        Long id,
        @NotBlank @Size(max = 120) String nombre,
        @NotNull Long profesorId,
        @NotNull Long alumnoId,
        boolean eliminado
) {}
