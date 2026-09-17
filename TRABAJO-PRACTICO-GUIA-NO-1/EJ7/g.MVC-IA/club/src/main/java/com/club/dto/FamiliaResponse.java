package com.club.dto;
import java.math.BigDecimal;
public record FamiliaResponse(String id, String nombre, BigDecimal cuotaMensual, String titularId, String titularNombre, int cantidadFamiliares) {}
