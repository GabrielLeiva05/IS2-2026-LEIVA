package com.club.dto;

import jakarta.validation.constraints.NotBlank;
public record RegistrarAccesoDTO(@NotBlank String personaId) {}
