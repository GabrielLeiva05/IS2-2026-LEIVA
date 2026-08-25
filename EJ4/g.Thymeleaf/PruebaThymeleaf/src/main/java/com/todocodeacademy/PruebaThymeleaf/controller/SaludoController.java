package com.todocodeacademy.PruebaThymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



/**
 *
 * @author Gabriel
 */

@Controller
public class SaludoController {
    
    @GetMapping("/saludo")
    public String saludo() {
        return "saludo";
    }
}
