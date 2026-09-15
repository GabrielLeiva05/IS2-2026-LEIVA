package com.nexora.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * ENTIDAD "Inventario" (Modelo)
 * ============================================================================
 * Corresponde a la clase "Inventario" del diagrama UML. Se modela como un
 * LIBRO MAYOR (ledger) de movimientos de stock: cada fila es un movimiento
 * puntual (ENTRADA o SALIDA) de una cantidad de un Producto en una fecha
 * determinada. El stock actual de un Producto se calcula (en
 * InventarioServiceImpl) como la suma algebraica de sus movimientos
 * (entradas - salidas), en lugar de guardarse como un contador desnormalizado
 * propenso a desincronizarse.
 *
 * RELACIÓN UML: DetalleCompra "*...1" ---> Inventario. Aquí se interpreta
 * como: cada operación de "disminuirInventario()" de un DetalleCompra genera
 * (a través del Service) un nuevo registro de Inventario de tipo SALIDA
 * asociado al Producto vendido. Ver la nota de diseño en {@link TipoMovimiento}.
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 * ============================================================================
 */
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Fecha y hora en que se registró el movimiento. */
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    /** Cantidad de unidades que involucra el movimiento (siempre positiva;
     *  el signo/efecto sobre el stock lo determina "tipoMovimiento"). */
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** Tipo de movimiento: ENTRADA (suma stock) o SALIDA (resta stock). */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private TipoMovimiento tipoMovimiento;

    /** Lado "muchos" de la relación Producto (1) ◇--- (*) Inventario.
     *  Esta entidad es la "dueña" de la relación (contiene la FK). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Inventario() {
        // Constructor vacío requerido por JPA/Hibernate.
    }

    // ==========================================================================
    // Getters y Setters explícitos
    // ==========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // El método "registrarMovimiento()" del diagrama UML se implementa como
    // regla de negocio en InventarioServiceImpl.registrarMovimiento(...),
    // que es quien decide validaciones (cantidad > 0, stock suficiente para
    // una SALIDA, etc.) antes de instanciar y persistir un objeto Inventario.
}
