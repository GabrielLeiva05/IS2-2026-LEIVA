# Arquitectura LearnHub

La aplicación implementa MVC server-side y separa responsabilidades.

```text
Thymeleaf -> Controller -> DTO -> Service -> Repository -> JPA/Hibernate -> MySQL
                                      |
                                      +-> reglas de negocio
                                      +-> PasswordEncoder
                                      +-> eventos de correo
```

## Entidades del diagrama

- **Profesor**: mantiene nombre, apellido, especialidad, sexo, fecha de nacimiento y baja lógica. Se relaciona 1:1 con Usuario y 1:N con Materia.
- **Grado**: posee composición 1:N con Aula mediante cascade + orphanRemoval.
- **Aula**: pertenece a un Grado y mantiene la relación M:N con Alumno indicada por el diagrama.
- **Alumno**: datos personales y baja lógica.
- **Materia**: pertenece a un Profesor y a un Alumno; posee Notas.
- **Nota**: fecha, valor y Materia.
- **Usuario/Rol**: entidades agregadas para autenticación/autorización, porque la consigna exige credenciales y roles.

## DTO

Los controllers nunca reciben entidades JPA desde formularios ni las colocan directamente en el Model. Los DTO transportan solamente los datos necesarios.

## Reglas de negocio

- correo personal único y normalizado a minúsculas;
- contraseña mínima de 8 caracteres;
- confirmación obligatoria;
- BCrypt para el hash;
- fecha de nacimiento no futura;
- nota entre 0 y 10;
- fecha de nota no futura;
- no se pueden asociar profesores/alumnos dados de baja;
- bajas de Profesor, Alumno y Materia son lógicas.

## Seguridad

- Spring Security con sesión;
- login mediante email;
- roles `DOCENTE` y `ADMIN`;
- CSRF activo;
- cookies HttpOnly + SameSite=Lax;
- BCrypt;
- área de auditoría restringida a ADMIN.

## Auditoría

Spring Data JPA registra `createdAt`, `updatedAt`, `createdBy`, `updatedBy`. Hibernate Envers conserva revisiones históricas y un `AuditRevision` identifica al usuario responsable.

El hash de contraseña está marcado `@NotAudited` para no conservar hashes anteriores innecesariamente.

## Correo

El registro publica `ProfesorRegistradoEvent`. `WelcomeEmailListener` usa `@TransactionalEventListener(AFTER_COMMIT)`, por lo que el correo sólo se intenta enviar una vez confirmada la transacción de alta.

## Concurrencia

`BaseEntity.version` usa `@Version` para optimistic locking.
