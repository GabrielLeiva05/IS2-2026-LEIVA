package com.furnish.erp.service;

import com.furnish.erp.domain.DetalleOrden;
import com.furnish.erp.domain.Stock;
import com.furnish.erp.domain.enums.TipoMovimiento;

import java.util.List;

/**
 * Interfaz de servicio: StockService.
 * Implementa el "Kardex" de inventario descripto en la clase Stock:
 * incrementarStock(), decrementarStock(), consultarStock().
 */
public interface StockService {

    /** Historial completo de movimientos de un producto (más nuevo primero). */
    List<Stock> historialDeProducto(Long idProducto);

    /** Regla de negocio central: consulta el stock ACTUAL de un producto
     *  (0 si nunca tuvo movimientos). */
    int consultarStockActual(Long idProducto);

    /** Genera un movimiento de tipo ENTRADA (incrementarStock del diagrama).
     *  detalleOrden es opcional: se completa cuando el alta de stock proviene
     *  de la recepción automática de una OrdenCompra. */
    Stock incrementarStock(Long idProducto, int cantidad, DetalleOrden detalleOrdenOrNull);

    /** Genera un movimiento de tipo SALIDA (decrementarStock del diagrama).
     *  Regla de negocio: no permite que el stock quede negativo. */
    Stock decrementarStock(Long idProducto, int cantidad);

    /** Punto de entrada genérico usado por ajustes manuales desde la vista. */
    Stock registrarMovimientoManual(Long idProducto, TipoMovimiento tipo, int cantidad);
}
