package com.learnhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Navegación pública principal de la plantilla LearnHub. */
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() { return "index"; }
}
