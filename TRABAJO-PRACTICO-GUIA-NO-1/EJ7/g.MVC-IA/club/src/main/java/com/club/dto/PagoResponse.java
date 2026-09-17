package com.club.dto;
import com.club.enumeration.EstadoPago;
import com.club.enumeration.MedioPago;
import java.math.BigDecimal;
import java.time.LocalDate;
public record PagoResponse(String id,String familiaId,String familiaNombre,String periodo,BigDecimal importe,MedioPago medioPago,EstadoPago estado,LocalDate fechaPago,String referencia) {}
