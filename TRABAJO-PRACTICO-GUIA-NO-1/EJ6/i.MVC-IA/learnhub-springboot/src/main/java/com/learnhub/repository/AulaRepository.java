package com.learnhub.repository;

import com.learnhub.entity.Aula;
import com.learnhub.entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Persistencia de aulas. */
public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByGradoOrderByDivisionAsc(Grado grado);
}
