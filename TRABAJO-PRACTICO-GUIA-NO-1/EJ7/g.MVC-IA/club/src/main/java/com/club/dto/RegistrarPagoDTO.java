package com.club.dto;

import com.club.enumeration.MedioPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RegistrarPagoDTO(
        @NotBlank String periodo,
        @NotNull @DecimalMin(value = "0.01") BigDecimal importe,
        @NotNull MedioPago medioPago,
        String referencia
) {}
