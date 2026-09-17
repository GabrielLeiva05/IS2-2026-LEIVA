package com.learnhub.repository;

import com.learnhub.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Consultas de persistencia específicas de Profesor. */
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    Optional<Profesor> findByUsuarioEmailIgnoreCase(String email);
    List<Profesor> findByEliminadoFalseOrderByApellidoAscNombreAsc();
}
