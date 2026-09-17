package com.club.dto;
import com.club.enumeration.EstadoSocio;
import java.time.LocalDate;
public record SocioResponse(String id,String nombre,String apellido,String email,String telefono,LocalDate fechaNacimiento,LocalDate fechaAlta,EstadoSocio estado,boolean eliminado,String familiaId,String familiaNombre,boolean tieneImagen) {}
