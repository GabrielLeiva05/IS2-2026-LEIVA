package com.furnish.erp.service;

import com.furnish.erp.domain.OrdenCompra;
import com.furnish.erp.domain.enums.EstadoOrden;
import com.furnish.erp.dto.OrdenCompraForm;

import java.util.List;

/**
 * Interfaz de servicio: OrdenCompraService.
 * Implementa registrarOrden(), agregarDetalle() (integrado en registrarOrden
 * ya que el formulario carga la orden completa con sus líneas de una sola
 * vez), calcularTotal() y cambiarEstado() del diagrama de clases.
 */
public interface OrdenCompraService {

    List<OrdenCompra> listarTodas();

    List<OrdenCompra> listarPorEstado(EstadoOrden estado);

    OrdenCompra buscarPorId(Long id);

    /** Registra una nueva orden de compra con sus líneas de detalle,
     *  calculando subtotal y precioTotal (calcularTotal) y dejándola en
     *  estado inicial PENDIENTE. */
    OrdenCompra registrarOrden(OrdenCompraForm form);

    /** Recalcula subtotal/precioTotal en base a los detalles actuales de la
     *  orden (se invoca automáticamente tras registrar o modificar detalles). */
    OrdenCompra calcularTotal(Long idOrdenCompra);

    /**
     * Regla de negocio más importante del módulo de compras: aplica una
     * transición de estado válida sobre la orden.
     *   PENDIENTE  -> APROBADA
     *   APROBADA   -> RECIBIDA   (dispara StockService.incrementarStock()
     *                              por cada línea de detalle -> "disminuirStock()"
     *                              del diagrama se interpreta como el efecto
     *                              de aplicar el movimiento de inventario que
     *                              produce recibir la orden)
     *   PENDIENTE/APROBADA -> CANCELADA
     * Cualquier otra transición lanza BusinessException.
     */
    OrdenCompra cambiarEstado(Long idOrdenCompra, EstadoOrden nuevoEstado);
}
