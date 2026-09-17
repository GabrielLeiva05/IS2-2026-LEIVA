package com.learnhub.dto;

import com.learnhub.enumeration.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** DTO de entrada del formulario público de registro del docente/profesor. */
public record RegistroProfesorDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 80, message = "El apellido no puede superar 80 caracteres")
        String apellido,
        @NotBlank(message = "La especialidad es obligatoria")
        @Size(max = 120, message = "La especialidad no puede superar 120 caracteres")
        String especialidad,
        @NotNull(message = "Debe seleccionar el sexo") Sexo sexo,
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
        LocalDate fechaNacimiento,
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Ingrese un correo válido")
        @Size(max = 180, message = "El correo no puede superar 180 caracteres")
        String email,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String password,
        @NotBlank(message = "Debe repetir la contraseña") String confirmPassword
) {}
