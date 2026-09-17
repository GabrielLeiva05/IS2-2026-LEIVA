package com.learnhub.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/** DTO de Nota; la escala de negocio utilizada es 0 a 10. */
public record NotaDTO(
        Long id,
        @NotNull @PastOrPresent(message = "La fecha no puede ser futura") LocalDate fecha,
        @NotNull @DecimalMin(value = "0.0") @DecimalMax(value = "10.0") Float valor,
        @NotNull Long materiaId
) {}
