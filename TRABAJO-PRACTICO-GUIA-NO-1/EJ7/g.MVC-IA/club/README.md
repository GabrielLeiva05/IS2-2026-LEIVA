# Club Gestión - MVC + Thymeleaf + JPA/MySQL + DTO + Seguridad + Auditoría

Proyecto final de gestión de socios, familias, cuotas, accesos e imágenes.

## Arquitectura
- MVC con Spring MVC y Thymeleaf.
- Controller: HTTP, formularios y DTO.
- Service: **toda la lógica y reglas de negocio**.
- Repository: persistencia mediante Spring Data JPA/Hibernate ORM.
- Entidades: atributos, relaciones, constructores, getters/setters y anotaciones JPA/auditoría; no contienen reglas de negocio.
- Controllers y Services intercambian DTOs; las entidades quedan dentro de la capa de persistencia/servicio.

## MySQL
MySQL es el perfil predeterminado.
1. Ejecutar `database/01-create-database.sql`.
2. Configurar `DB_USERNAME` y `DB_PASSWORD`.
3. Ejecutar `mvn clean spring-boot:run`.

Para pruebas rápidas existe H2: `mvn spring-boot:run -Dspring-boot.run.profiles=h2`.

## Seguridad
Spring Security + formulario de login + BCrypt + roles `ADMIN` y `USER`.
Usuarios demo creados si no existen: `admin/admin123` (ADMIN) y `user/user123` (USER). Las contraseñas se almacenan como hashes BCrypt.

## Auditoría
Hibernate Envers audita Persona, Socio, GrupoFamiliar, Pago, RegistroAcceso e Imagen. Cada revisión guarda el usuario autenticado mediante `RevisionAuditoriaListener`. ADMIN puede consultar `/auditoria`.

## Pagos
Medios: EFECTIVO, TRANSFERENCIA y MERCADO_PAGO. Transferencia y Mercado Pago exigen referencia. No se permiten dos pagos para la misma familia y período.

## Testing
El ZIP separado contiene pruebas unitarias y planes JMeter funcional, carga y stress.
