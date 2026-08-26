# Nexora Gestión

Sistema de gestión de **Usuarios, Administradores, Productos, Inventario y
Compras**, construido a partir del diagrama de clases de diseño provisto,
con arquitectura **MVC** (Modelo–Vista–Controlador):

- **Modelo**: Spring Data JPA (Hibernate) como ORM sobre **MySQL**.
- **Vista**: **Thymeleaf**, reutilizando el diseño visual de la plantilla
  Bootstrap 5 "Nexora".
- **Controlador**: Spring MVC (`@Controller`), controladores "delgados" que
  solo orquestan llamadas a la capa de Servicio.
- **Reglas de negocio**: viven **exclusivamente** en la capa `service`
  (interfaces + `*ServiceImpl`). El controlador y la vista nunca deciden
  nada de negocio.

Todo el código está comentado en detalle: cada clase explica su rol dentro
de la arquitectura, qué anotación de Spring/JPA se está usando y por qué, y
cómo se traduce cada elemento del diagrama UML a código.

---

## 1. Estructura del proyecto

```
nexora-gestion/
├── pom.xml
├── docker-compose.yml          <- Levanta MySQL local en un contenedor
├── src/main/java/com/nexora/gestion/
│   ├── NexoraGestionApplication.java
│   ├── model/          <- Entidades JPA (Persona, Usuario, Administrador,
│   │                       Producto, Inventario, Compra, DetalleCompra,
│   │                       EstadoUsuario, TipoMovimiento)
│   ├── repository/     <- Interfaces JpaRepository (ORM / acceso a datos)
│   ├── service/        <- Interfaces de reglas de negocio
│   │   └── impl/       <- Implementaciones (donde vive la lógica real)
│   ├── controller/     <- Controladores Spring MVC (@Controller)
│   ├── dto/             <- Formularios (LoginForm, ProductoForm, etc.)
│   ├── exception/      <- Excepciones de negocio + manejador global
│   └── config/          <- Interceptor de sesión, WebConfig, seed de datos
└── src/main/resources/
    ├── application.properties
    ├── templates/       <- Vistas Thymeleaf (.html)
    │   ├── fragments/layout.html  <- navbar/footer/head reutilizados
    │   ├── productos/, inventario/, usuarios/, compras/
    └── static/css, static/js     <- Estilos de la plantilla "Nexora"
```

## 2. Cómo se traduce el diagrama UML al código

| Elemento del UML                         | Dónde vive en el código                                            |
|-------------------------------------------|----------------------------------------------------------------------|
| Clase `Persona` (abstracta)                | `model/Persona.java` (`@MappedSuperclass`)                          |
| `Usuario extends Persona`                  | `model/Usuario.java` (`@Entity`)                                    |
| `Administrador extends Persona`            | `model/Administrador.java` (`@Entity`)                              |
| `ENUM estadoUsuario`                       | `model/EstadoUsuario.java`                                          |
| `Usuario.iniciarSesion()`, `sumarIntentoFallido()`, `bloquear()`, `resetearIntentos()` | Mutadores simples en `model/Usuario.java`; la **regla** (máx. 3 intentos, verificación de password) vive en `service/impl/UsuarioServiceImpl.java` |
| `Administrador.desbloquearUsuario()`, `gestionarUsuario()` | `service/impl/AdministradorServiceImpl.java` (orquesta a `UsuarioService`) |
| `Producto.registrarProducto()/editarProducto()/eliminarProducto()` | `service/impl/ProductoServiceImpl.java`                              |
| `Compra.registrarCompra()/agregarDetalle()/anularCompra()` | `service/impl/CompraServiceImpl.java` (transaccional, coordina Inventario) |
| `DetalleCompra.disminuirInventario()`      | Invocado dentro de `CompraServiceImpl.agregarDetalle()`, delega en `InventarioService.registrarMovimiento(SALIDA)` |
| `Inventario.registrarMovimiento()`         | `service/impl/InventarioServiceImpl.java`                            |
| Composición `Compra ◆── DetalleCompra`     | `@OneToMany(cascade=ALL, orphanRemoval=true)` en `model/Compra.java` |

Se documentaron explícitamente, dentro del propio código, dos pequeñas
**extensiones necesarias** sobre el diagrama original (no alteran su
espíritu, solo completan detalles de implementación no especificados):
- `Producto.precio` (necesario para poder calcular `Compra.precioTotal`).
- `TipoMovimiento` (ENTRADA/SALIDA) en `Inventario`, para poder diferenciar
  una reposición de stock de una venta.

## 3. Requisitos previos

- **Java 17** o superior.
- **Maven 3.9+** (o usar el soporte de Maven integrado de tu IDE:
  IntelliJ IDEA, Eclipse/STS o VS Code con la extensión "Extension Pack for
  Java" lo detectan automáticamente al abrir la carpeta del proyecto).
- **Docker** (opcional, pero muy recomendado para no instalar MySQL a mano).
  Si ya tenés un MySQL propio corriendo, podés saltar el paso de Docker y
  ajustar `application.properties`.

## 4. Levantar la base de datos MySQL

**Opción A — con Docker (recomendada):**

```bash
cd nexora-gestion
docker compose up -d
```

Esto crea un contenedor MySQL 8 con:
- Base de datos: `nexora_gestion`
- Usuario: `nexora`
- Contraseña: `nexora`
- Puerto: `3306`

Estos valores ya coinciden con `src/main/resources/application.properties`,
así que no hace falta tocar nada más.

**Opción B — con un MySQL propio ya instalado:**

Creá la base de datos y el usuario manualmente:

```sql
CREATE DATABASE nexora_gestion;
CREATE USER 'nexora'@'%' IDENTIFIED BY 'nexora';
GRANT ALL PRIVILEGES ON nexora_gestion.* TO 'nexora'@'%';
FLUSH PRIVILEGES;
```

O bien editá `spring.datasource.url`, `spring.datasource.username` y
`spring.datasource.password` en `application.properties` para que apunten a
tu instancia existente.

## 5. Compilar y ejecutar la aplicación

Desde la carpeta raíz del proyecto (`nexora-gestion/`):

```bash
mvn spring-boot:run
```

o, si preferís compilar el `.jar` y ejecutarlo aparte:

```bash
mvn clean package
java -jar target/nexora-gestion.jar
```

Al arrancar vas a ver en la consola:
- Los logs de Hibernate creando automáticamente las tablas en MySQL
  (`ddl-auto=update`).
- El mensaje `>>> Administrador demo creado: admin@nexora.com / Admin123!`
- El mensaje `>>> Catálogo demo de productos + stock inicial creado.`

(Estos datos de ejemplo los genera `config/DataInitializer.java` la primera
vez que se ejecuta, solo si las tablas están vacías.)

**Alternativa sin línea de comandos:** abrí la carpeta en IntelliJ IDEA /
Eclipse / VS Code, dejá que el IDE importe el proyecto Maven, y ejecutá
directamente la clase `NexoraGestionApplication` (botón "Run").

## 6. Probar la aplicación

Abrí el navegador en **http://localhost:8080**

### Como Usuario (cliente que compra)
1. Hacé clic en **Registrarme** y creá una cuenta.
2. Iniciá sesión con tu cuenta nueva.
3. Andá a **Mi Carrito**, elegí un producto y una cantidad, y agregalo.
4. Confirmá la compra. Vas a ver el descuento de stock reflejado
   automáticamente en el panel de administración (Inventario).
5. En **Mis Compras** podés ver tu historial y anular una compra (esto
   repone el stock).
6. Probá equivocarte la contraseña 3 veces al iniciar sesión: la cuenta se
   bloquea automáticamente (regla de negocio en `UsuarioServiceImpl`).

### Como Administrador
1. Iniciá sesión en **/login** eligiendo el tipo de cuenta "Administrador",
   con las credenciales demo:
   - Correo: `admin@nexora.com`
   - Contraseña: `Admin123!`
2. En **Productos** podés dar de alta, editar o eliminar productos.
3. En **Inventario** podés ver el historial de movimientos y registrar
   nuevas entradas de stock.
4. En **Usuarios** podés ver todos los usuarios registrados, bloquearlos,
   desbloquearlos (esto reutiliza al usuario bloqueado por 3 intentos
   fallidos del punto anterior) o eliminarlos.

## 7. Notas de diseño y decisiones tomadas

- **Autenticación propia por sesión (`HttpSession`) en vez de Spring
  Security completo**: se optó por este enfoque para que el flujo de
  `iniciarSesion()`, `sumarIntentoFallido()`, `bloquear()` y
  `resetearIntentos()` del diagrama UML sea explícito y esté 100%
  implementado en la capa de Servicio, tal como lo pide la consigna, en
  lugar de delegarlo en la cadena de filtros de un framework de seguridad.
  Las contraseñas igualmente se hashean con **BCrypt**
  (`spring-security-crypto`).
- **Borrado lógico vs. físico**: `Producto` y `Compra` se dan de baja
  lógicamente (`activo=false` / `anulada=true`) cuando tienen historial
  asociado, para no romper la integridad referencial ni perder
  trazabilidad — decisión típica en sistemas de gestión reales.
- **Inventario como libro de movimientos (ledger)**: en vez de guardar un
  contador de stock que se pueda desincronizar, el stock actual siempre se
  calcula sumando/restando los movimientos históricos (`InventarioRepository
  .calcularStockActual`).

## 8. Comandos útiles

```bash
# Ver logs del contenedor de MySQL
docker logs -f nexora-mysql

# Apagar la base de datos (conserva los datos en el volumen)
docker compose stop

# Apagar y borrar todo (incluye los datos)
docker compose down -v

# Ejecutar los tests
mvn test
```
