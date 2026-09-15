package com.furnish.erp.controller;

import com.furnish.erp.domain.Empleado;
import com.furnish.erp.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * Controlador: EmpleadoController
 * ============================================================================
 * Capa Controlador de MVC: SOLO se encarga de:
 *   1) Recibir la petición HTTP (GET/POST) y sus parámetros/formularios.
 *   2) Delegar toda decisión de negocio al Service correspondiente.
 *   3) Elegir qué VISTA Thymeleaf renderizar (o a qué URL redirigir) y qué
 *      datos ponerle al Model para que la vista los muestre.
 * No contiene ninguna validación de negocio ni acceso a repositorios: eso
 * vive exclusivamente en EmpleadoService.
 * ============================================================================
 */
@Controller
@RequestMapping("/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empleados", empleadoService.listarActivos());
        return "empleados/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "empleados/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("empleado", empleadoService.buscarPorId(id));
        return "empleados/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empleado") Empleado empleado,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "empleados/form";
        }
        if (empleado.getIdEmpleado() == null) {
            empleadoService.registrarEmpleado(empleado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Empleado registrado correctamente.");
        } else {
            empleadoService.editarEmpleado(empleado.getIdEmpleado(), empleado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Empleado actualizado correctamente.");
        }
        return "redirect:/empleados";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        empleadoService.eliminarEmpleado(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Empleado eliminado correctamente.");
        return "redirect:/empleados";
    }
}
