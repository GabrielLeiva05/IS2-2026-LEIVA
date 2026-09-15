package com.nexora.gestion.service;

import com.nexora.gestion.model.Inventario;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.model.TipoMovimiento;

import java.util.List;

/**
 * Contrato de reglas de negocio de Inventario: registrarMovimiento() del
 * diagrama UML, más el cálculo de stock actual (necesario para poder
 * decidir si hay stock suficiente al momento de una Compra).
 */
public interface InventarioService {

    /**
     * Regla de negocio central: registra un movimiento de stock.
     *  - cantidad debe ser > 0.
     *  - si tipoMovimiento es SALIDA, valida que el stock actual alcance
     *    (si no, lanza StockInsuficienteException).
     */
    Inventario registrarMovimiento(Producto producto, Integer cantidad, TipoMovimiento tipoMovimiento);

    /** Calcula el stock actual de un producto (entradas - salidas). */
    Integer calcularStockActual(Producto producto);

    List<Inventario> historialDeProducto(Producto producto);

    List<Inventario> listarTodos();
}
