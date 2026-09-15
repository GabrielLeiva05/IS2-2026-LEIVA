package com.nexora.gestion.service.impl;

import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.repository.ProductoRepository;
import com.nexora.gestion.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * IMPLEMENTACIÓN "ProductoServiceImpl" (Capa de Servicio)
 * ============================================================================
 * Reglas de negocio de Producto: registrarProducto(), editarProducto(),
 * eliminarProducto() del diagrama UML.
 * ============================================================================
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto registrarProducto(Producto producto) {
        validar(producto);
        producto.setActivo(Boolean.TRUE);
        return productoRepository.save(producto);
    }

    @Override
    public Producto editarProducto(Long id, Producto datosNuevos) {
        Producto existente = obtenerOFallar(id);
        validar(datosNuevos);
        existente.setNombre(datosNuevos.getNombre());
        existente.setDescripcion(datosNuevos.getDescripcion());
        existente.setPrecio(datosNuevos.getPrecio());
        return productoRepository.save(existente);
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = obtenerOFallar(id);
        // Regla de negocio: si el producto tiene movimientos de inventario
        // (historial de stock/ventas), no se borra físicamente -se rompería
        // la integridad referencial e histórica-, sino que se da de baja
        // lógica (activo = false), dejando de listarse en el catálogo.
        if (producto.getMovimientosInventario() != null && !producto.getMovimientosInventario().isEmpty()) {
            producto.setActivo(Boolean.FALSE);
            productoRepository.save(producto);
        } else {
            productoRepository.delete(producto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /** Validación de reglas de negocio antes de persistir un Producto. */
    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a cero.");
        }
    }

    private Producto obtenerOFallar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: id=" + id));
    }
}
