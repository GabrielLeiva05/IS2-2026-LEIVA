package com.learnhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO aislado para nunca transportar una entidad con contraseñas. */
public record CambiarPasswordDTO(
        @NotBlank String passwordActual,
        @NotBlank @Size(min = 8, max = 72) String nuevaPassword,
        @NotBlank String confirmarNuevaPassword
) {}
