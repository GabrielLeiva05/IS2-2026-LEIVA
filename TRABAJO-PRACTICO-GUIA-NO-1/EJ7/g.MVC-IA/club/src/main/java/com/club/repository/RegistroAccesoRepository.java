package com.club.repository;
import com.club.entity.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso,String> {
    List<RegistroAcceso> findAllByOrderByFechaDescHoraEntradaDesc();
    List<RegistroAcceso> findByPersonaIdOrderByFechaDescHoraEntradaDesc(String personaId);
    boolean existsByPersonaIdAndHoraSalidaIsNull(String personaId);
}
