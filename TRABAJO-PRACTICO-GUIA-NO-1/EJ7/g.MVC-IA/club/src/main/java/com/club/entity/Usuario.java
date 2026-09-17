package com.club.entity;

import com.club.enumeration.Rol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_username", columnNames = "username"))
@Getter @Setter @NoArgsConstructor
public class Usuario {
    @Id @GeneratedValue @UuidGenerator @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(nullable = false, length = 60) private String username;
    @Column(nullable = false, length = 100) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private Rol rol;
    @Column(nullable = false) private boolean activo = true;
    public Usuario(String username, String password, Rol rol) { this.username = username; this.password = password; this.rol = rol; }
}
