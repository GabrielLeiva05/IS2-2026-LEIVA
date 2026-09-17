# LearnHub — Spring Boot MVC basado en el diagrama de clases

Proyecto adaptado a partir de la plantilla visual **LearnHub** y del diagrama de clases recibido.

## Tecnologías

- Java 21
- Spring Boot 3.5.5
- Spring MVC
- Thymeleaf
- Spring Data JPA / Hibernate ORM
- MySQL
- Spring Security
- BCrypt
- Hibernate Envers
- Bean Validation
- Spring Mail
- Lombok
- Maven

## 1. Modelo implementado

Se respetaron las clases del diagrama:

- `Profesor`
- `Grado`
- `Aula`
- `Alumno`
- `Materia`
- `Nota`

Y se agregaron las entidades necesarias para cumplir la nueva consigna:

- `Usuario`: credenciales de acceso.
- `Rol`: autorización.
- `AuditRevision`: cabecera de auditoría Envers.
- `BaseEntity`: auditoría técnica y optimistic locking.

### Relaciones

```text
Profesor 1 ----- 1 Usuario
Usuario  * ----- * Rol
Profesor 1 ----- * Materia
Materia  * ----- 1 Alumno
Materia 1 ----- * Nota
Grado 1 ----- 1..* Aula        (composición)
Aula 1..* ----- 1..* Alumno   (tal como indica el diagrama)
```

El mapeo clase por clase y relación por relación está documentado en `docs/mapeo-diagrama.md`.

**Importante:** la relación Aula-Alumno se implementó como `@ManyToMany` porque el diagrama adjunto marca `1..*` en ambos extremos. Aunque en muchos sistemas escolares se modelaría un alumno perteneciendo a un aula concreta, cambiar esa cardinalidad sería alterar el diagrama.

## 2. Registro de docentes

El formulario solicita:

- Nombre
- Apellido
- Especialidad
- Sexo
- Fecha de nacimiento
- Correo personal
- Contraseña
- Confirmación de contraseña

El correo personal es el **username** del sistema.

Al registrar:

1. se normaliza el correo a minúsculas;
2. se comprueba que sea único;
3. se valida la fecha;
4. se valida la confirmación de contraseña;
5. se genera un hash BCrypt;
6. se crea Usuario;
7. se asigna `DOCENTE`;
8. se crea Profesor;
9. se persiste todo dentro de una transacción;
10. después del commit se envía el correo de bienvenida.

## 3. Cambio de contraseña

El usuario debe informar:

- contraseña actual;
- nueva contraseña;
- confirmación.

El service comprueba la contraseña actual con `BCrypt.matches`, impide reutilizarla y guarda solamente el nuevo hash.

El campo `passwordHash` está marcado `@NotAudited`: no se almacenan hashes históricos innecesariamente.

## 4. Seguridad

### Autenticación

Spring Security usa login de formulario con sesión porque el proyecto es MVC + Thymeleaf.

### Autorización

- `ROLE_DOCENTE`: área privada del docente.
- `ROLE_ADMIN`: gestión administrativa y consulta de auditoría.

### CSRF

Se mantiene habilitado y los formularios Thymeleaf incluyen el token CSRF.

### Cookies

La sesión usa `HttpOnly` y `SameSite=Lax`.

### Contraseñas

Se usa `BCryptPasswordEncoder`. BCrypt es hashing, no cifrado reversible.

## 5. Auditoría

Hay dos niveles.

### Auditoría técnica

`BaseEntity` agrega:

- `createdAt`
- `updatedAt`
- `createdBy`
- `updatedBy`
- `version`

### Auditoría histórica

Hibernate Envers genera tablas `_AUD` y `audit_revision`.

`AuditRevisionListener` guarda el email del usuario autenticado que produjo la revisión.

Los controllers administrativos permiten consultar historial de Profesor, Alumno y Materia.

## 6. Validaciones

### Profesor

- nombre y apellido obligatorios;
- especialidad obligatoria;
- correo válido y único;
- contraseña entre 8 y 72 caracteres;
- fecha de nacimiento pasada;
- confirmación de contraseña.

### Alumno

- nombre y apellido obligatorios;
- fecha de nacimiento pasada;
- no se asocian alumnos dados de baja.

### Nota

- valor entre 0 y 10;
- fecha no futura;
- materia existente y activa.

### Grado/Aula/Materia

Se controlan campos obligatorios, tamaños máximos y existencia de las entidades relacionadas.

## 7. Bajas lógicas

El diagrama contiene `eliminado` en Profesor, Alumno y Materia.

No se elimina físicamente el registro: se establece `eliminado=true`.

Esto conserva el historial académico y evita romper relaciones con Notas.

## 7.1 Vistas y flujos disponibles

La versión original sólo exponía registro/login de un profesor. Se agregaron
todas las vistas necesarias para cubrir el CRUD que ya existía en la capa de
servicios pero no tenía controller/HTML:

**Área ADMIN (`/gestion`, protegida por rol `ADMIN`):**

- Dashboard con contadores y accesos a cada módulo.
- Profesores: listado (activos y dados de baja), edición, baja lógica y
  vista de "materias dictadas" por cada uno (equivalente a
  `listarClasesDictadas()` del diagrama).
- Grados y aulas: alta de grado, listado de aulas por grado, alta de aula,
  detalle de aula con listado de alumnos asociados y formulario para
  asociar un alumno activo (`Aula.asociarAlumno`).
- Alumnos: listado, alta y edición (incluye checkboxes para la relación
  muchos-a-muchos Aula-Alumno), baja lógica.
- Materias: listado, alta, edición, baja lógica y notas por materia
  (listado + alta de nota, con selector de materia en lugar de un id
  numérico).
- Notas: alta y edición, siempre asociadas a una materia existente.

**Área DOCENTE (`/docente`, protegida por rol `DOCENTE`):**

- "Mis materias": lista las materias que dicta el profesor autenticado
  (`listarClasesDictadas()`).
- Por cada materia propia: ver historial de notas y cargar una nueva.
- Edición de una nota propia.

La propiedad de cada materia/nota se valida en el propio controller
(`DocenteController`) contra el profesor autenticado en cada request, y el
`materiaId` nunca se toma del formulario para las operaciones de edición:
siempre se recalcula en el servidor a partir de la ruta o del registro
existente, para que un docente no pueda cargar o modificar notas de
materias ajenas manipulando el POST.

## 8. Composición Grado-Aula

El diagrama utiliza composición. En JPA se representa mediante:

```java
@OneToMany(mappedBy = "grado", cascade = CascadeType.ALL, orphanRemoval = true)
```

De esta manera el ciclo de vida del Aula queda ligado al Grado.

## 9. DTOs

Los DTO evitan que las entidades JPA lleguen directamente a la web.

Ejemplos:

- `RegistroProfesorDTO`
- `ProfesorDTO`
- `ProfesorFormDTO`
- `GradoDTO`
- `AulaDTO`
- `AlumnoDTO`
- `MateriaDTO`
- `NotaDTO`
- `CambiarPasswordDTO`
- `AuditEntryDTO`

Las asociaciones se representan con IDs, no con entidades completas.

## 10. Capas

```text
controller/
    Entrada HTTP, validación, Model y navegación.

dto/
    Contratos de datos entre capas.

service/
    Reglas de negocio y transacciones.

repository/
    Persistencia mediante Spring Data JPA.

entity/
    Modelo ORM y relaciones.

security/
    UserDetailsService y auditor actual.

config/
    Security, auditoría JPA y datos iniciales.
```

## 11. Configuración MySQL

Crear la base:

```sql
CREATE DATABASE learnhub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Configurar variables de entorno:

```text
DB_USERNAME=root
DB_PASSWORD=tu_password
```

La aplicación usa `spring.jpa.hibernate.ddl-auto=update` para crear/actualizar tablas automáticamente.

## 12. SMTP

Para cumplir el correo de bienvenida configurar:

```text
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=...
MAIL_PASSWORD=...
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS=true
```

No colocar credenciales SMTP dentro del repositorio.

## 13. Administrador inicial

Opcionalmente:

```text
ADMIN_EMAIL=admin@learnhub.local
ADMIN_PASSWORD=una-password-segura
```

En el primer arranque se crea el usuario ADMIN si todavía no existe.

No hay contraseña administrativa hardcodeada.

## 14. Ejecución

Con Maven:

```bash
mvn clean spring-boot:run
```

Luego:

```text
http://localhost:8080
```

## 15. Estructura

```text
src/main/java/com/learnhub
├── config
├── controller
├── dto
├── entity
├── enumeration
├── exception
├── repository
├── security
└── service

src/main/resources
├── static
├── templates
└── application.properties

database
└── 01-create-database.sql

docs
├── arquitectura.md
├── diagrama-clases.puml
└── plantilla-utilizada.md
```

## 16. Comentarios del código

Las clases principales contienen comentarios explicando:

- responsabilidad de la clase;
- motivo de cada relación JPA;
- propósito de DTO;
- reglas de negocio;
- motivo de BCrypt;
- funcionamiento de auditoría;
- motivo de `@Version`;
- razón de `AFTER_COMMIT` para correo;
- diferencia entre seguridad y datos de negocio.

## 17. Decisiones adicionales incorporadas

### Optimistic locking (`@Version`)
Evita que dos operaciones concurrentes sobrescriban cambios sin detectarlo.

### Baja lógica
Respeta el atributo `eliminado` del diagrama y conserva historial.

### `@NotAudited` para passwordHash
La auditoría de contraseñas no necesita guardar hashes anteriores. Se registra la modificación de la cuenta, pero no se conserva material sensible innecesariamente.

### Evento `AFTER_COMMIT`
Desacopla correo de la transacción y evita enviar una bienvenida si el registro terminó en rollback.

### Separación Usuario/Profesor
Permite que seguridad y dominio evolucionen independientemente y evita que la entidad de negocio contenga credenciales.


## Compatibilidad con JDK 25 y Lombok

El proyecto compila con **Java 21 (`--release 21`)**, pero puede ejecutarse con un JDK 25 instalado en el equipo. Se fijó Lombok en **1.18.48** y se declaró explícitamente como `annotationProcessor` de Maven. Esto es importante porque las versiones anteriores de Lombok no incorporaban soporte completo para JDK 25; la documentación oficial de Lombok indica soporte de JDK 25 desde la rama 1.18.40.
