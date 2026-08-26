package com.nexora.gestion.controller;

import com.nexora.gestion.model.Producto;
import com.nexora.gestion.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * ============================================================================
 * "HomeController" (Capa de Controlador)
 * ============================================================================
 * @Controller (no @RestController): indica que los métodos devuelven
 * NOMBRES DE VISTA (Strings que Thymeleaf resuelve a plantillas .html en
 * src/main/resources/templates), no datos serializados como JSON. Este es
 * el patrón MVC "clásico" pedido: Controller -> Model -> View (Thymeleaf).
 *
 * El Controller es DELIBERADAMENTE "delgado" (thin controller): solo llama
 * al Service correspondiente y arma el Model para la vista. No contiene
 * ninguna regla de negocio ni acceso directo a repositorios.
 * ============================================================================
 */
@Controller
public class HomeController {

    private final ProductoService productoService;

    public HomeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /** Página de inicio pública: muestra el catálogo de productos activos. */
    @GetMapping("/")
    public String home(Model model) {
        List<Producto> productos = productoService.listarActivos();
        model.addAttribute("productos", productos);
        return "index"; // -> templates/index.html
    }
}
