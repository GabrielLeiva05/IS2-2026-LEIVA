package com.learnhub.dto;

import com.learnhub.enumeration.Sexo;

import java.time.LocalDate;

/** DTO de salida de Profesor; nunca expone la entidad JPA ni el hash. */
public record ProfesorDTO(
        Long id,
        String nombre,
        String apellido,
        String especialidad,
        Sexo sexo,
        LocalDate fechaNacimiento,
        String email,
        boolean eliminado
) {}
