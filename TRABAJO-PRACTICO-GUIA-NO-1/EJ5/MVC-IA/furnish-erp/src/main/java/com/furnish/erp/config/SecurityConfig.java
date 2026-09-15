package com.furnish.erp.config;

import com.furnish.erp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ============================================================================
 * Configuración de Spring Security: login con usuario y contraseña
 * ============================================================================
 * @Configuration : declara Beans de infraestructura de Spring.
 * @EnableWebSecurity : activa la cadena de filtros de seguridad de Spring
 *      Security sobre todas las peticiones HTTP.
 * @EnableMethodSecurity : habilita anotaciones de autorización a nivel de
 *      método (@PreAuthorize("hasRole('ADMIN')")) en los Controllers/Services.
 * ============================================================================
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;
    // Se inyecta el PasswordEncoder (definido en PasswordEncoderConfig, ver
    // esa clase para la explicación de por qué NO se declara @Bean acá).
    private final PasswordEncoder passwordEncoder;

    /**
     * Conecta Spring Security con nuestra fuente de usuarios (UsuarioService,
     * que implementa UserDetailsService) y le indica CÓMO comparar
     * contraseñas (con el PasswordEncoder inyectado).
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define la cadena de filtros de seguridad: qué rutas son públicas, qué
     * rutas requieren autenticación, qué rutas requieren un ROL específico,
     * y cómo se comporta el formulario de login/logout.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos (CSS/JS/imágenes) y la página de login
                // son accesibles sin autenticarse.
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/login").permitAll()
                // Solo ADMIN puede administrar usuarios del sistema.
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                // Solo ADMIN puede eliminar (baja lógica) empleados/proveedores/productos.
                .requestMatchers("/empleados/eliminar/**", "/proveedores/eliminar/**",
                                  "/productos/eliminar/**").hasRole("ADMIN")
                // El resto de las pantallas requiere estar autenticado
                // (ADMIN o EMPLEADO), sin distinción adicional de rol.
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")            // Vista Thymeleaf propia (no la de Spring por defecto)
                .loginProcessingUrl("/login")    // URL a la que se envía el POST del formulario
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );
            // Nota: la protección CSRF queda HABILITADA (comportamiento por
            // defecto de Spring Security), ya que esta es una aplicación MVC
            // clásica basada en formularios HTML. Todas las vistas Thymeleaf
            // usan th:action en sus <form>, lo que hace que Thymeleaf inserte
            // automáticamente el campo oculto con el token CSRF requerido.

        return http.build();
    }
}
