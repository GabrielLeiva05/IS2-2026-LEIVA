package com.nexora.gestion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * ENTIDAD "Producto" (Modelo)
 * ============================================================================
 * Corresponde a la clase "Producto" del diagrama UML.
 *
 * NOTA DE DISEÑO: el diagrama UML no incluye un atributo de precio en
 * "Producto", pero "Compra" sí tiene "precioTotal", el cual se calcula a
 * partir de los "DetalleCompra" de cada producto. Por lo tanto, para que el
 * cálculo de precioTotal sea posible, se agrega aquí el atributo "precio"
 * como una extensión necesaria y mínima sobre el diagrama original (se
 * documenta explícitamente esta decisión de diseño).
 *
 * RELACIÓN UML: Producto "1" ◇----- "*...1" Inventario (agregación: un
 * Producto puede tener muchos registros/movimientos de Inventario a lo largo
 * del tiempo). Se mapea como @OneToMany desde Producto hacia Inventario.
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 * ============================================================================
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    /** Precio unitario de venta (ver nota de diseño de la clase). */
    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio = BigDecimal.ZERO;

    /** Indica si el producto está habilitado para la venta (borrado lógico,
     *  en lugar de eliminar físicamente productos con historial de ventas). */
    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    /**
     * Lado "1" de la relación Producto (1) ◇--- (*) Inventario.
     * El dueño de la relación (FK "producto_id") es Inventario.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Inventario> movimientosInventario = new ArrayList<>();

    public Producto() {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Inventario> getMovimientosInventario() {
        return movimientosInventario;
    }

    public void setMovimientosInventario(List<Inventario> movimientosInventario) {
        this.movimientosInventario = movimientosInventario;
    }

    // Métodos de negocio del diagrama (registrarProducto, editarProducto,
    // eliminarProducto) se implementan en ProductoServiceImpl: allí se valida
    // (nombre no vacío, precio > 0), se persiste vía ProductoRepository y se
    // decide si el borrado es físico o lógico según exista historial.
}
