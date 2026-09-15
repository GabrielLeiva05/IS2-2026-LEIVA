package com.furnish.erp.repository;

import com.furnish.erp.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Usuario. findByUsername es utilizado por
 * UsuarioServiceImpl (que implementa UserDetailsService de Spring Security)
 * para cargar las credenciales durante el proceso de login.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
