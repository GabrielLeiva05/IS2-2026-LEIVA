package com.furnish.erp.config;

import com.furnish.erp.domain.enums.Rol;
import com.furnish.erp.dto.UsuarioForm;
import com.furnish.erp.repository.UsuarioRepository;
import com.furnish.erp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * DataInitializer
 * ============================================================================
 * Implementa CommandLineRunner: Spring Boot ejecuta automáticamente el
 * método run() una única vez, justo después de que el contexto de la
 * aplicación terminó de inicializarse (arranque de la app).
 *
 * Se usa para garantizar que exista al menos un usuario ADMIN con el cual
 * poder ingresar al sistema la primera vez que se levanta el proyecto,
 * evitando el problema del "huevo y la gallina" (sin un usuario cargado,
 * nadie podría loguearse para crear el primer usuario desde la UI).
 *
 * Las credenciales se toman de application.yml (app.admin.username /
 * app.admin.password), que a su vez pueden sobrescribirse con variables de
 * entorno (ADMIN_USERNAME / ADMIN_PASSWORD) para no dejar credenciales fijas
 * "hardcodeadas" en el código fuente.
 * ============================================================================
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByUsername(adminUsername)) {
            log.info("Usuario administrador '{}' ya existe, no se vuelve a crear.", adminUsername);
            return;
        }

        UsuarioForm form = new UsuarioForm();
        form.setUsername(adminUsername);
        form.setPassword(adminPassword);
        form.setRol(Rol.ADMIN);
        form.setHabilitado(true);

        usuarioService.registrarUsuario(form);
        log.info("============================================================");
        log.info(" Usuario administrador inicial creado.");
        log.info(" Usuario : {}", adminUsername);
        log.info(" Clave   : {} (cambiarla en producción)", adminPassword);
        log.info("============================================================");
    }
}
