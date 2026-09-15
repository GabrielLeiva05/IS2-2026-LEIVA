package com.furnish.erp.service.impl;

import com.furnish.erp.domain.DetalleOrden;
import com.furnish.erp.domain.Empleado;
import com.furnish.erp.domain.OrdenCompra;
import com.furnish.erp.domain.Producto;
import com.furnish.erp.domain.Proveedor;
import com.furnish.erp.domain.enums.EstadoOrden;
import com.furnish.erp.dto.DetalleOrdenForm;
import com.furnish.erp.dto.OrdenCompraForm;
import com.furnish.erp.exception.BusinessException;
import com.furnish.erp.exception.ResourceNotFoundException;
import com.furnish.erp.repository.OrdenCompraRepository;
import com.furnish.erp.service.EmpleadoService;
import com.furnish.erp.service.OrdenCompraService;
import com.furnish.erp.service.ProductoService;
import com.furnish.erp.service.ProveedorService;
import com.furnish.erp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * ============================================================================
 * Implementación: OrdenCompraServiceImpl
 * ============================================================================
 * Es el Service que más reglas de negocio concentra, ya que orquesta a
 * Empleado, Proveedor, Producto y Stock para resolver el caso de uso
 * completo de "compra a un proveedor". Nótese que SOLO se inyectan otros
 * *Service* (nunca Repository de otra entidad), respetando la regla de que
 * las reglas de negocio están encapsuladas exclusivamente en esta capa.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final EmpleadoService empleadoService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final StockService stockService;

    /** Transiciones de estado válidas (máquina de estados simple). */
    private static final Set<EstadoOrden> DESDE_PENDIENTE = Set.of(EstadoOrden.APROBADA, EstadoOrden.CANCELADA);
    private static final Set<EstadoOrden> DESDE_APROBADA = Set.of(EstadoOrden.RECIBIDA, EstadoOrden.CANCELADA);

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> listarTodas() {
        return ordenCompraRepository.findAllByOrderByFechaEmisionDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> listarPorEstado(EstadoOrden estado) {
        return ordenCompraRepository.findByEstadoOrderByFechaEmisionDesc(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompra buscarPorId(Long id) {
        OrdenCompra orden = ordenCompraRepository.findConDetallesById(id);
        if (orden == null) {
            throw new ResourceNotFoundException("No se encontró la orden de compra con id " + id);
        }
        return orden;
    }

    @Override
    public OrdenCompra registrarOrden(OrdenCompraForm form) {
        if (form.getDetalles() == null || form.getDetalles().isEmpty()) {
            throw new BusinessException("La orden de compra debe tener al menos un producto");
        }

        Empleado empleado = empleadoService.buscarPorId(form.getIdEmpleado());
        Proveedor proveedor = proveedorService.buscarPorId(form.getIdProveedor());

        OrdenCompra orden = OrdenCompra.builder()
                .fechaEmision(LocalDate.now())
                .estado(EstadoOrden.PENDIENTE)
                .empleado(empleado)
                .proveedor(proveedor)
                .subtotal(0.0)
                .precioTotal(0.0)
                .build();

        // "agregarDetalle()" del diagrama: se agrega cada línea recibida del
        // formulario, validando que el producto exista y esté activo.
        for (DetalleOrdenForm df : form.getDetalles()) {
            Producto producto = productoService.buscarPorId(df.getIdProducto());
            DetalleOrden detalle = DetalleOrden.builder()
                    .producto(producto)
                    .cantidadProducto(df.getCantidadProducto())
                    .precioUnitario(df.getPrecioUnitario())
                    .build();
            orden.agregarDetalleEnMemoria(detalle);
        }

        aplicarCalculoTotal(orden);
        return ordenCompraRepository.save(orden);
    }

    @Override
    public OrdenCompra calcularTotal(Long idOrdenCompra) {
        OrdenCompra orden = buscarPorId(idOrdenCompra);
        aplicarCalculoTotal(orden);
        return ordenCompraRepository.save(orden);
    }

    /** calcularTotal(): suma cada línea (cantidad * precioUnitario). En este
     *  modelo subtotal y precioTotal coinciden porque el diagrama no define
     *  impuestos/descuentos; se mantienen como dos campos separados por
     *  fidelidad al diagrama original y para permitir agregar impuestos o
     *  descuentos a futuro sin cambiar el modelo. */
    private void aplicarCalculoTotal(OrdenCompra orden) {
        double subtotal = orden.getDetalles().stream()
                .mapToDouble(DetalleOrden::getSubtotalLinea)
                .sum();
        orden.setSubtotal(subtotal);
        orden.setPrecioTotal(subtotal);
    }

    @Override
    public OrdenCompra cambiarEstado(Long idOrdenCompra, EstadoOrden nuevoEstado) {
        OrdenCompra orden = buscarPorId(idOrdenCompra);
        EstadoOrden actual = orden.getEstado();

        boolean transicionValida =
                (actual == EstadoOrden.PENDIENTE && DESDE_PENDIENTE.contains(nuevoEstado)) ||
                (actual == EstadoOrden.APROBADA && DESDE_APROBADA.contains(nuevoEstado));

        if (!transicionValida) {
            throw new BusinessException(
                    "No se puede cambiar la orden de estado " + actual + " a " + nuevoEstado);
        }

        // Efecto de negocio al RECIBIR la orden: por cada línea de detalle se
        // incrementa el stock del producto correspondiente, trazando el
        // movimiento hacia el DetalleOrden que lo originó (Stock.detalleOrden).
        // Esto es la interpretación funcional de "disminuirStock()" del
        // diagrama: el detalle de la orden es quien "impacta" en el stock al
        // efectivizarse la recepción de mercadería.
        if (nuevoEstado == EstadoOrden.RECIBIDA) {
            for (DetalleOrden detalle : orden.getDetalles()) {
                stockService.incrementarStock(
                        detalle.getProducto().getId(),
                        detalle.getCantidadProducto(),
                        detalle);
            }
        }

        orden.setEstado(nuevoEstado);
        return ordenCompraRepository.save(orden);
    }
}
