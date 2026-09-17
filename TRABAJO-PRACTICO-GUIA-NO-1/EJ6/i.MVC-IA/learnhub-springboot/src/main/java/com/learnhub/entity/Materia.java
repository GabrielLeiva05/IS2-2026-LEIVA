package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

/**
 * Materia del diagrama. Una materia es dictada por exactamente un Profesor y
 * queda asociada al Alumno indicado por el modelo recibido.
 */
@Entity
@Table(name = "materias")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Materia extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false)
    private boolean eliminado = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @OneToMany(mappedBy = "materia", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();

    /** Método equivalente a registrarMateria del diagrama. */
    public void registrarMateria() {
        this.eliminado = false;
    }

    /** Método equivalente a eliminarMateria del diagrama. */
    public void eliminarMateria() {
        this.eliminado = true;
    }
}
