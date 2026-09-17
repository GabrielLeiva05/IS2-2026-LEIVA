package com.learnhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

/** DTO de Alumno. */
public record AlumnoDTO(
        Long id,
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Size(max = 80) String apellido,
        @NotNull @Past(message = "La fecha debe ser anterior a hoy") LocalDate fechaNacimiento,
        Set<Long> aulaIds,
        boolean eliminado
) {}
