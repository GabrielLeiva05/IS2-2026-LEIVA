package com.nexora.gestion.service;

import com.nexora.gestion.model.Compra;
import com.nexora.gestion.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de reglas de negocio de Compra: registrarCompra(),
 * agregarDetalle() y anularCompra() del diagrama UML. Esta es la interfaz
 * que más orquesta a otros servicios (Producto e Inventario), porque una
 * Compra afecta transaccionalmente varias entidades a la vez.
 */
public interface CompraService {

    /** Crea una Compra nueva "en borrador" (sin detalles todavía) asociada
     *  a un Usuario — equivale al "carrito" antes de confirmarse. */
    Compra iniciarCompra(Usuario usuario);

    /**
     * Regla de negocio "agregarDetalle()" + "disminuirInventario()":
     * agrega un renglón de Producto/cantidad a la Compra, valida stock
     * disponible, calcula el subtotal con el precio vigente del producto,
     * recalcula el precioTotal de la Compra y descuenta el stock (a través
     * de InventarioService), todo dentro de una única transacción.
     */
    Compra agregarDetalle(Long compraId, Long productoId, Integer cantidad);

    /** Regla de negocio "registrarCompra()": confirma/cierra la compra
     *  (valida que tenga al menos un detalle). */
    Compra confirmarCompra(Long compraId);

    /** Regla de negocio "anularCompra()": marca la compra como anulada y
     *  repone el stock de cada producto involucrado (movimientos de
     *  ENTRADA compensatorios), preservando la trazabilidad histórica. */
    Compra anularCompra(Long compraId);

    List<Compra> historialDeUsuario(Usuario usuario);

    List<Compra> listarTodas();

    Optional<Compra> buscarPorId(Long id);
}
