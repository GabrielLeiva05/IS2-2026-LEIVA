package com.furnish.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ============================================================================
 * PasswordEncoderConfig
 * ============================================================================
 * El bean {@link PasswordEncoder} se separó de {@link SecurityConfig} en su
 * propia clase de configuración, SIN NINGUNA dependencia, para evitar una
 * referencia circular:
 *
 *   SecurityConfig  --necesita-->  UsuarioService (para el login)
 *   UsuarioServiceImpl --necesita--> PasswordEncoder
 *
 * Si el PasswordEncoder se definiera como @Bean dentro de SecurityConfig,
 * Spring tendría que terminar de construir SecurityConfig (que depende de
 * UsuarioService) para poder darle el PasswordEncoder a UsuarioServiceImpl,
 * pero UsuarioServiceImpl es, a su vez, una dependencia de SecurityConfig:
 * un ciclo irresoluble. Al vivir en una clase aparte y sin dependencias,
 * PasswordEncoderConfig puede instanciarse primero sin problemas, rompiendo
 * el ciclo.
 * ============================================================================
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * BCrypt aplica un algoritmo de hashing adaptativo (con "salt"
     * incorporado) diseñado específicamente para contraseñas: es lento a
     * propósito para dificultar ataques de fuerza bruta. NUNCA se guarda ni
     * compara la contraseña en texto plano.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
