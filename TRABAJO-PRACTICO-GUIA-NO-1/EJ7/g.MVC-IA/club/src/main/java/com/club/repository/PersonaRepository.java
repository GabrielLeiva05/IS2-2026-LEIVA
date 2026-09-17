package com.club.repository;
import com.club.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PersonaRepository extends JpaRepository<Persona,String> {
    List<Persona> findByEliminadoFalseOrderByApellidoAscNombreAsc();
}
