package com.club.dto;
import java.math.BigDecimal;
import java.util.List;
public record FamiliaDetalleResponse(String id,String nombre,BigDecimal cuotaMensual,PersonaResponse titular,List<PersonaResponse> familiares,List<PagoResponse> pagos) {}
