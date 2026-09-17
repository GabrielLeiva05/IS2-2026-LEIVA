package com.learnhub.entity;

import com.learnhub.enumeration.Sexo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa al Profesor del diagrama de clases.
 *
 * <p>La consigna utiliza también el término "docente". En el modelo se
 * conserva Profesor porque es el nombre exacto de la clase del diagrama.</p>
 *
 * <p>eliminado implementa baja lógica: no se destruyen los datos históricos
 * del profesor, algo especialmente importante porque sus Materias pueden
 * tener Notas asociadas.</p>
 */
@Entity
@Table(name = "profesores")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class Profesor extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Column(nullable = false, length = 120)
    private String especialidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Sexo sexo;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private boolean eliminado = false;

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "profesor", fetch = FetchType.LAZY)
    private List<Materia> materias = new ArrayList<>();

    /** Método de dominio equivalente a registrarProfesor del diagrama. */
    public void registrarProfesor() {
        this.eliminado = false;
    }

    /** Método de dominio equivalente a eliminarProfesor del diagrama. */
    public void eliminarProfesor() {
        this.eliminado = true;
    }
}
