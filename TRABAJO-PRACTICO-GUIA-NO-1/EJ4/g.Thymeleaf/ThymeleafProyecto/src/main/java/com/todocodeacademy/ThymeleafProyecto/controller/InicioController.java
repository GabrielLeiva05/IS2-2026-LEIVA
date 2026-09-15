package com.todocodeacademy.ThymeleafProyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Gabriel
 */

@Controller
public class InicioController {
    
    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}
