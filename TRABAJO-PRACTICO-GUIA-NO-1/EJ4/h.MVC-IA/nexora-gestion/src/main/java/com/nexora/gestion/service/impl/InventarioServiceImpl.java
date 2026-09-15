package com.nexora.gestion.service.impl;

import com.nexora.gestion.exception.StockInsuficienteException;
import com.nexora.gestion.model.Inventario;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.model.TipoMovimiento;
import com.nexora.gestion.repository.InventarioRepository;
import com.nexora.gestion.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ============================================================================
 * IMPLEMENTACIÓN "InventarioServiceImpl" (Capa de Servicio)
 * ============================================================================
 * Regla de negocio central "registrarMovimiento()" del diagrama UML: valida
 * la cantidad y, para movimientos de SALIDA, que exista stock suficiente,
 * antes de persistir el movimiento. Esta clase es utilizada tanto por el
 * flujo de "reposición de stock" del Administrador (ENTRADA) como por
 * CompraServiceImpl al concretar una venta (SALIDA), garantizando que la
 * regla se aplique siempre desde un único lugar (no duplicada en cada
 * Controller).
 * ============================================================================
 */
@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public Inventario registrarMovimiento(Producto producto, Integer cantidad, TipoMovimiento tipoMovimiento) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor a cero.");
        }

        if (tipoMovimiento == TipoMovimiento.SALIDA) {
            Integer stockActual = calcularStockActual(producto);
            if (stockActual < cantidad) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para el producto '" + producto.getNombre() + "'. "
                                + "Disponible: " + stockActual + ", solicitado: " + cantidad + ".");
            }
        }

        Inventario movimiento = new Inventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(cantidad);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setFecha(LocalDateTime.now());
        return inventarioRepository.save(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calcularStockActual(Producto producto) {
        return inventarioRepository.calcularStockActual(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> historialDeProducto(Producto producto) {
        return inventarioRepository.findByProductoOrderByFechaDesc(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> listarTodos() {
        return inventarioRepository.findAllWithProducto();
    }
}
