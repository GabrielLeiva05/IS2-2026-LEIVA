package com.furnish.erp.repository;

import com.furnish.erp.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /** Historial de movimientos de un producto, del más reciente al más viejo. */
    List<Stock> findByProducto_IdOrderByFechaDesc(Long idProducto);

    /** Último movimiento registrado para un producto: representa el stock
     *  "actual" vigente (ver StockService.consultarStockActual()). */
    @Query("SELECT s FROM Stock s WHERE s.producto.id = :idProducto " +
           "ORDER BY s.fecha DESC, s.idMovimiento DESC")
    List<Stock> findUltimoMovimiento(@Param("idProducto") Long idProducto);

    default Optional<Stock> findUltimo(Long idProducto) {
        List<Stock> lista = findUltimoMovimiento(idProducto);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }
}
