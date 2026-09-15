package com.furnish.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA: Proveedor (Modelo puro, sin lógica de negocio -> ver
 * {@link com.furnish.erp.service.ProveedorService}).
 *
 * Relación 1...* con OrdenCompra: un Proveedor puede tener muchas órdenes
 * de compra realizadas hacia él (mappedBy = "proveedor").
 */
@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "ordenesCompra")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;

    /** NOTA / SUPUESTO DE DISEÑO: el diagrama original no incluye un campo de
     *  nombre para Proveedor, pero un proveedor sin razón social no es
     *  operable en un sistema real (no se podría identificar en listados,
     *  combos de selección, órdenes de compra, etc.). Se agrega este atributo
     *  como una extensión mínima y justificada del modelo original. Todas las
     *  extensiones de este tipo están documentadas en el README.md. */
    @NotBlank(message = "El nombre/razón social es obligatorio")
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @NotNull(message = "El teléfono es obligatorio")
    @Column(name = "telefono", nullable = false)
    private Integer telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    /** Baja lógica (llamado "eliminador" en el diagrama original; se corrige
     *  el nombre a "eliminado" por consistencia con el resto del modelo). */
    @Builder.Default
    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;

    @Builder.Default
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.PERSIST)
    private List<OrdenCompra> ordenesCompra = new ArrayList<>();
}
