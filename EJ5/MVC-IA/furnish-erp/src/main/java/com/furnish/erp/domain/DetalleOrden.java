package com.furnish.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * Entidad JPA: DetalleOrden
 * ============================================================================
 * Línea de detalle de una OrdenCompra: qué Producto se pidió, en qué
 * cantidad y a qué precio unitario.
 *
 * SUPUESTO DE DISEÑO: el diagrama no dibuja explícitamente una línea entre
 * DetalleOrden y Producto, pero el atributo "cantidadProducto" sólo tiene
 * sentido si el detalle referencia a un Producto concreto. Se agrega la
 * relación @ManyToOne hacia Producto como una extensión necesaria y mínima
 * para que el modelo sea funcional (documentado también en el README).
 *
 * El método disminuirStock() del diagrama de clases NO se implementa acá
 * (esta clase es solo Modelo): la lógica real vive en
 * {@link com.furnish.erp.service.StockService}, que es invocado por
 * OrdenCompraService.cambiarEstado() cuando una orden pasa a estado RECIBIDA.
 * ============================================================================
 */
@Entity
@Table(name = "detalle_orden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"ordenCompra", "movimientosStock"})
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad_producto", nullable = false)
    private Integer cantidadProducto;

    @NotNull
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    /** Lado "muchos" de la composición con OrdenCompra. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_compra", nullable = false)
    private OrdenCompra ordenCompra;

    /** Producto asociado a esta línea de la orden (ver nota de diseño arriba). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /** Lado "1" de la relación DetalleOrden 1 -- 1...* Stock: cada línea de
     *  compra puede originar uno o varios movimientos de stock (por ejemplo,
     *  si la mercadería se recibe en más de una entrega parcial). Ver Stock. */
    @Builder.Default
    @OneToMany(mappedBy = "detalleOrden", cascade = CascadeType.PERSIST)
    private List<Stock> movimientosStock = new ArrayList<>();

    /** Subtotal de la línea (cantidad * precio unitario). Es un cálculo
     *  simple y determinístico, no una regla de negocio compleja, por lo que
     *  se acepta como método de conveniencia dentro de la entidad (no decide
     *  nada, solo deriva un valor de sus propios atributos). */
    @Transient
    public double getSubtotalLinea() {
        if (cantidadProducto == null || precioUnitario == null) return 0.0;
        return cantidadProducto * precioUnitario;
    }
}
