package com.furnish.erp.controller;

import com.furnish.erp.domain.Proveedor;
import com.furnish.erp.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorService.listarActivos());
        return "proveedores/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("proveedor", proveedorService.buscarPorId(id));
        return "proveedores/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "proveedores/form";
        }
        if (proveedor.getIdProveedor() == null) {
            proveedorService.registrarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("mensajeExito", "Proveedor registrado correctamente.");
        } else {
            proveedorService.editarProveedor(proveedor.getIdProveedor(), proveedor);
            redirectAttributes.addFlashAttribute("mensajeExito", "Proveedor actualizado correctamente.");
        }
        return "redirect:/proveedores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        proveedorService.eliminarProveedor(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Proveedor eliminado correctamente.");
        return "redirect:/proveedores";
    }
}
