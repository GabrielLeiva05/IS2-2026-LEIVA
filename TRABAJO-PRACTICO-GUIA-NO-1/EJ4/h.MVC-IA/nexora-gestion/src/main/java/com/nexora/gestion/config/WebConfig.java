package com.nexora.gestion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ============================================================================
 * CONFIGURACIÓN "WebConfig" (Capa de Configuración de Spring MVC)
 * ============================================================================
 * @Configuration marca esta clase como fuente de definición de Beans.
 * Al implementar {@link WebMvcConfigurer}, Spring nos permite personalizar
 * aspectos de Spring MVC (interceptores, recursos estáticos, formateadores,
 * etc.) sin tener que reemplazar toda la autoconfiguración por defecto que
 * trae Spring Boot.
 * ============================================================================
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Registramos el interceptor de sesión para TODAS las rutas; el
        // propio interceptor decide internamente si la ruta requiere
        // autenticación (ver SesionInterceptor.preHandle).
        registry.addInterceptor(new SesionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/webjars/**");
    }
}
