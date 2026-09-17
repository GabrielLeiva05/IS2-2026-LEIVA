package com.learnhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * Rol de autorización. Se modela como entidad para que el sistema pueda
 * crecer sin modificar la tabla Usuario cada vez que aparezca un rol nuevo.
 */
@Entity
@Table(name = "roles")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Rol extends BaseEntity {

    @Column(nullable = false, unique = true, length = 40)
    private String nombre;

    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
