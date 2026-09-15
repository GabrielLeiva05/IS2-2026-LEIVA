package com.furnish.erp.dto;

import com.furnish.erp.domain.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para alta/edición de Usuario desde el panel de administración. Separa
 * la contraseña en texto plano (solo viaja en este formulario, nunca se
 * persiste tal cual) del hash BCrypt que finalmente se guarda en la entidad
 * Usuario (ver UsuarioServiceImpl.registrarUsuario()).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioForm {

    private Long idUsuario; // null = alta, no null = edición

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 60)
    private String username;

    /** Opcional en edición (si se deja vacío, no se modifica la contraseña
     *  actual); obligatorio en alta -> validado a mano en el Service. */
    private String password;

    @NotNull(message = "Debe seleccionar un rol")
    private Rol rol;

    /** Empleado opcional a vincular con este usuario. */
    private Long idEmpleado;

    private boolean habilitado = true;
}
