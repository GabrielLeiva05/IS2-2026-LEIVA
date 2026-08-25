package com.todocodeacademy.ThymeleafProyecto.controller;

import java.util.ArrayList;
import java.util.List;
import com.todocodeacademy.ThymeleafProyecto.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Gabriel
 */
@Controller
public class ProductoController {
    
    @GetMapping("/productos")
    public String mostrarProductos(Model model) {
        
        List<Producto> listaProductos = new ArrayList<>();
        
        listaProductos.add(new Producto(1L, "Notebook IdeaPad", "Lenovo", 850000));
        listaProductos.add(new Producto(2L, "Mouse Inalambrico", "Logitech", 25000));
        listaProductos.add(new Producto(3L, "Teclado Mecanico", "Redragon", 850000));
        listaProductos.add(new Producto(4L, "Monitor", "Samsung", 850000));
        listaProductos.add(new Producto(5L, "Auriculares", "Sony", 850000));
        
        model.addAttribute("listaProductos", listaProductos);
        
        return "productos";
    }
}
