package com.learnhub.repository;

import com.learnhub.entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistencia de grados. */
public interface GradoRepository extends JpaRepository<Grado, Long> {
    boolean existsByNivelIgnoreCase(String nivel);
}
