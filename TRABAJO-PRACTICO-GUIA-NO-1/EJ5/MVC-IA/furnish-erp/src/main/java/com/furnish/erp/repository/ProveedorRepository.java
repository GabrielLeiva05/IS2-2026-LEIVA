package com.furnish.erp.repository;

import com.furnish.erp.domain.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByEliminadoFalseOrderByNombreAsc();

    /** NOTA: se usa "findByIdProveedor..." (y no "findById...") porque el
     *  campo real de la entidad se llama "idProveedor". Ver aclaración
     *  equivalente en EmpleadoRepository. */
    Optional<Proveedor> findByIdProveedorAndEliminadoFalse(Long idProveedor);
}
