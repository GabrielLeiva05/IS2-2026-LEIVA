package com.nexora.gestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * CLASE DE ARRANQUE (Entry point) DE LA APLICACIÓN
 * ============================================================================
 * La anotación {@link SpringBootApplication} es una anotación "compuesta" que
 * combina tres anotaciones:
 *
 *  - @Configuration   : indica que esta clase puede definir Beans de Spring.
 *  - @EnableAutoConfiguration : le dice a Spring Boot que configure
 *        automáticamente el contexto en base a las dependencias presentes en
 *        el classpath (por ejemplo, al detectar el starter de JPA + el driver
 *        de MySQL, configura automáticamente el DataSource, el
 *        EntityManagerFactory y el JpaTransactionManager).
 *  - @ComponentScan   : le dice a Spring que escanee este paquete (y los
 *        subpaquetes: config, model, repository, service, controller, dto,
 *        exception) en busca de clases anotadas con @Component, @Service,
 *        @Repository, @Controller, etc. para registrarlas como Beans.
 *
 * Al ejecutar el método main() se levanta un servidor Tomcat embebido y todo
 * el contexto de Spring (arquitectura MVC completa: Modelo -> Repository/JPA,
 * Vista -> Thymeleaf, Controlador -> @Controller).
 * ============================================================================
 */
@SpringBootApplication
public class NexoraGestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexoraGestionApplication.class, args);
    }

}
