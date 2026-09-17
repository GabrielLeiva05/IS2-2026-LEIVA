package com.club.entity;

import com.club.enumeration.EstadoPago;
import com.club.enumeration.MedioPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Audited
@Table(name = "pagos", uniqueConstraints = @UniqueConstraint(name = "uk_pago_familia_periodo", columnNames = {"grupo_familiar_id", "periodo"}))
@Getter
@Setter
@NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MedioPago medioPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado;

    @Column(length = 120)
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_familiar_id", nullable = false)
    private GrupoFamiliar grupoFamiliar;

    public Pago(String periodo, BigDecimal importe, MedioPago medioPago, String referencia) {
        this.periodo = periodo;
        this.importe = importe;
        this.medioPago = medioPago;
        this.referencia = referencia;
    }
}
