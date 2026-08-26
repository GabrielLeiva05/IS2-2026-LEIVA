package com.nexora.gestion.repository;

import com.nexora.gestion.model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad DetalleCompra.
 * No necesita consultas propias por ahora: se accede a los detalles
 * navegando la relación Compra -> detalles (composición).
 */
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
}
