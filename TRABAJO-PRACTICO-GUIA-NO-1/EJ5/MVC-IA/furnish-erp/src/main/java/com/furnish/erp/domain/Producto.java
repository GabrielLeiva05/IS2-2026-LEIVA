package com.furnish.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidad JPA: Producto (Modelo puro).
 * Métodos de negocio del diagrama (cargarProducto, editarProducto,
 * eliminarProducto) -> implementados en {@link com.furnish.erp.service.ProductoService}.
 *
 * Relación con Stock: ver documentación en {@link Stock} sobre la
 * interpretación adoptada para la relación Producto-Stock del diagrama
 * (patrón de Kardex / historial de movimientos de inventario).
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Size(max = 1000)
    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @NotBlank(message = "La marca es obligatoria")
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Builder.Default
    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;
}
