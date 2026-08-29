package com.furnish.erp.service.impl;

import com.furnish.erp.domain.Producto;
import com.furnish.erp.exception.BusinessException;
import com.furnish.erp.exception.ResourceNotFoundException;
import com.furnish.erp.repository.ProductoRepository;
import com.furnish.erp.service.ProductoService;
import com.furnish.erp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    // Se inyecta StockService (y no StockRepository) porque, según la regla
    // de arquitectura del proyecto, un Service solo debe hablar con otros
    // Services o con SU PROPIO Repository; nunca con el Repository de otra
    // entidad directamente.
    private final StockService stockService;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByEliminadoFalseOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String texto) {
        return productoRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el producto con id " + id));
    }

    @Override
    public Producto cargarProducto(Producto producto) {
        producto.setEliminado(false);
        return productoRepository.save(producto);
    }

    @Override
    public Producto editarProducto(Long id, Producto datosActualizados) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        existente.setMarca(datosActualizados.getMarca());
        existente.setCategoria(datosActualizados.getCategoria());
        return productoRepository.save(existente);
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        // Regla de negocio: no se puede dar de baja un producto que todavía
        // tiene unidades en stock, para no perder trazabilidad de inventario
        // existente sin antes regularizarlo (venderlo, transferirlo, etc.).
        int stockActual = stockService.consultarStockActual(producto.getId());
        if (stockActual > 0) {
            throw new BusinessException(
                    "No se puede eliminar el producto '" + producto.getNombre() +
                    "' porque todavía tiene " + stockActual + " unidades en stock.");
        }
        producto.setEliminado(true);
        productoRepository.save(producto);
    }
}
