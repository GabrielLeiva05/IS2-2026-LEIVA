package com.furnish.erp.repository;

import com.furnish.erp.domain.OrdenCompra;
import com.furnish.erp.domain.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    /** 
     * Trae también Empleado y Proveedor porque son relaciones LAZY y las
     * vistas las necesitan después de que termina la transacción del Service.
     * Esto evita LazyInitializationException al volver a la orden después de
     * aprobar/recibir/cancelar.
     */
    @Query("SELECT o FROM OrdenCompra o " +
           "JOIN FETCH o.empleado " +
           "JOIN FETCH o.proveedor " +
           "ORDER BY o.fechaEmision DESC")
    List<OrdenCompra> findAllByOrderByFechaEmisionDesc();

    @Query("SELECT o FROM OrdenCompra o " +
           "JOIN FETCH o.empleado " +
           "JOIN FETCH o.proveedor " +
           "WHERE o.estado = :estado " +
           "ORDER BY o.fechaEmision DESC")
    List<OrdenCompra> findByEstadoOrderByFechaEmisionDesc(EstadoOrden estado);

    /** Trae la orden junto con todas las relaciones que usan las vistas:
     *  Empleado, Proveedor, Detalles y Producto.
     *  Al estar open-in-view=false, todo lo necesario debe quedar cargado
     *  dentro de la transacción del Service.
     */
    @Query("SELECT DISTINCT o FROM OrdenCompra o " +
           "JOIN FETCH o.empleado " +
           "JOIN FETCH o.proveedor " +
           "LEFT JOIN FETCH o.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE o.idOrdenCompra = :id")
    OrdenCompra findConDetallesById(Long id);
}
