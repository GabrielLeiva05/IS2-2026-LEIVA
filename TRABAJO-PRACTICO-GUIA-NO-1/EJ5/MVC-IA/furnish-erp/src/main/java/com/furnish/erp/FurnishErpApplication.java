package com.furnish.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * Clase principal de arranque de la aplicación Spring Boot.
 * ============================================================================
 * La anotación {@link SpringBootApplication} es una anotación "combo" que
 * agrupa tres anotaciones:
 *   - @Configuration     : la clase puede declarar beans de Spring.
 *   - @EnableAutoConfiguration : Spring Boot configura automáticamente
 *         Tomcat embebido, Thymeleaf, JPA/Hibernate, Spring Security, etc.
 *         en base a las dependencias presentes en el pom.xml (auto-config).
 *   - @ComponentScan     : escanea todos los paquetes debajo de
 *         com.furnish.erp buscando @Controller, @Service, @Repository,
 *         @Configuration, @Component, etc. y los registra como Beans.
 *
 * Al ejecutar `SpringApplication.run(...)` se levanta el contenedor de
 * Spring (ApplicationContext) y un servidor Tomcat embebido en el puerto
 * configurado en application.yml (por defecto 8080).
 * ============================================================================
 */
@SpringBootApplication
public class FurnishErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurnishErpApplication.class, args);
    }
}
