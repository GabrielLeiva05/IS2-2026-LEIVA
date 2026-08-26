package com.nexora.gestion.config;

import com.nexora.gestion.model.Administrador;
import com.nexora.gestion.model.Producto;
import com.nexora.gestion.model.TipoMovimiento;
import com.nexora.gestion.repository.AdministradorRepository;
import com.nexora.gestion.repository.ProductoRepository;
import com.nexora.gestion.service.AdministradorService;
import com.nexora.gestion.service.InventarioService;
import com.nexora.gestion.service.ProductoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

/**
 * ============================================================================
 * "DataInitializer" (Capa de Configuración) — Carga de datos de ejemplo
 * ============================================================================
 * Implementa {@link CommandLineRunner}: Spring Boot ejecuta automáticamente
 * el método run(...) una única vez, justo después de levantar todo el
 * contexto de la aplicación (útil para poblar datos de demostración).
 *
 * @Profile("!test") evita que este "seed" de datos se ejecute durante los
 * tests automatizados (ver src/test), donde normalmente se prefiere una
 * base de datos limpia y controlada por cada test.
 *
 * Se apoya en los Services (no en los Repository directamente) para que los
 * datos de ejemplo respeten exactamente las mismas reglas de negocio que
 * respetaría un usuario real usando la aplicación (contraseñas hasheadas,
 * validaciones, etc.).
 * ============================================================================
 */
@Configuration
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final AdministradorService administradorService;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final InventarioService inventarioService;

    public DataInitializer(AdministradorRepository administradorRepository,
                            AdministradorService administradorService,
                            ProductoRepository productoRepository,
                            ProductoService productoService,
                            InventarioService inventarioService) {
        this.administradorRepository = administradorRepository;
        this.administradorService = administradorService;
        this.productoRepository = productoRepository;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    @Override
    public void run(String... args) {
        crearAdministradorDemoSiNoExiste();
        crearProductosDemoSiNoExisten();
    }

    private void crearAdministradorDemoSiNoExiste() {
        if (administradorRepository.count() == 0) {
            Administrador admin = new Administrador();
            admin.setNombre("Ana");
            admin.setApellido("García");
            admin.setDocumento(30111222);
            admin.setCorreo("admin@nexora.com");
            admin.setPassword("Admin123!"); // Se hashea dentro del Service.
            admin.setNivelAcceso("SUPERADMIN");
            administradorService.registrar(admin);
            System.out.println(">>> Administrador demo creado: admin@nexora.com / Admin123!");
        }
    }

    private void crearProductosDemoSiNoExisten() {
        if (productoRepository.count() == 0) {
            Producto p1 = nuevoProducto("Notebook Nexora 14\"", "Notebook ultraliviana 14 pulgadas, 16GB RAM, SSD 512GB.", "899.99");
            Producto p2 = nuevoProducto("Mouse Inalámbrico", "Mouse ergonómico inalámbrico con sensor óptico de precisión.", "19.90");
            Producto p3 = nuevoProducto("Teclado Mecánico", "Teclado mecánico retroiluminado, switches rojos.", "54.50");
            Producto p4 = nuevoProducto("Monitor 27\" 4K", "Monitor IPS 27 pulgadas resolución 4K, 60Hz.", "329.00");

            inventarioService.registrarMovimiento(p1, 15, TipoMovimiento.ENTRADA);
            inventarioService.registrarMovimiento(p2, 50, TipoMovimiento.ENTRADA);
            inventarioService.registrarMovimiento(p3, 30, TipoMovimiento.ENTRADA);
            inventarioService.registrarMovimiento(p4, 10, TipoMovimiento.ENTRADA);

            System.out.println(">>> Catálogo demo de productos + stock inicial creado.");
        }
    }

    private Producto nuevoProducto(String nombre, String descripcion, String precio) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(new BigDecimal(precio));
        return productoService.registrarProducto(p);
    }
}
