package com.nexora.gestion.repository;

import com.nexora.gestion.model.Compra;
import com.nexora.gestion.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Compra.
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    /** Historial de compras de un usuario, más recientes primero. */
    @Query("""
           SELECT DISTINCT c
           FROM Compra c
           LEFT JOIN FETCH c.detalles d
           LEFT JOIN FETCH d.producto
           WHERE c.usuario = :usuario
           ORDER BY c.fechaCompra DESC
           """)
    List<Compra> findByUsuarioWithDetalles(@Param("usuario") Usuario usuario);

    /** Todas las compras del sistema, para el panel del administrador. */
    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    @Query("SELECT DISTINCT c FROM Compra c ORDER BY c.fechaCompra DESC")
    List<Compra> findAllByOrderByFechaCompraDesc();

    /** Compra con sus detalles y productos cargados para la vista del carrito. */
    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    @Query("SELECT c FROM Compra c WHERE c.id = :id")
    Optional<Compra> findByIdWithDetalles(@Param("id") Long id);
}
