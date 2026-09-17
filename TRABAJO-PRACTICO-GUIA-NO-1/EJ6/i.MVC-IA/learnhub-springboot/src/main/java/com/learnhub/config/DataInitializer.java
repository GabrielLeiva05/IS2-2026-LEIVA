package com.learnhub.config;

import com.learnhub.entity.Rol;
import com.learnhub.entity.Usuario;
import com.learnhub.repository.RolRepository;
import com.learnhub.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Crea roles obligatorios y, opcionalmente, un administrador inicial.
 *
 * <p>El admin sólo se crea si ADMIN_EMAIL y ADMIN_PASSWORD están definidos.
 * Nunca se deja una contraseña administrativa hardcodeada en el repositorio.</p>
 */
@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Bean
    ApplicationRunner initRolesYAdmin() {
        return args -> {
            Rol docente = rolRepository.findByNombre("DOCENTE")
                    .orElseGet(() -> rolRepository.save(new Rol("DOCENTE")));
            Rol admin = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> rolRepository.save(new Rol("ADMIN")));

            if (adminEmail != null && !adminEmail.isBlank()
                    && adminPassword != null && !adminPassword.isBlank()) {
                Usuario usuario = usuarioRepository.findByEmailIgnoreCase(adminEmail)
                        .orElseGet(() -> {
                            Usuario nuevo = new Usuario();
                            nuevo.setEmail(adminEmail.trim().toLowerCase());
                            return nuevo;
                        });
                usuario.setEmail(adminEmail.trim().toLowerCase());
                usuario.setPasswordHash(passwordEncoder.encode(adminPassword));
                usuario.setHabilitado(true);
                usuario.getRoles().add(admin);
                usuarioRepository.save(usuario);
            }
        };
    }
}
