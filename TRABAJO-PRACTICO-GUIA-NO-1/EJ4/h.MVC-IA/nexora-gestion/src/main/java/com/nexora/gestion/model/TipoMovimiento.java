package com.nexora.gestion.model;

/**
 * ============================================================================
 * ENUM "TipoMovimiento" (Modelo)
 * ============================================================================
 * NOTA DE DISEÑO (extensión sobre el diagrama UML original):
 * El diagrama UML define la clase "Inventario" con el método
 * "registrarMovimiento()" pero no detalla si un movimiento es de entrada
 * (ingreso de stock, ej. una compra a un proveedor) o de salida (egreso de
 * stock, ej. una venta a un cliente). Para poder implementar correctamente
 * el método "disminuirInventario()" de "DetalleCompra" (que debe DISMINUIR
 * stock) y a la vez soportar cargas de stock por parte del Administrador,
 * se incorpora este enum como detalle de implementación, documentado aquí
 * de forma explícita.
 * ============================================================================
 */
public enum TipoMovimiento {
    /** Ingreso de stock (ej.: reposición realizada por el administrador). */
    ENTRADA,
    /** Egreso de stock (ej.: consumido por una compra de un usuario). */
    SALIDA
}
