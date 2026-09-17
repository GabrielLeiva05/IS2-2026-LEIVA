package com.learnhub.controller;

import com.learnhub.dto.RegistroProfesorDTO;
import com.learnhub.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/** Controller público de login y registro. */
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final ProfesorService profesorService;

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("registro", new RegistroProfesorDTO(null, null, null, null, null, null, null, null));
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroProfesorDTO dto,
                            BindingResult result, Model model) {
        if (result.hasErrors()) return "auth/registro";
        try {
            profesorService.registrarProfesor(dto);
            return "redirect:/registro/exito";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/registro";
        }
    }

    @GetMapping("/registro/exito")
    public String registroExito() { return "auth/registro-exito"; }
}
