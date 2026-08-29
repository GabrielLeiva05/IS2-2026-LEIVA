package com.furnish.erp.controller;

import com.furnish.erp.domain.enums.EstadoOrden;
import com.furnish.erp.dto.DetalleOrdenForm;
import com.furnish.erp.dto.OrdenCompraForm;
import com.furnish.erp.service.EmpleadoService;
import com.furnish.erp.service.OrdenCompraService;
import com.furnish.erp.service.ProductoService;
import com.furnish.erp.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

/**
 * ============================================================================
 * Controlador: OrdenCompraController
 * ============================================================================
 * Orquesta el caso de uso principal del sistema: registrar una orden de
 * compra a un proveedor con N líneas de producto, y luego ir avanzando su
 * estado (PENDIENTE -> APROBADA -> RECIBIDA / CANCELADA). Toda la lógica de
 * cálculo de totales y de impacto en el stock vive en OrdenCompraService.
 * ============================================================================
 */
@Controller
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final EmpleadoService empleadoService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ordenes", ordenCompraService.listarTodas());
        return "ordenes/list";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model) {
        model.addAttribute("orden", ordenCompraService.buscarPorId(id));
        return "ordenes/ver";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        OrdenCompraForm form = new OrdenCompraForm();
        // Se agrega una línea de detalle vacía inicial para que el formulario
        // Thymeleaf ya tenga al menos una fila renderizada (el usuario puede
        // agregar más filas dinámicamente con JavaScript, ver ordenes/form.html).
        form.getDetalles().add(new DetalleOrdenForm());
        cargarCombos(model);
        model.addAttribute("ordenForm", form);
        return "ordenes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("ordenForm") OrdenCompraForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        // Descarta filas completamente vacías que puedan llegar del formulario
        // dinámico (por ejemplo, si el usuario agregó una fila y no la completó).
        form.setDetalles(new ArrayList<>(form.getDetalles().stream()
                .filter(d -> d.getIdProducto() != null)
                .toList()));

        if (form.getDetalles().isEmpty()) {
            bindingResult.reject("detalles.vacio", "Debe cargar al menos un producto en la orden");
        }

        if (bindingResult.hasErrors()) {
            cargarCombos(model);
            return "ordenes/form";
        }

        var orden = ordenCompraService.registrarOrden(form);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Orden de compra #" + orden.getIdOrdenCompra() + " registrada correctamente.");
        return "redirect:/ordenes";
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                 @RequestParam("nuevoEstado") EstadoOrden nuevoEstado,
                                 RedirectAttributes redirectAttributes) {
        ordenCompraService.cambiarEstado(id, nuevoEstado);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "La orden #" + id + " pasó al estado " + nuevoEstado + ".");
        return "redirect:/ordenes/ver/" + id;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("empleados", empleadoService.listarActivos());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
    }
}
