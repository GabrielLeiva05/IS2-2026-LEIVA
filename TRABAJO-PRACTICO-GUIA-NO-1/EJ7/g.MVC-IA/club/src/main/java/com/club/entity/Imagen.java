package com.club.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "imagenes")
@Getter
@Setter
@NoArgsConstructor
public class Imagen {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String mime;

    @Lob
    @Column(nullable = false)
    private byte[] contenido;

    @Column(nullable = false)
    private boolean eliminado = false;

    public Imagen(String nombre, String mime, byte[] contenido) {
        this.nombre = nombre;
        this.mime = mime;
        this.contenido = contenido;
    }
}
