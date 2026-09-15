package com.nexora.gestion.controller;

import com.nexora.gestion.dto.MovimientoInventarioForm;
import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.exception.StockInsuficienteException;
import com.nexora.gestion.model.Inventario;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.model.TipoMovimiento;
import com.nexora.gestion.service.InventarioService;
import com.nexora.gestion.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * ============================================================================
 * "InventarioController" (Capa de Controlador) — Panel de administración
 * ============================================================================
 * Rutas bajo "/admin/inventario/**" (solo Administrador). Expone el listado
 * global de movimientos y el formulario para "registrarMovimiento()"
 * (reposición manual de stock, TipoMovimiento.ENTRADA), delegando toda la
 * validación (cantidad > 0, stock suficiente en caso de salidas) en
 * InventarioService.
 * ============================================================================
 */
@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;

    public InventarioController(InventarioService inventarioService, ProductoService productoService) {
        this.inventarioService = inventarioService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Inventario> movimientos = inventarioService.listarTodos();
        model.addAttribute("movimientos", movimientos);
        return "inventario/list"; // -> templates/inventario/list.html
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(@ModelAttribute("movimientoForm") MovimientoInventarioForm form, Model model) {
        model.addAttribute("productos", productoService.listarActivos());
        return "inventario/form"; // -> templates/inventario/form.html
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("movimientoForm") MovimientoInventarioForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productos", productoService.listarActivos());
            return "inventario/form";
        }
        try {
            Producto producto = productoService.buscarPorId(form.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado."));

            inventarioService.registrarMovimiento(producto, form.getCantidad(), TipoMovimiento.ENTRADA);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Se registraron " + form.getCantidad() + " unidades de stock para '" + producto.getNombre() + "'.");
            return "redirect:/admin/inventario";
        } catch (StockInsuficienteException | RecursoNoEncontradoException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            model.addAttribute("productos", productoService.listarActivos());
            return "inventario/form";
        }
    }
}
