package com.furnish.erp.domain;

import com.furnish.erp.domain.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * ============================================================================
 * Entidad JPA: Usuario
 * ============================================================================
 * EXTENSIÓN AGREGADA AL DIAGRAMA ORIGINAL, solicitada explícitamente por el
 * cliente: "quienes acceden al sistema lo realizan por medio de un usuario y
 * contraseña". El diagrama de clases no incluía esta entidad, por lo que se
 * la modela como una tabla propia (no se reutiliza Empleado como usuario)
 * para separar responsabilidades:
 *   - Empleado -> datos de RRHH / negocio (quién soy en la empresa).
 *   - Usuario  -> credenciales de acceso al sistema (cómo entro al sistema).
 *
 * Un Usuario puede estar asociado opcionalmente a un Empleado (relación
 * 0..1 -- 1) para saber "qué empleado es" quien inició sesión; también se
 * permite crear un usuario ADMIN sin Empleado asociado (superusuario técnico).
 *
 * La contraseña NUNCA se guarda en texto plano: se persiste el hash BCrypt
 * generado por Spring Security (ver UsuarioServiceImpl / SecurityConfig).
 * ============================================================================
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 60)
    @Column(name = "username", nullable = false, unique = true, length = 60)
    private String username;

    /** Hash BCrypt de la contraseña (nunca texto plano). */
    @NotBlank
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /** NOTA: se usa @NotNull (y no @NotBlank) porque "rol" es un enum, no un
     *  String; @NotBlank solo aplica a CharSequence y provoca una excepción
     *  en tiempo de ejecución (HV000030) si se usa sobre un tipo no textual. */
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private Rol rol;

    @Builder.Default
    @Column(name = "habilitado", nullable = false)
    private boolean habilitado = true;

    /** Relación opcional 1 a 1 con Empleado: no todo usuario técnico (ej.
     *  el admin de sistema) tiene por qué corresponder a un Empleado de RRHH. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = true, unique = true)
    private Empleado empleado;
}
