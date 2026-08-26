package com.nexora.gestion.repository;

import com.nexora.gestion.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Administrador. Ver el javadoc de
 * {@link UsuarioRepository} para la explicación general del rol del ORM acá.
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    /** Busca un administrador por correo (usado para el login). */
    Optional<Administrador> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
