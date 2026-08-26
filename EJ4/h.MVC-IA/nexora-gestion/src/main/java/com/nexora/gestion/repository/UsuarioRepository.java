package com.nexora.gestion.repository;

import com.nexora.gestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * REPOSITORIO "UsuarioRepository" (Modelo / Persistencia — ORM)
 * ============================================================================
 * Extiende {@link JpaRepository}, que mediante Spring Data JPA nos da "gratis"
 * (sin escribir SQL ni implementaciones) las operaciones CRUD básicas:
 * save(), findById(), findAll(), deleteById(), etc. Esto es el "ORM" en
 * acción: Spring Data JPA + Hibernate traducen estas llamadas a sentencias
 * SQL sobre la tabla "usuarios" en MySQL, y devuelven objetos Java (Usuario)
 * en lugar de ResultSets crudos.
 *
 * Los métodos "findByCorreo" y "existsByCorreo" son "Query Methods": Spring
 * Data JPA interpreta el nombre del método y genera la consulta JPQL/SQL
 * automáticamente, sin que tengamos que escribir una sola línea de SQL.
 *
 * IMPORTANTE: este repositorio NO contiene lógica de negocio, solo consultas.
 * Toda regla (ej. "no permitir loguearse si está bloqueado") vive en
 * UsuarioServiceImpl, que es quien usa este repositorio como colaborador.
 * ============================================================================
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** Busca un usuario por su correo (usado para el login). */
    Optional<Usuario> findByCorreo(String correo);

    /** Indica si ya existe un usuario registrado con ese correo (para
     *  validar duplicados al registrar una cuenta nueva). */
    boolean existsByCorreo(String correo);

    /** Lista usuarios filtrando por si están bloqueados o no. */
    List<Usuario> findByBloqueado(Boolean bloqueado);
}
