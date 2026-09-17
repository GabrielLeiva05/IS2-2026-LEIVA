package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

/**
 * Grado académico. Tiene composición con Aula: el diagrama indica que las
 * aulas pertenecen al grado y que su ciclo de vida depende de él.
 */
@Entity
@Table(name = "grados")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Grado extends BaseEntity {

    @Column(name = "nivel", nullable = false, unique = true, length = 50)
    private String nivel;

    @OneToMany(mappedBy = "grado", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Aula> aulas = new ArrayList<>();

    /** Agrega un aula manteniendo ambos lados de la relación sincronizados. */
    public void agregarAula(Aula aula) {
        if (aula == null) {
            throw new IllegalArgumentException("El aula no puede ser null.");
        }
        if (!aulas.contains(aula)) {
            aulas.add(aula);
        }
        aula.setGrado(this);
    }
}
