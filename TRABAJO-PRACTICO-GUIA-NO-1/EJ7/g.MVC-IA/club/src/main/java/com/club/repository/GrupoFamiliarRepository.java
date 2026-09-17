package com.club.repository;
import com.club.entity.GrupoFamiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GrupoFamiliarRepository extends JpaRepository<GrupoFamiliar,String> {
    List<GrupoFamiliar> findAllByOrderByNombreAsc();
}
