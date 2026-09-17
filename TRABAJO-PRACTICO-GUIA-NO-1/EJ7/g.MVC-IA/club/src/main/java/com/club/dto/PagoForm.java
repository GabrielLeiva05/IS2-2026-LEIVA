package com.club.dto;
import com.club.enumeration.MedioPago;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
@Getter @Setter
public class PagoForm { private String periodo; private BigDecimal importe; private MedioPago medioPago; private String referencia; }
