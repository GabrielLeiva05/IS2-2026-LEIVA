package com.learnhub.controller;

import com.learnhub.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/** Consultas de auditoría restringidas a ADMIN. */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuditService auditService;

    @GetMapping("/auditoria/profesor/{id}")
    public String profesor(@PathVariable Long id, Model model) {
        model.addAttribute("historial", auditService.historialProfesor(id));
        return "admin/auditoria";
    }

    @GetMapping("/auditoria/alumno/{id}")
    public String alumno(@PathVariable Long id, Model model) {
        model.addAttribute("historial", auditService.historialAlumno(id));
        return "admin/auditoria";
    }

    @GetMapping("/auditoria/materia/{id}")
    public String materia(@PathVariable Long id, Model model) {
        model.addAttribute("historial", auditService.historialMateria(id));
        return "admin/auditoria";
    }
}
