package com.furnish.erp.service.impl;

import com.furnish.erp.domain.DetalleOrden;
import com.furnish.erp.domain.Producto;
import com.furnish.erp.domain.Stock;
import com.furnish.erp.domain.enums.TipoMovimiento;
import com.furnish.erp.exception.BusinessException;
import com.furnish.erp.exception.ResourceNotFoundException;
import com.furnish.erp.repository.ProductoRepository;
import com.furnish.erp.repository.StockRepository;
import com.furnish.erp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ============================================================================
 * Implementación: StockServiceImpl  (Kardex de inventario)
 * ============================================================================
 * Corresponde a los métodos incrementarStock(), decrementarStock() y
 * consultarStock() de la clase Stock del diagrama de clases.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Stock> historialDeProducto(Long idProducto) {
        return stockRepository.findByProducto_IdOrderByFechaDesc(idProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public int consultarStockActual(Long idProducto) {
        // El "stock actual" vigente es el stockActual del último movimiento
        // registrado para ese producto (patrón Kardex); si nunca tuvo
        // movimientos, se considera 0.
        return stockRepository.findUltimo(idProducto)
                .map(Stock::getStockActual)
                .orElse(0);
    }

    @Override
    public Stock incrementarStock(Long idProducto, int cantidad, DetalleOrden detalleOrdenOrNull) {
        if (cantidad <= 0) {
            throw new BusinessException("La cantidad a incrementar debe ser mayor a 0");
        }
        Producto producto = obtenerProducto(idProducto);
        int stockAnterior = consultarStockActual(idProducto);

        Stock movimiento = Stock.builder()
                .fecha(LocalDateTime.now())
                .tipoMovimiento(TipoMovimiento.ENTRADA)
                .cantidad(cantidad)
                .stockActual(stockAnterior + cantidad)
                .producto(producto)
                .detalleOrden(detalleOrdenOrNull)
                .build();

        return stockRepository.save(movimiento);
    }

    @Override
    public Stock decrementarStock(Long idProducto, int cantidad) {
        if (cantidad <= 0) {
            throw new BusinessException("La cantidad a decrementar debe ser mayor a 0");
        }
        Producto producto = obtenerProducto(idProducto);
        int stockAnterior = consultarStockActual(idProducto);

        // Regla de negocio: el stock nunca puede quedar en un valor negativo.
        if (stockAnterior - cantidad < 0) {
            throw new BusinessException(
                    "Stock insuficiente para '" + producto.getNombre() + "'. " +
                    "Stock actual: " + stockAnterior + ", se intentó descontar: " + cantidad);
        }

        Stock movimiento = Stock.builder()
                .fecha(LocalDateTime.now())
                .tipoMovimiento(TipoMovimiento.SALIDA)
                .cantidad(cantidad)
                .stockActual(stockAnterior - cantidad)
                .producto(producto)
                .detalleOrden(null)
                .build();

        return stockRepository.save(movimiento);
    }

    @Override
    public Stock registrarMovimientoManual(Long idProducto, TipoMovimiento tipo, int cantidad) {
        // Delega en incrementar/decrementar según corresponda, evitando
        // duplicar la validación de "stock no negativo".
        return switch (tipo) {
            case ENTRADA -> incrementarStock(idProducto, cantidad, null);
            case SALIDA -> decrementarStock(idProducto, cantidad);
        };
    }

    private Producto obtenerProducto(Long idProducto) {
        return productoRepository.findByIdAndEliminadoFalse(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el producto con id " + idProducto));
    }
}
