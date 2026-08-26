package com.nexora.gestion.exception;

/**
 * Excepción de negocio lanzada cuando se intenta vender/descontar más
 * unidades de un Producto de las que hay disponibles en Inventario.
 * Decidida y lanzada desde InventarioServiceImpl / CompraServiceImpl.
 */
public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
