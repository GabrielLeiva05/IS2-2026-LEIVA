package com.furnish.erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) que representa UNA línea del formulario de
 * carga de OrdenCompra. Se usa un DTO en vez de la entidad DetalleOrden
 * directamente en el formulario para:
 *  1) Desacoplar la vista del modelo de persistencia.
 *  2) Poder recibir el idProducto como campo simple (un <select>) en vez de
 *     requerir que Thymeleaf arme un objeto Producto completo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenForm {

    @NotNull(message = "Debe seleccionar un producto")
    private Long idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidadProducto;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private Double precioUnitario;
}
