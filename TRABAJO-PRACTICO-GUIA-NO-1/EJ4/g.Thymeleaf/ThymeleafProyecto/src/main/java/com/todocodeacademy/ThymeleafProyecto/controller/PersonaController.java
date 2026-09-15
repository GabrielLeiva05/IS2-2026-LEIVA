package com.todocodeacademy.ThymeleafProyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.todocodeacademy.ThymeleafProyecto.model.Persona;

/**
 *
 * @author Gabriel
 */

@Controller
public class PersonaController {
    
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("persona", new Persona());
        return "formulario";
    }
    
    @PostMapping("/procesar")
    public String procesarFormulario(
            Persona persona,
            Model model) {
        
        model.addAttribute("persona", persona);
        
        if (persona.getEdad() >= 18) {
            model.addAttribute("tipoEdad", "Mayor de Edad");
        } else {
            model.addAttribute("tipoEdad", "Es menor de edad");
        }
        
        return "resultado";
    }
}
