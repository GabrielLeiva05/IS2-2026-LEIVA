package com.club.service;

import com.club.dto.EditarSocioDTO;
import com.club.dto.RegistrarSocioDTO;
import com.club.dto.SocioResponse;
import com.club.entity.Socio;
import com.club.enumeration.EstadoSocio;
import com.club.exception.BusinessException;
import com.club.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class SocioService {
    private final SocioRepository socioRepository;
    @Transactional public SocioResponse registrar(RegistrarSocioDTO dto) {
        validarDatos(dto); String email=dto.email().trim().toLowerCase();
        if(socioRepository.existsByEmailIgnoreCaseAndEliminadoFalse(email)) throw new BusinessException("Ya existe un socio activo con ese email.");
        Socio s=new Socio(dto.nombre().trim(),dto.apellido().trim(),email,dto.telefono().trim()); s.setFechaNacimiento(dto.fechaNacimiento()); s.setFechaAlta(LocalDate.now()); s.setEstado(EstadoSocio.ACTIVO); s.setEliminado(false); return toDto(socioRepository.save(s));
    }
    @Transactional(readOnly=true) public List<SocioResponse> listar(){ return socioRepository.findByEliminadoFalseOrderByApellidoAscNombreAsc().stream().map(this::toDto).toList(); }
    @Transactional(readOnly=true) public List<SocioResponse> listarTodos(){ return socioRepository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly=true) public SocioResponse obtener(String id){ return toDto(getEntity(id)); }
    @Transactional public SocioResponse editar(String id, EditarSocioDTO dto){
        validarDatos(new RegistrarSocioDTO(dto.nombre(),dto.apellido(),dto.email(),dto.telefono(),dto.fechaNacimiento())); Socio s=getEntity(id); String email=dto.email().trim().toLowerCase();
        if(!email.equalsIgnoreCase(s.getEmail()) && socioRepository.existsByEmailIgnoreCaseAndEliminadoFalse(email)) throw new BusinessException("Ya existe un socio activo con ese email.");
        s.setNombre(dto.nombre().trim()); s.setApellido(dto.apellido().trim()); s.setEmail(email); s.setTelefono(dto.telefono().trim()); s.setFechaNacimiento(dto.fechaNacimiento()); return toDto(socioRepository.save(s));
    }
    @Transactional public void darDeBaja(String id){ Socio s=getEntity(id); s.setEliminado(true); s.setEstado(EstadoSocio.INACTIVO); }
    @Transactional public void reactivar(String id){ Socio s=getEntity(id); s.setEliminado(false); s.setEstado(EstadoSocio.ACTIVO); }
    private Socio getEntity(String id){ return socioRepository.findById(id).orElseThrow(()->new BusinessException("Socio no encontrado.")); }
    private SocioResponse toDto(Socio s){ String fid=s.getGrupoFamiliar()==null?null:s.getGrupoFamiliar().getId(); String fn=s.getGrupoFamiliar()==null?null:s.getGrupoFamiliar().getNombre(); return new SocioResponse(s.getId(),s.getNombre(),s.getApellido(),s.getEmail(),s.getTelefono(),s.getFechaNacimiento(),s.getFechaAlta(),s.getEstado(),s.isEliminado(),fid,fn,s.getImagen()!=null && !s.getImagen().isEliminado()); }
    private void validarDatos(RegistrarSocioDTO d){ if(d==null||d.fechaNacimiento()==null||d.nombre()==null||d.apellido()==null||d.email()==null||d.telefono()==null) throw new BusinessException("Todos los datos del socio son obligatorios."); if(d.fechaNacimiento().isAfter(LocalDate.now())) throw new BusinessException("La fecha de nacimiento no puede ser futura."); if(d.nombre().trim().length()<2||d.apellido().trim().length()<2) throw new BusinessException("Nombre y apellido deben tener al menos 2 caracteres."); }
}
