package com.furnish.erp.domain.enums;

/**
 * Tipo de movimiento de inventario registrado en {@link com.furnish.erp.domain.Stock}.
 * Se corresponde con los métodos incrementarStock()/decrementarStock() del
 * diagrama de clases (StockService es quien decide qué tipo aplicar).
 *
 *  ENTRADA -> incrementa el stock (ej: recepción de una OrdenCompra a un Proveedor).
 *  SALIDA  -> disminuye el stock (ej: venta, rotura, ajuste negativo, merma).
 */
public enum TipoMovimiento {
    ENTRADA,
    SALIDA
}
