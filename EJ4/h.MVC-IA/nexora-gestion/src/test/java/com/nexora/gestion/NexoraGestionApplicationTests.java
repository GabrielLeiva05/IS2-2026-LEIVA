package com.nexora.gestion;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de humo (smoke test): verifica que todo el contexto de Spring
 * (Controllers, Services, Repositories, configuración) se pueda levantar
 * sin errores de cableado de Beans (@Autowired, @Bean, etc.).
 *
 * @ActiveProfiles("test") evita que se ejecute el DataInitializer (que
 * requiere una base de datos real) durante este test — ver
 * @Profile("!test") en config/DataInitializer.java.
 *
 * NOTA: para que este test corra necesita, de todas formas, que
 * application.properties apunte a una base MySQL accesible (o bien
 * agregar una base H2 en memoria solo para tests, fuera del alcance de
 * este ejercicio). Se deja documentado como próximo paso de mejora.
 */
@SpringBootTest
@ActiveProfiles("test")
class NexoraGestionApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto de Spring no logra armarse, este test falla solo.
    }
}
