package com.nexora.gestion.repository;

import com.nexora.gestion.model.Inventario;
import com.nexora.gestion.model.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================================
 * Repositorio JPA para la entidad Inventario (libro de movimientos de stock).
 * ============================================================================
 * Incluye una consulta JPQL personalizada con @Query para calcular el stock
 * actual de un producto como la suma algebraica de sus movimientos
 * (entradas positivas, salidas negativas), evitando así mantener un contador
 * desnormalizado que pueda desincronizarse con la realidad.
 * ============================================================================
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    /** Historial de movimientos de un producto, más recientes primero. */
    List<Inventario> findByProductoOrderByFechaDesc(Producto producto);

    /**
     * Lista todos los movimientos junto con su Producto para poder
     * renderizar la vista Thymeleaf cuando Open Session in View está
     * deshabilitado.
     */
    @EntityGraph(attributePaths = "producto")
    @Query("SELECT i FROM Inventario i ORDER BY i.fecha DESC")
    List<Inventario> findAllWithProducto();

    /**
     * Calcula el stock actual sumando entradas y restando salidas.
     * La expresión CASE WHEN traduce el enum TipoMovimiento a +cantidad o
     * -cantidad antes de sumar, todo resuelto en una sola consulta SQL.
     */
    @Query("""
           SELECT COALESCE(SUM(
                CASE WHEN i.tipoMovimiento = com.nexora.gestion.model.TipoMovimiento.ENTRADA
                     THEN i.cantidad
                     ELSE -i.cantidad
                END), 0)
           FROM Inventario i
           WHERE i.producto = :producto
           """)
    Integer calcularStockActual(@Param("producto") Producto producto);
}
