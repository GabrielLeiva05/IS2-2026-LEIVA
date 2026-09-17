package com.learnhub.repository;

import com.learnhub.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Persistencia de roles. */
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
