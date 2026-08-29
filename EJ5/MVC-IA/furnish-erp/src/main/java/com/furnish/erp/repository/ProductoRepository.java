package com.furnish.erp.repository;

import com.furnish.erp.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEliminadoFalseOrderByNombreAsc();

    Optional<Producto> findByIdAndEliminadoFalse(Long id);

    /** Búsqueda simple por nombre para el buscador del listado de productos
     *  (usa LIKE %texto% generado automáticamente por "Containing"). */
    List<Producto> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}
