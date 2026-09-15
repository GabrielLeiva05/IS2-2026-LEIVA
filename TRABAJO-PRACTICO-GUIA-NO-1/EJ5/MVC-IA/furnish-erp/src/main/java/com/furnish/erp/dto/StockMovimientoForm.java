package com.furnish.erp.dto;

import com.furnish.erp.domain.enums.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para el formulario de "ajuste manual de stock" (fuera del flujo
 * automático de recepción de una OrdenCompra): permite a un empleado
 * registrar una entrada o salida manual (ej: rotura, ajuste de inventario).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovimientoForm {

    @NotNull(message = "Debe seleccionar un producto")
    private Long idProducto;

    @NotNull(message = "Debe indicar el tipo de movimiento")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
