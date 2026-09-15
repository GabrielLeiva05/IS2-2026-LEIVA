package com.furnish.erp.controller;

import com.furnish.erp.service.EmpleadoService;
import com.furnish.erp.service.OrdenCompraService;
import com.furnish.erp.service.ProductoService;
import com.furnish.erp.service.ProveedorService;
import com.furnish.erp.domain.enums.EstadoOrden;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la pantalla de inicio tras el login: muestra indicadores
 * simples (cantidad de empleados, proveedores, productos y órdenes
 * pendientes) consultando exclusivamente a los Services correspondientes.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final EmpleadoService empleadoService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final OrdenCompraService ordenCompraService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("cantidadEmpleados", empleadoService.listarActivos().size());
        model.addAttribute("cantidadProveedores", proveedorService.listarActivos().size());
        model.addAttribute("cantidadProductos", productoService.listarActivos().size());
        model.addAttribute("ordenesPendientes",
                ordenCompraService.listarPorEstado(EstadoOrden.PENDIENTE).size());
        model.addAttribute("ordenesAprobadas",
                ordenCompraService.listarPorEstado(EstadoOrden.APROBADA).size());
        return "dashboard";
    }
}
