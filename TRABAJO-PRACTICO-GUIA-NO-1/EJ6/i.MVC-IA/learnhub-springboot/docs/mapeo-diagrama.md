# Mapeo exacto del diagrama de clases

## Clases originales

| Diagrama | Java | Tabla |
|---|---|---|
| Profesor | `Profesor` | `profesores` |
| Grado | `Grado` | `grados` |
| Aula | `Aula` | `aulas` |
| Alumno | `Alumno` | `alumnos` |
| Materia | `Materia` | `materias` |
| Nota | `Nota` | `notas` |

## Atributos

- Profesor: `id`, `nombre`, `apellido`, `especialidad`, `eliminado` + datos solicitados para autenticación (`sexo`, `fechaNacimiento`) y vínculo con `Usuario`.
- Grado: `id`, `nivel`.
- Aula: `id`, `division`.
- Alumno: `id`, `nombre`, `apellido`, `fechaNacimiento`, `eliminado`.
- Materia: `id`, `nombre`, `eliminado`.
- Nota: `id`, `fecha`, `valor`.

`id` es `Long` en Java en lugar de `int` del dibujo porque es una representación JPA más apropiada para claves autogeneradas y no cambia el significado del modelo.

## Relaciones

### Profesor-Materia

El diagrama marca `1` del lado de Profesor y `*` del lado de Materia. Se implementa:

```java
Profesor 1 ---- * Materia
```

con `@OneToMany(mappedBy = "profesor")` y `@ManyToOne`.

### Grado-Aula

El rombo negro representa composición. Se implementa:

```java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
```

El Aula posee `@ManyToOne` hacia Grado.

### Aula-Alumno

El dibujo muestra `1..*` en ambos extremos. Por eso se conserva literalmente como ManyToMany con tabla `aula_alumno`.

### Materia-Alumno

El dibujo marca `*` junto a Materia y `1` junto a Alumno. Se implementa como muchas Materias asociadas a un Alumno, mediante `@ManyToOne` desde Materia.

### Materia-Nota

El dibujo marca `1` junto a Materia y `*` junto a Nota. Se implementa como `@OneToMany` / `@ManyToOne`.

## Clases agregadas por el nuevo requerimiento

### Usuario

Es necesaria porque el enunciado exige ingreso mediante correo y contraseña, cambio de contraseña y autenticación. El correo personal del Profesor es el username.

### Rol

Es necesaria para autorización. Se incluyen `DOCENTE` y `ADMIN`.

### BaseEntity

No es una clase de negocio del diagrama. Es una superclase técnica para centralizar ID, auditoría y `@Version`.

### AuditRevision

Es infraestructura de Hibernate Envers para saber quién hizo cada modificación histórica.
