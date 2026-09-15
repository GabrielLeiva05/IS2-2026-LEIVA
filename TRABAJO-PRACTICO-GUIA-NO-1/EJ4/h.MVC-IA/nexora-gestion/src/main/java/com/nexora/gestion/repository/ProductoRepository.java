package com.nexora.gestion.repository;

import com.nexora.gestion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Producto.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** Lista solo los productos activos (no eliminados lógicamente),
     *  pensado para mostrarse en el catálogo visible a los usuarios. */
    List<Producto> findByActivoTrue();

    /** Búsqueda simple por coincidencia parcial de nombre (ignora
     *  mayúsculas/minúsculas), usada por el buscador del catálogo. */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
