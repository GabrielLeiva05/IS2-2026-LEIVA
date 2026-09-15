package com.furnish.erp.domain.enums;

/**
 * Estados posibles del ciclo de vida de una {@link com.furnish.erp.domain.OrdenCompra}.
 * Se modela como enum (en vez de String libre) para que las transiciones de
 * estado sean controladas exclusivamente por la capa de negocio
 * (OrdenCompraService.cambiarEstado()), evitando valores inválidos en la BD.
 *
 *  PENDIENTE   -> la orden fue registrada pero todavía no fue enviada al proveedor.
 *  APROBADA    -> la orden fue confirmada/enviada al proveedor.
 *  RECIBIDA    -> la mercadería fue recibida; dispara el incremento de Stock.
 *  CANCELADA   -> la orden fue anulada y no genera movimientos de stock.
 */
public enum EstadoOrden {
    PENDIENTE,
    APROBADA,
    RECIBIDA,
    CANCELADA
}
