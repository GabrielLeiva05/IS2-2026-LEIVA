package com.todocodeacademy.Sesiones.controller;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Gabriel
 */

@Controller
public class LoginController {
    //BBDD "Lógica"
    private Map<String, String> usuarios = new HashMap<>();

    public LoginController() {

        usuarios.put("Luisina", "1234");
        usuarios.put("TodoCode", "java");
        usuarios.put("Ibra", "perro");
        usuarios.put("Colapinto", "f1");
    }
    //Login
    @GetMapping("/login")
    public String mostrarLogin(){
        return "login";
    }
    //Inicio de Sesion
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String nombreUsuario,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        
        //Validar que el usuario existe
        if (usuarios.containsKey(nombreUsuario)) {
            //Validar la contraseña
            String contra = usuarios.get(nombreUsuario);
            if (contra.equals(password)) {
                session.setAttribute("usuarioLogueado", nombreUsuario);
                return "redirect:/bienvenida";
            }
        }
            
        // Si no existe el usuario o si falla la contraseña
        model.addAttribute("error", "Usuario o Contraseña incorrecta.");
        
        return "login";
    }
    //Pagina de bienvenida
    @GetMapping("/bienvenida")
    public String mostrarBienvenida(HttpSession session) {
        
        String usuario = (String) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        return "bienvenida";
    }
    //Cerrar Sesion
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
