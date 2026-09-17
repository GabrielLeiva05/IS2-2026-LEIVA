package com.club.dto;
import java.time.LocalDate;
public record PersonaResponse(String id,String nombre,String apellido,String email,String telefono,LocalDate fechaNacimiento,boolean eliminado,String familiaId) {}
