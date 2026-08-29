package com.furnish.erp.domain;

import com.furnish.erp.domain.enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * Entidad JPA: Stock  (registro de movimientos de inventario / "Kardex")
 * ============================================================================
 * NOTA / INTERPRETACIÓN DEL DIAGRAMA: los atributos originales de esta clase
 * (idMovimiento, fecha, tipoMovimiento, stockActual) describen inequívocamente
 * un LIBRO DE MOVIMIENTOS de inventario (patrón "Kardex"), y no un único
 * registro 1 a 1 por Producto. Por eso, aunque el diagrama dibuja la relación
 * Producto-Stock con cardinalidad "1...1", se optó -de forma documentada y
 * deliberada- por modelarla como:
 *
 *    Producto (1) -----------------< (0..*) Stock         [obligatoria]
 *    DetalleOrden (1) --------------< (0..*) Stock         [opcional]
 *
 * Es decir: cada movimiento de Stock pertenece siempre a un Producto, y
 * opcionalmente puede trazar de qué DetalleOrden de compra se originó
 * (trazabilidad de "por qué entró esta mercadería"). Esta es la forma
 * estándar de la industria para modelar un Kardex de inventario y es la
 * única manera de que los métodos incrementarStock()/decrementarStock()/
 * consultarStock() (que dependen de guardar HISTORIAL) tengan sentido.
 *
 * El "stockActual" de cada fila representa la foto del stock del producto
 * INMEDIATAMENTE DESPUÉS de aplicado ese movimiento (columna típica de un
 * Kardex). El stock actual "vigente" de un producto se obtiene consultando
 * el movimiento más reciente (ver StockService.consultarStockActual()).
 * ============================================================================
 */
@Entity
@Table(name = "stock_movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"producto", "detalleOrden"})
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipoMovimiento;

    @NotNull
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    /** Cantidad involucrada en el movimiento (cuánto entró o salió). Se
     *  agrega como complemento necesario de stockActual para poder auditar
     *  el movimiento (el diagrama solo guardaba la foto final). */
    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** Producto obligatorio: todo movimiento de stock pertenece a un producto. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /** DetalleOrden opcional: sólo se completa cuando el movimiento de tipo
     *  ENTRADA fue generado automáticamente al recibir una OrdenCompra. Los
     *  movimientos manuales (ajustes, ventas, mermas) quedan con este campo
     *  en null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle", nullable = true)
    private DetalleOrden detalleOrden;
}
