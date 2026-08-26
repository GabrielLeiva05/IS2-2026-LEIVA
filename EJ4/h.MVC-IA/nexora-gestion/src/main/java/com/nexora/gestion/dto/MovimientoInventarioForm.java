package com.nexora.gestion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para registrar manualmente un movimiento de stock (reposición) desde
 * el panel de administración de Inventario.
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 */
public class MovimientoInventarioForm {

    @NotNull(message = "Debe seleccionar un producto.")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero.")
    private Integer cantidad;

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
