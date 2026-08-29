package com.furnish.erp.controller;

import com.furnish.erp.dto.StockMovimientoForm;
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

/**
 * Controlador del módulo de Stock (Kardex de inventario). Permite consultar
 * el stock actual/historial por producto y registrar ajustes manuales
 * (entradas/salidas que no provienen de una OrdenCompra).
 */
@Controller
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final ProductoService productoService;

    @GetMapping
    public String listar(Model model) {
        var productos = productoService.listarActivos();
        Map<Long, Integer> stockPorProducto = new HashMap<>();
        for (var p : productos) {
            stockPorProducto.put(p.getId(), stockService.consultarStockActual(p.getId()));
        }
        model.addAttribute("productos", productos);
        model.addAttribute("stockPorProducto", stockPorProducto);
        return "stock/list";
    }

    @GetMapping("/historial/{idProducto}")
    public String historial(@PathVariable Long idProducto, Model model) {
        model.addAttribute("producto", productoService.buscarPorId(idProducto));
        model.addAttribute("movimientos", stockService.historialDeProducto(idProducto));
        model.addAttribute("stockActual", stockService.consultarStockActual(idProducto));
        return "stock/historial";
    }

    @GetMapping("/ajuste")
    public String ajusteForm(Model model) {
        model.addAttribute("movimiento", new StockMovimientoForm());
        model.addAttribute("productos", productoService.listarActivos());
        return "stock/form";
    }

    @PostMapping("/ajuste")
    public String registrarAjuste(@Valid @ModelAttribute("movimiento") StockMovimientoForm form,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productos", productoService.listarActivos());
            return "stock/form";
        }
        stockService.registrarMovimientoManual(form.getIdProducto(), form.getTipoMovimiento(), form.getCantidad());
        redirectAttributes.addFlashAttribute("mensajeExito", "Movimiento de stock registrado correctamente.");
        return "redirect:/stock";
    }
}
