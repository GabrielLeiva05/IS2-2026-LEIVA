package com.learnhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO de Grado. */
public record GradoDTO(Long id, @NotBlank @Size(max = 50) String nivel) {}
