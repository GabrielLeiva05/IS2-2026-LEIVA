package com.club.service;

import com.club.dto.*;
import com.club.entity.GrupoFamiliar;
import com.club.entity.Persona;
import com.club.entity.Socio;
import com.club.exception.BusinessException;
import com.club.repository.GrupoFamiliarRepository;
import com.club.repository.PersonaRepository;
import com.club.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class FamiliaService {
    private final GrupoFamiliarRepository familiaRepository; private final SocioRepository socioRepository; private final PersonaRepository personaRepository;
    @Transactional public FamiliaResponse registrar(RegistrarFamiliaDTO dto){
        if(dto==null||dto.nombre()==null||dto.nombre().isBlank()||dto.cuotaMensual()==null||dto.titularId()==null||dto.titularId().isBlank()) throw new BusinessException("Los datos de la familia son obligatorios.");
        if(dto.cuotaMensual().signum()<=0) throw new BusinessException("La cuota mensual debe ser mayor a cero."); Socio titular=socioRepository.findById(dto.titularId()).orElseThrow(()->new BusinessException("El socio titular no existe.")); validarTitular(titular);
        GrupoFamiliar f=new GrupoFamiliar(); f.setNombre(dto.nombre().trim()); f.setCuotaMensual(dto.cuotaMensual()); f.setTitular(titular); familiaRepository.save(f); titular.setGrupoFamiliar(f); f.getFamiliares().add(titular); return toDto(f);
    }
    @Transactional public FamiliaResponse editar(String id,EditarFamiliaDTO dto){ if(dto==null||dto.nombre()==null||dto.nombre().isBlank()||dto.cuotaMensual()==null||dto.cuotaMensual().signum()<=0) throw new BusinessException("Los datos de la familia son inválidos."); GrupoFamiliar f=getEntity(id); f.setNombre(dto.nombre().trim()); f.setCuotaMensual(dto.cuotaMensual()); return toDto(familiaRepository.save(f)); }
    @Transactional(readOnly=true) public List<FamiliaResponse> listar(){ return familiaRepository.findAllByOrderByNombreAsc().stream().map(this::toDto).toList(); }
    @Transactional(readOnly=true) public FamiliaDetalleResponse obtenerDetalle(String id){ GrupoFamiliar f=getEntity(id); return new FamiliaDetalleResponse(f.getId(),f.getNombre(),f.getCuotaMensual(),persona(f.getTitular()),f.getFamiliares().stream().map(this::persona).toList(),f.getPagos().stream().map(p->new PagoResponse(p.getId(),f.getId(),f.getNombre(),p.getPeriodo(),p.getImporte(),p.getMedioPago(),p.getEstado(),p.getFechaPago(),p.getReferencia())).toList()); }
    @Transactional public void agregarFamiliar(String familiaId,String personaId){ GrupoFamiliar f=getEntity(familiaId); Persona p=personaRepository.findById(personaId).orElseThrow(()->new BusinessException("La persona no existe.")); if(p.isEliminado()) throw new BusinessException("No se puede agregar una persona dada de baja."); if(p.getGrupoFamiliar()!=null&&!familiaId.equals(p.getGrupoFamiliar().getId())) throw new BusinessException("La persona ya pertenece a otro grupo familiar."); if(f.getFamiliares().stream().anyMatch(x->x.getId().equals(personaId))) throw new BusinessException("La persona ya pertenece a esta familia."); p.setGrupoFamiliar(f); }
    @Transactional public void eliminarFamiliar(String familiaId,String personaId){ GrupoFamiliar f=getEntity(familiaId); Persona p=personaRepository.findById(personaId).orElseThrow(()->new BusinessException("La persona no existe.")); if(f.getTitular().getId().equals(personaId)) throw new BusinessException("El titular no puede quitarse de la familia."); if(p.getGrupoFamiliar()==null||!familiaId.equals(p.getGrupoFamiliar().getId())) throw new BusinessException("La persona no pertenece a esta familia."); p.setGrupoFamiliar(null); }
    @Transactional public void eliminar(String id){ GrupoFamiliar f=getEntity(id); if(!f.getPagos().isEmpty()) throw new BusinessException("No se puede eliminar una familia que tiene pagos registrados."); for(Persona p:personaRepository.findAll()) if(p.getGrupoFamiliar()!=null&&id.equals(p.getGrupoFamiliar().getId())) p.setGrupoFamiliar(null); familiaRepository.delete(f); }
    private GrupoFamiliar getEntity(String id){ return familiaRepository.findById(id).orElseThrow(()->new BusinessException("Grupo familiar no encontrado.")); }
    private void validarTitular(Socio s){ if(s.isEliminado()||s.getEstado()!=com.club.enumeration.EstadoSocio.ACTIVO) throw new BusinessException("El titular debe ser un socio activo."); if(s.getGrupoFamiliar()!=null) throw new BusinessException("El socio ya pertenece a un grupo familiar."); }
    private FamiliaResponse toDto(GrupoFamiliar f){ return new FamiliaResponse(f.getId(),f.getNombre(),f.getCuotaMensual(),f.getTitular().getId(),f.getTitular().getNombre()+" "+f.getTitular().getApellido(),f.getFamiliares().size()); }
    private PersonaResponse persona(Persona p){ return new PersonaResponse(p.getId(),p.getNombre(),p.getApellido(),p.getEmail(),p.getTelefono(),p.getFechaNacimiento(),p.isEliminado(),p.getGrupoFamiliar()==null?null:p.getGrupoFamiliar().getId()); }
}
