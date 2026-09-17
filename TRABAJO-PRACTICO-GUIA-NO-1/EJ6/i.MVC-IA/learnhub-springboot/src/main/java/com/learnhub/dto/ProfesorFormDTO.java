package com.learnhub.dto;

import com.learnhub.enumeration.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** DTO para editar los datos de negocio de un profesor autenticado. */
public record ProfesorFormDTO(
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Size(max = 80) String apellido,
        @NotBlank @Size(max = 120) String especialidad,
        @NotNull Sexo sexo,
        @NotNull @Past(message = "La fecha debe ser anterior a hoy") LocalDate fechaNacimiento
) {}
