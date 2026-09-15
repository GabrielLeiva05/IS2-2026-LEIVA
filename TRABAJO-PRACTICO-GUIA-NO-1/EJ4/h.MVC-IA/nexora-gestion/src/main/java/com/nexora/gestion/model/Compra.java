package com.nexora.gestion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * ENTIDAD "Compra" (Modelo)
 * ============================================================================
 * Corresponde a la clase "Compra" del diagrama UML.
 *
 * RELACIONES UML:
 *  - Usuario "1" --- "1...*" Compra: lado "muchos" acá (@ManyToOne hacia
 *    Usuario, con la FK "usuario_id" en la tabla "compras").
 *  - Compra "1" ◆--- "1...*" DetalleCompra: COMPOSICIÓN (rombo relleno en el
 *    diagrama, del lado de Compra) hacia DetalleCompra. Composición implica
 *    que el ciclo de vida de los DetalleCompra está totalmente atado al de
 *    su Compra dueña: si se elimina/anula la Compra, sus detalles no pueden
 *    existir de forma independiente. Se mapea con cascade=ALL +
 *    orphanRemoval=true.
 *
 * Los métodos "registrarCompra()", "agregarDetalle()" y "anularCompra()" del
 * diagrama UML son REGLAS DE NEGOCIO (calculan totales, validan stock,
 * revierten movimientos de inventario) y por eso NO están implementados
 * acá sino en {@code CompraServiceImpl}, que es quien orquesta esta entidad
 * junto con ProductoService/InventarioService dentro de una transacción.
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 * ============================================================================
 */
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Fecha y hora en que se concretó la compra. */
    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra = LocalDateTime.now();

    /** Monto total de la compra = suma de subtotales de sus DetalleCompra.
     *  Se recalcula siempre desde el Service, nunca se setea "a mano" desde
     *  la vista/controlador. */
    @Column(name = "precio_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioTotal = BigDecimal.ZERO;

    /** Bandera de anulación (borrado lógico de la compra). */
    @Column(name = "anulada", nullable = false)
    private Boolean anulada = Boolean.FALSE;

    /** Lado "muchos" de Usuario (1) --- (1..*) Compra. Dueño de la FK. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Lado "1" de la composición Compra (1) ◆--- (1..*) DetalleCompra. */
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DetalleCompra> detalles = new ArrayList<>();

    public Compra() {
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

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }

    /**
     * Método auxiliar de conveniencia (NO es una regla de negocio: solo
     * mantiene la coherencia bidireccional de la relación en memoria antes
     * de persistir). La regla de negocio real de "agregarDetalle" —validar
     * stock, calcular subtotal, actualizar precioTotal, generar el
     * movimiento de inventario— vive en CompraServiceImpl.agregarDetalle(...).
     */
    public void addDetalleInterno(DetalleCompra detalle) {
        detalle.setCompra(this);
        this.detalles.add(detalle);
    }
}
