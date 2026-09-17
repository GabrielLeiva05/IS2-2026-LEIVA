package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

/**
 * Aula perteneciente a un Grado.
 *
 * <p>El diagrama representa la asociación Aula-Alumno como 1..* en ambos
 * extremos. Por lo tanto se implementa literalmente como ManyToMany con una
 * tabla intermedia aula_alumno.</p>
 */
@Entity
@Table(name = "aulas")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Aula extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String division;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grado_id", nullable = false)
    private Grado grado;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "aula_alumno",
            joinColumns = @JoinColumn(name = "aula_id"),
            inverseJoinColumns = @JoinColumn(name = "alumno_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_aula_alumno", columnNames = {"aula_id", "alumno_id"}))
    private Set<Alumno> alumnos = new HashSet<>();

    /** Devuelve la cantidad de alumnos asociados al aula. */
    public int obtenerCantidadAlumnos() {
        return alumnos.size();
    }
}
