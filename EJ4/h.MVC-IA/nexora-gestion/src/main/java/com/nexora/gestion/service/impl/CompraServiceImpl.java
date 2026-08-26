package com.nexora.gestion.service.impl;

import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.model.Compra;
import com.nexora.gestion.model.DetalleCompra;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.model.TipoMovimiento;
import com.nexora.gestion.model.Usuario;
import com.nexora.gestion.repository.CompraRepository;
import com.nexora.gestion.repository.ProductoRepository;
import com.nexora.gestion.service.CompraService;
import com.nexora.gestion.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * IMPLEMENTACIÓN "CompraServiceImpl" (Capa de Servicio)
 * ============================================================================
 * Es el Service más "orquestador" del sistema: coordina Compra, Producto e
 * Inventario dentro de transacciones ACID. @Transactional garantiza que, por
 * ejemplo, si falla la validación de stock al agregar un detalle, NINGÚN
 * cambio parcial (ni el detalle, ni el recálculo del total, ni el movimiento
 * de inventario) queda persistido: todo o nada.
 *
 * Traduce los métodos del diagrama UML:
 *  - Compra.registrarCompra()      -> confirmarCompra(compraId)
 *  - Compra.agregarDetalle()       -> agregarDetalle(compraId, productoId, cantidad)
 *  - Compra.anularCompra()         -> anularCompra(compraId)
 *  - DetalleCompra.disminuirInventario() -> invocado internamente desde
 *        agregarDetalle(), delegando en InventarioService.registrarMovimiento(SALIDA)
 * ============================================================================
 */
@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final InventarioService inventarioService;

    public CompraServiceImpl(CompraRepository compraRepository,
                              ProductoRepository productoRepository,
                              InventarioService inventarioService) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.inventarioService = inventarioService;
    }

    @Override
    public Compra iniciarCompra(Usuario usuario) {
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setFechaCompra(LocalDateTime.now());
        compra.setPrecioTotal(BigDecimal.ZERO);
        compra.setAnulada(Boolean.FALSE);
        return compraRepository.save(compra);
    }

    @Override
    public Compra agregarDetalle(Long compraId, Long productoId, Integer cantidad) {
        Compra compra = obtenerCompraOFallar(compraId);

        if (Boolean.TRUE.equals(compra.getAnulada())) {
            throw new IllegalStateException("No se pueden agregar productos a una compra anulada.");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: id=" + productoId));

        // 1) disminuirInventario(): se valida y descuenta stock ANTES de
        //    confirmar el detalle, para no vender por encima del stock real.
        //    Si no hay stock suficiente, InventarioService lanza
        //    StockInsuficienteException y la transacción entera se revierte.
        inventarioService.registrarMovimiento(producto, cantidad, TipoMovimiento.SALIDA);

        // 2) Se arma el renglón (DetalleCompra) con el precio vigente del
        //    producto "congelado" en el momento de la compra.
        DetalleCompra detalle = new DetalleCompra();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        compra.addDetalleInterno(detalle);

        // 3) Se recalcula el total de la compra en base a TODOS sus detalles
        //    (nunca se confía en un total parcial acumulado a mano).
        recalcularTotal(compra);

        return compraRepository.save(compra);
    }

    @Override
    public Compra confirmarCompra(Long compraId) {
        Compra compra = obtenerCompraOFallar(compraId);
        if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            throw new IllegalStateException("No se puede registrar una compra sin productos.");
        }
        recalcularTotal(compra);
        return compraRepository.save(compra);
    }

    @Override
    public Compra anularCompra(Long compraId) {
        Compra compra = obtenerCompraOFallar(compraId);
        if (Boolean.TRUE.equals(compra.getAnulada())) {
            throw new IllegalStateException("La compra ya se encuentra anulada.");
        }

        // Regla de negocio: anular una compra repone el stock de cada
        // producto involucrado, generando movimientos de ENTRADA
        // compensatorios (se preserva el historial completo: nunca se
        // "borran" los movimientos de SALIDA originales).
        for (DetalleCompra detalle : compra.getDetalles()) {
            inventarioService.registrarMovimiento(detalle.getProducto(), detalle.getCantidad(), TipoMovimiento.ENTRADA);
        }

        compra.setAnulada(Boolean.TRUE);
        return compraRepository.save(compra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compra> historialDeUsuario(Usuario usuario) {
        return compraRepository.findByUsuarioWithDetalles(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compra> listarTodas() {
        return compraRepository.findAllByOrderByFechaCompraDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Compra> buscarPorId(Long id) {
        return compraRepository.findByIdWithDetalles(id);
    }

    /** Recalcula precioTotal como la suma de los subtotales de cada detalle. */
    private void recalcularTotal(Compra compra) {
        BigDecimal total = compra.getDetalles().stream()
                .map(DetalleCompra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        compra.setPrecioTotal(total);
    }

    private Compra obtenerCompraOFallar(Long compraId) {
        return compraRepository.findByIdWithDetalles(compraId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Compra no encontrada: id=" + compraId));
    }
}
