package com.furnish.erp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO raíz del formulario "Nueva orden de compra": agrupa el encabezado
 * (empleado, proveedor) con una lista dinámica de líneas de detalle. El
 * controlador (OrdenCompraController) recibe este objeto con @ModelAttribute
 * y delega toda la creación real (persistencia, cálculo de totales) a
 * OrdenCompraService.registrarOrden(...).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompraForm {

    @NotNull(message = "Debe seleccionar un empleado")
    private Long idEmpleado;

    @NotNull(message = "Debe seleccionar un proveedor")
    private Long idProveedor;

    @Size(min = 1, message = "La orden debe tener al menos un producto")
    private List<@Valid DetalleOrdenForm> detalles = new ArrayList<>();
}
