package com.learnhub.repository;

import com.learnhub.entity.Materia;
import com.learnhub.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Persistencia de notas. */
public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByMateriaOrderByFechaDesc(Materia materia);
}
