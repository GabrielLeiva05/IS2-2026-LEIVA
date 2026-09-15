package com.nexora.gestion.controller;

import com.nexora.gestion.dto.ProductoForm;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.service.InventarioService;
import com.nexora.gestion.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * "ProductoController" (Capa de Controlador) — Administración de Productos
 * ============================================================================
 * Rutas bajo "/admin/productos/**" (protegidas: solo Administrador).
 * Implementa registrarProducto()/editarProducto()/eliminarProducto() del
 * diagrama UML delegando siempre en ProductoService. También expone el
 * stock actual de cada producto (calculado por InventarioService) para que
 * la vista lo muestre junto al listado.
 * ============================================================================
 */
@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final InventarioService inventarioService;

    public ProductoController(ProductoService productoService, InventarioService inventarioService) {
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Producto> productos = productoService.listarTodos();
        // Mapa auxiliar producto.id -> stock actual, calculado vía Service
        // (nunca leído directamente de un campo desnormalizado).
        Map<Long, Integer> stockPorProducto = new HashMap<>();
        for (Producto p : productos) {
            stockPorProducto.put(p.getId(), inventarioService.calcularStockActual(p));
        }
        model.addAttribute("productos", productos);
        model.addAttribute("stockPorProducto", stockPorProducto);
        return "productos/list"; // -> templates/productos/list.html
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAlta(@ModelAttribute("productoForm") ProductoForm form) {
        return "productos/form"; // -> templates/productos/form.html
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new com.nexora.gestion.exception.RecursoNoEncontradoException(
                        "Producto no encontrado: id=" + id));

        ProductoForm form = new ProductoForm();
        form.setId(producto.getId());
        form.setNombre(producto.getNombre());
        form.setDescripcion(producto.getDescripcion());
        form.setPrecio(producto.getPrecio());

        model.addAttribute("productoForm", form);
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("productoForm") ProductoForm form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "productos/form";
        }

        Producto producto = new Producto();
        producto.setNombre(form.getNombre());
        producto.setDescripcion(form.getDescripcion());
        producto.setPrecio(form.getPrecio());

        if (form.getId() == null) {
            productoService.registrarProducto(producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto creado correctamente.");
        } else {
            productoService.editarProducto(form.getId(), producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto actualizado correctamente.");
        }
        return "redirect:/admin/productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productoService.eliminarProducto(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado (o dado de baja) correctamente.");
        return "redirect:/admin/productos";
    }
}
