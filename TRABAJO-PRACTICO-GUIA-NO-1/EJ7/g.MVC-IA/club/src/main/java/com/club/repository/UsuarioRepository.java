package com.club.repository;
import com.club.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario,String> { Optional<Usuario> findByUsernameIgnoreCase(String username); }
