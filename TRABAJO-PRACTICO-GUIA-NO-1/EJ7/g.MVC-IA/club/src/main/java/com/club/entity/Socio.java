package com.club.entity;

import com.club.enumeration.EstadoSocio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Audited
@Table(name = "socios")
@Getter
@Setter
@NoArgsConstructor
public class Socio extends Persona {
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSocio estado = EstadoSocio.ACTIVO;

    public Socio(String nombre, String apellido, String email, String telefono) {
        super(nombre, apellido, email, telefono);
        this.fechaAlta = LocalDate.now();
        this.estado = EstadoSocio.ACTIVO;
    }
}
