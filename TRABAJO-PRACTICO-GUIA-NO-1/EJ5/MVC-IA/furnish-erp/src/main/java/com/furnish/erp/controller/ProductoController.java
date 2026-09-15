package com.furnish.erp.controller;

import com.furnish.erp.domain.Producto;
import com.furnish.erp.service.ProductoService;
import com.furnish.erp.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final StockService stockService;

    @GetMapping
    public String listar(@RequestParam(value = "buscar", required = false) String buscar, Model model) {
        var productos = (buscar == null || buscar.isBlank())
                ? productoService.listarActivos()
                : productoService.buscarPorNombre(buscar);

        // El listado también muestra el stock actual de cada producto,
        // consultado a través de StockService (nunca accediendo a su
        // repositorio directamente desde el controller).
        Map<Long, Integer> stockPorProducto = new HashMap<>();
        for (Producto p : productos) {
            stockPorProducto.put(p.getId(), stockService.consultarStockActual(p.getId()));
        }

        model.addAttribute("productos", productos);
        model.addAttribute("stockPorProducto", stockPorProducto);
        model.addAttribute("buscar", buscar);
        return "productos/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.buscarPorId(id));
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "productos/form";
        }
        if (producto.getId() == null) {
            productoService.cargarProducto(producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto cargado correctamente.");
        } else {
            productoService.editarProducto(producto.getId(), producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto actualizado correctamente.");
        }
        return "redirect:/productos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productoService.eliminarProducto(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
        return "redirect:/productos";
    }
}
