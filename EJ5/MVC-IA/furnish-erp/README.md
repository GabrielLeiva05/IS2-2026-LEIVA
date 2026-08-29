# Furnish ERP — Sistema de Gestión de Compras, Proveedores, Empleados y Stock

Aplicación web desarrollada en **Java + Spring Boot**, con **Thymeleaf** como
motor de vistas, **Spring Data JPA (Hibernate)** como ORM contra **MySQL**, y
**Spring Security** para el acceso mediante usuario y contraseña.

Implementa el diagrama de clases de diseño provisto (Empleado, Proveedor,
Producto, OrdenCompra, DetalleOrden, Stock), siguiendo la arquitectura
**MVC**, y adapta la identidad visual del template "Furnish" (negro + naranja,
tipografía Poppins) al panel de administración.

---

## 1. Arquitectura y capas del proyecto

```
com.furnish.erp
├── domain/              Entidades JPA (Modelo) + domain/enums
├── repository/          Interfaces Spring Data JPA (acceso a datos / ORM)
├── service/             Interfaces de servicio  (ÚNICA capa con reglas de negocio)
│   └── impl/            Implementaciones de los servicios
├── controller/          Controladores Spring MVC (capa Controlador)
├── dto/                 Objetos de transferencia para formularios complejos
├── config/              SecurityConfig (login) y DataInitializer (usuario admin inicial)
└── exception/           Excepciones de negocio + manejador global (@ControllerAdvice)

src/main/resources
├── templates/           Vistas Thymeleaf (capa Vista), una carpeta por módulo
├── static/css/          Hoja de estilos propia (adaptada del template Furnish)
└── application.yml      Configuración de datasource, JPA y Thymeleaf
```

**Regla de arquitectura aplicada:** todas las reglas de negocio (validaciones,
cálculos, transiciones de estado, control de stock) están encapsuladas
exclusivamente en la capa `service`. Los `controller` solo reciben la petición
HTTP y delegan; los `repository` solo acceden a datos; las entidades (`domain`)
son POJOs de persistencia sin lógica de negocio.

### Módulos funcionales
| Módulo | Rutas | Descripción |
|---|---|---|
| Empleados | `/empleados` | ABM con baja lógica |
| Proveedores | `/proveedores` | ABM con baja lógica |
| Productos | `/productos` | ABM con baja lógica + stock actual |
| Órdenes de Compra | `/ordenes` | Alta con líneas dinámicas + máquina de estados |
| Stock | `/stock` | Kardex de inventario (historial + ajustes manuales) |
| Usuarios | `/usuarios` | Alta/edición de credenciales de acceso (solo ADMIN) |

---

## 2. Login y seguridad (funcionalidad agregada)

El diagrama de clases original no incluía una entidad de autenticación. Por
pedido explícito, se agregó:

- Entidad `Usuario` (username, password hasheada con **BCrypt**, rol, y
  vínculo opcional a un `Empleado`).
- `SecurityConfig`: login por formulario (usuario y contraseña), rutas
  protegidas, y control de acceso por rol (`ADMIN` / `EMPLEADO`).
- `DataInitializer`: crea automáticamente un usuario **ADMIN** la primera vez
  que se levanta la aplicación (ver credenciales en la sección 4).

---

## 3. Supuestos y decisiones de diseño sobre el diagrama original

El diagrama entregado tenía algunos puntos ambiguos o incompletos para poder
implementarse tal cual. Se documentan aquí las decisiones tomadas, todas
razonadas y mínimas:

1. **Relación Producto–Stock–DetalleOrden**: los atributos de `Stock`
   (`idMovimiento`, `fecha`, `tipoMovimiento`, `stockActual`) describen un
   **Kardex de inventario** (historial de movimientos), no un registro único
   1 a 1 por producto. Se modeló como: `Producto (1) -- (0..*) Stock`
   (obligatorio) y `DetalleOrden (1) -- (0..*) Stock` (opcional, para
   trazabilidad de qué compra originó una entrada). Es el patrón estándar de
   la industria para este tipo de reglas de negocio.
2. **`DetalleOrden.disminuirStock()`**: se interpretó como el efecto de
   negocio que se dispara cuando una Orden de Compra pasa a estado
   `RECIBIDA`: por cada línea de detalle se **incrementa** el stock del
   producto correspondiente (recibir mercadería de un proveedor aumenta el
   inventario). La lógica vive en `OrdenCompraService.cambiarEstado()`, que
   invoca a `StockService.incrementarStock()`.
3. **Relación DetalleOrden–Producto**: el diagrama no dibuja explícitamente
   esta línea, pero el atributo `cantidadProducto` solo tiene sentido
   referenciando a un producto concreto; se agregó como `@ManyToOne`.
4. **Campo `nombre` en Proveedor**: se agregó porque un proveedor sin
   identificación no es operable en la práctica (listados, combos, etc.).
5. **`eliminador` → `eliminado`**: se corrigió el nombre del campo booleano
   de baja lógica en `Proveedor` por consistencia con el resto del modelo.
6. **IDs como `Long`** en vez de `int`: se usó `Long` (recomendación estándar
   de JPA/Hibernate para claves primarias autoincrementales), en lugar de
   `int`/`Integer` como figuraba en el diagrama.

---

## 4. Cómo levantar el proyecto

### Requisitos previos
- Docker y Docker Compose (opción recomendada, todo incluido), **o bien**
- JDK 21 + Maven 3.9+ + una instancia de MySQL 8+ accesible.

### Opción A — Todo con Docker Compose (recomendada)

Desde la raíz del proyecto (donde está `docker-compose.yml`):

```bash
docker compose up -d --build
```

Esto levanta dos contenedores:
- `furnish-erp-mysql`: MySQL (imagen `mysql:latest`), con la base de datos
  `furnish_erp` ya creada y persistida en un volumen Docker.
- `furnish-erp-app`: la aplicación Spring Boot, compilada dentro de un
  contenedor multi-stage (no requiere tener Maven/JDK instalado en la máquina
  host) y conectada automáticamente al contenedor de MySQL.

La aplicación queda disponible en **http://localhost:8080**

Para ver los logs de arranque (y confirmar la creación del usuario admin):
```bash
docker compose logs -f app
```

Para detener todo (conservando los datos):
```bash
docker compose down
```

Para detener y borrar también los datos de MySQL:
```bash
docker compose down -v
```

### Opción B — MySQL con Docker, aplicación con Maven local

Si preferís correr la aplicación directamente con Maven (por ejemplo, para
desarrollarla en tu IDE), podés levantar SOLO la base de datos con Docker:

```bash
docker compose up -d mysql
```

Y luego correr la aplicación con Maven (usa por defecto `localhost:3306`,
ver `application.yml`):

```bash
mvn spring-boot:run
```

O generar el JAR y ejecutarlo:
```bash
mvn clean package -DskipTests
java -jar target/furnish-erp.jar
```

### Variables de entorno disponibles (todas opcionales, con valor por defecto)

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `DB_HOST` | `localhost` | Host de MySQL |
| `DB_PORT` | `3306` | Puerto de MySQL |
| `DB_NAME` | `furnish_erp` | Base de datos |
| `DB_USER` | `furnish_user` | Usuario de MySQL |
| `DB_PASSWORD` | `furnish_pass` | Contraseña de MySQL |
| `ADMIN_USERNAME` | `admin` | Usuario administrador inicial del sistema |
| `ADMIN_PASSWORD` | `admin123` | Contraseña del administrador inicial |

---

## 5. Acceso al sistema

Una vez levantada la aplicación, ingresar a **http://localhost:8080** (redirige
automáticamente a `/login`).

**Usuario administrador creado automáticamente al primer arranque:**
```
Usuario:    admin
Contraseña: admin123
```

> ⚠️ Cambiar esta contraseña (o las variables `ADMIN_USERNAME`/`ADMIN_PASSWORD`
> antes del primer arranque) antes de usar el sistema en un entorno real.

Desde el menú **Usuarios** (solo visible para el rol ADMIN) se pueden crear
nuevos usuarios con rol `ADMIN` o `EMPLEADO`, vinculándolos opcionalmente a un
Empleado existente.

---

## 6. Notas técnicas adicionales

- **ORM**: `spring.jpa.hibernate.ddl-auto=update` — Hibernate crea/actualiza
  automáticamente el esquema de MySQL en base a las entidades `@Entity` al
  arrancar la aplicación. No es necesario ejecutar scripts SQL manualmente.
- **Contraseñas**: se almacenan siempre con hash **BCrypt** (nunca en texto
  plano), tanto para el usuario admin inicial como para los usuarios creados
  desde la UI.
- **Baja lógica**: Empleado, Proveedor y Producto no se eliminan físicamente
  de la base de datos (campo `eliminado`), preservando la integridad
  referencial e histórica de las órdenes de compra y movimientos de stock.
- **Manejo de errores**: `GlobalExceptionHandler` centraliza la traducción de
  excepciones de negocio (`BusinessException`) y de recursos no encontrados
  (`ResourceNotFoundException`) a vistas Thymeleaf amigables.
