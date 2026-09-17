package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.HashSet;
import java.util.Set;

/**
 * Cuenta de autenticación del sistema.
 *
 * <p>El correo personal del docente funciona como username. La contraseña
 * nunca se persiste en claro: passwordHash contiene exclusivamente el hash
 * generado por BCrypt.</p>
 *
 * <p>Usuario se mantiene separado de Profesor para no mezclar seguridad con
 * datos de negocio. Esto también permite que exista un usuario ADMIN sin
 * necesidad de que sea un profesor.</p>
 */
@Entity
@Table(name = "usuarios", uniqueConstraints =
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email"))
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Usuario extends BaseEntity {

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    /**
     * Se excluye de Envers para no almacenar históricamente hashes de
     * contraseñas. La modificación de la cuenta sí genera una revisión.
     */
    @NotAudited
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false)
    private boolean habilitado = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();
}
