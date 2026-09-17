package com.learnhub.repository;

import com.learnhub.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Persistencia de alumnos. */
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findByEliminadoFalseOrderByApellidoAscNombreAsc();
}
