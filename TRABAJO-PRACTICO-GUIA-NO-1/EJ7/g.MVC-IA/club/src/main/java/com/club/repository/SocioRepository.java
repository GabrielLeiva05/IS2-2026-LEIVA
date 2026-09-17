package com.club.repository;
import com.club.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SocioRepository extends JpaRepository<Socio,String> {
    List<Socio> findByEliminadoFalseOrderByApellidoAscNombreAsc();
    boolean existsByEmailIgnoreCaseAndEliminadoFalse(String email);
}
