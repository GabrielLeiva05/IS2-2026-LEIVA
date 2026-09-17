package com.club.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Table(name = "grupos_familiares")
@Getter
@Setter
@NoArgsConstructor
public class GrupoFamiliar {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "cuota_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal cuotaMensual;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "titular_id", nullable = false, unique = true)
    private Socio titular;

    @OneToMany(mappedBy = "grupoFamiliar", fetch = FetchType.LAZY)
    private List<Persona> familiares = new ArrayList<>();

    @OneToMany(mappedBy = "grupoFamiliar", fetch = FetchType.LAZY)
    private List<Pago> pagos = new ArrayList<>();
}
