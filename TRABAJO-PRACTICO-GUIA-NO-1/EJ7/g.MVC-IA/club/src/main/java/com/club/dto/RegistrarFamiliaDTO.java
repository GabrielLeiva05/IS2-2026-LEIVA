package com.club.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RegistrarFamiliaDTO(
        @NotBlank String nombre,
        @NotNull @DecimalMin(value = "0.01") BigDecimal cuotaMensual,
        @NotBlank String titularId
) {}
