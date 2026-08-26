package com.nexora.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * ============================================================================
 * ENTIDAD "DetalleCompra" (Modelo)
 * ============================================================================
 * Corresponde a la clase "DetalleCompra" del diagrama UML: el "renglón" de
 * una Compra, que indica qué Producto se llevó y en qué cantidad.
 *
 * RELACIONES UML:
 *  - Compra "1" ◆--- "1...*" DetalleCompra (composición; lado "muchos" acá,
 *    FK "compra_id", dueño de esa relación).
 *  - DetalleCompra "*...1" ---> Inventario/Producto: se modela como
 *    @ManyToOne hacia Producto (ver nota de diseño en la clase Inventario)
 *    porque lo que el detalle afecta es el STOCK de un Producto concreto.
 *
 * El método "disminuirInventario()" del diagrama UML es una regla de negocio:
 * NO se implementa acá. Es {@code CompraServiceImpl} quien, al agregar este
 * detalle a una compra, invoca a {@code InventarioService.registrarMovimiento(
 * producto, cantidad, TipoMovimiento.SALIDA)} dentro de la misma transacción,
 * garantizando atomicidad entre "se vendió" y "se descontó del stock".
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 * ============================================================================
 */
@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Cantidad de unidades del producto llevadas en este renglón. */
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** Precio unitario "congelado" al momento de la compra (aunque el
     *  precio del Producto cambie después, el historial no se altera). */
    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    /** Subtotal = cantidad * precioUnitario (calculado por el Service). */
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    /** Lado "muchos" de la composición con Compra. Dueño de la FK "compra_id". */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    /** Producto afectado por este detalle (y, por extensión, su inventario). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public DetalleCompra() {
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
