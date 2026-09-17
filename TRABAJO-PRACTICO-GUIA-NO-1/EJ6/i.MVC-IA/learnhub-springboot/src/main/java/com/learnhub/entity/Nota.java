package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

/**
 * Nota obtenida por un alumno en una Materia.
 *
 * <p>Se valida en Service que valor esté entre 0 y 10 y que fecha no sea
 * futura. Es una regla de negocio del sistema educativo, no una decisión de
 * la vista.</p>
 */
@Entity
@Table(name = "notas")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Nota extends BaseEntity {

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Float valor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
}
