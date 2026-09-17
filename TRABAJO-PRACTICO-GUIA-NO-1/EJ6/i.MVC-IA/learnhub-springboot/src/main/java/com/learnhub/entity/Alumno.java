package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Alumno del diagrama. eliminado implementa baja lógica.
 *
 * <p>La relación con Aula es ManyToMany porque así está expresada en el
 * diagrama adjunto (1..* en ambos extremos).</p>
 */
@Entity
@Table(name = "alumnos")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Alumno extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private boolean eliminado = false;

    @ManyToMany(mappedBy = "alumnos", fetch = FetchType.LAZY)
    private Set<Aula> aulas = new HashSet<>();

    /** Método de dominio equivalente a registrarAlumno. */
    public void registrarAlumno() {
        this.eliminado = false;
    }

    /** Método de dominio equivalente a eliminarAlumno. */
    public void eliminarAlumno() {
        this.eliminado = true;
    }
}
