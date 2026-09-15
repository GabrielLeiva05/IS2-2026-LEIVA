package com.nexora.gestion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * ============================================================================
 * DTO "LoginForm" (Data Transfer Object)
 * ============================================================================
 * Los DTOs son objetos simples usados para transportar datos entre la Vista
 * (formularios Thymeleaf) y el Controller, SIN exponer directamente las
 * entidades JPA (Usuario/Administrador) a la capa web. Esto evita, por
 * ejemplo, que un formulario HTML pueda "setear" accidentalmente (o
 * maliciosamente) campos sensibles de la entidad que no debería poder tocar
 * (mass assignment). Las anotaciones de Bean Validation (@NotBlank, @Email)
 * se validan automáticamente en el Controller con @Valid antes de que el
 * flujo llegue al Service.
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 * ============================================================================
 */
public class LoginForm {

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo no tiene un formato válido.")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    /** Indica si se intenta loguear como "USUARIO" o "ADMINISTRADOR".
     *  Se resuelve mediante un simple selector en la vista de login. */
    private String tipoCuenta = "USUARIO";

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
}
