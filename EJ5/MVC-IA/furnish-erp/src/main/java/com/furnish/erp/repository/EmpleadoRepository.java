package com.furnish.erp.repository;

import com.furnish.erp.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * Repositorio Spring Data JPA para Empleado.
 * ============================================================================
 * Al extender JpaRepository<Empleado, Long>, Spring Data JPA genera en tiempo
 * de ejecución (proxy dinámico) la implementación de operaciones CRUD básicas
 * (save, findById, findAll, deleteById, etc.) sin necesidad de escribir SQL ni
 * implementar la interfaz manualmente. @Repository es opcional (Spring ya lo
 * detecta por herencia de JpaRepository) pero se deja explícito por claridad
 * y para habilitar la traducción de excepciones JDBC -> DataAccessException.
 *
 * Los métodos "derivados" (findByEliminadoFalse, existsByDni, etc.) se
 * generan automáticamente por Spring Data a partir del NOMBRE del método,
 * sin necesidad de escribir la consulta (Query Derivation / Query Methods).
 * ============================================================================
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /** Lista solo los empleados activos (no eliminados lógicamente). */
    List<Empleado> findByEliminadoFalseOrderByNombreAsc();

    /** Usado por el Service para validar unicidad de DNI antes de registrar. */
    boolean existsByDni(Integer dni);

    /** NOTA: el método debe llamarse "findByIdEmpleado..." (y no "findById...")
     *  porque Spring Data JPA deriva la consulta a partir del NOMBRE del
     *  atributo real de la entidad, que en este caso es "idEmpleado" (no
     *  "id"). Usar "findById" solo funciona si el campo se llama literalmente
     *  "id" (como sí ocurre, por ejemplo, en la entidad Producto). */
    Optional<Empleado> findByIdEmpleadoAndEliminadoFalse(Long idEmpleado);
}
