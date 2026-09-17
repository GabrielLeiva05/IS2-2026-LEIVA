package com.club.controller;
import com.club.service.RevisionService; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.GetMapping; import java.util.*;
@Controller @RequiredArgsConstructor public class AuditoriaController { private final RevisionService service; @GetMapping("/auditoria") public String listar(Model model){model.addAttribute("revisiones",service.listar());return "auditoria/lista";} }
