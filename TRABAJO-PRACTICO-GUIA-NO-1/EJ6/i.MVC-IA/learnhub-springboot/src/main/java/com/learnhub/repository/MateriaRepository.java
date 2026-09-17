package com.learnhub.repository;

import com.learnhub.entity.Materia;
import com.learnhub.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Persistencia de materias. */
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByProfesorAndEliminadoFalseOrderByNombreAsc(Profesor profesor);
}
