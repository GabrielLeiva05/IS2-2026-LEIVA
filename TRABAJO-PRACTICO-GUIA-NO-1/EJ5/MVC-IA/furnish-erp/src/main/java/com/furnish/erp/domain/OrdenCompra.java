package com.furnish.erp.domain;

import com.furnish.erp.domain.enums.EstadoOrden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * Entidad JPA: OrdenCompra
 * ============================================================================
 * Es la clase central del diagrama: agrupa a un Empleado (quien la registra),
 * un Proveedor (a quien se le compra) y una lista de DetalleOrden (líneas de
 * productos comprados), calculando subtotal/precioTotal.
 *
 * Relaciones:
 *  - @ManyToOne hacia Empleado  (1 Empleado -- * OrdenCompra)
 *  - @ManyToOne hacia Proveedor (1 Proveedor -- * OrdenCompra)
 *  - @OneToMany hacia DetalleOrden -> relación de COMPOSICIÓN (rombo relleno
 *      en el diagrama): el ciclo de vida de los DetalleOrden depende
 *      completamente de su OrdenCompra (si se elimina la orden, se eliminan
 *      sus detalles -> cascade = ALL + orphanRemoval = true).
 * ============================================================================
 */
@Entity
@Table(name = "ordenes_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"detalles"})
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_compra")
    private Long idOrdenCompra;

    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    /** Se modela como enum (ver EstadoOrden) para que solo el Service pueda
     *  aplicar transiciones válidas de estado (cambiarEstado()). */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoOrden estado;

    @Builder.Default
    @Column(name = "subtotal", nullable = false)
    private Double subtotal = 0.0;

    @Builder.Default
    @Column(name = "precio_total", nullable = false)
    private Double precioTotal = 0.0;

    /** FK hacia Empleado. LAZY: solo se consulta el Empleado cuando se
     *  necesita explícitamente (buena práctica de performance con ORM). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    /** FK hacia Proveedor. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    /** Composición OrdenCompra "1" --(rombo relleno)--> "1...*" DetalleOrden.
     *  cascade=ALL + orphanRemoval=true modelan fielmente la composición UML:
     *  un DetalleOrden no puede existir sin su OrdenCompra. */
    @Builder.Default
    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();

    /** Método de conveniencia (NO es lógica de negocio, solo mantiene
     *  consistente la relación bidireccional en memoria antes de persistir;
     *  el cálculo de totales real lo hace OrdenCompraService.calcularTotal()). */
    public void agregarDetalleEnMemoria(DetalleOrden detalle) {
        detalles.add(detalle);
        detalle.setOrdenCompra(this);
    }
}
