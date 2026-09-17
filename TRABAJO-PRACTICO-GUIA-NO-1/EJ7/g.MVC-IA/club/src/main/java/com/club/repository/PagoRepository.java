package com.club.repository;
import com.club.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PagoRepository extends JpaRepository<Pago,String> {
    boolean existsByGrupoFamiliarIdAndPeriodo(String grupoId, String periodo);
    List<Pago> findByGrupoFamiliarIdOrderByFechaPagoDesc(String grupoId);
    long countByGrupoFamiliarIdAndEstado(String grupoId, com.club.enumeration.EstadoPago estado);
}
