# Arquitectura

El proyecto utiliza una arquitectura por capas:

- **Entity:** representa el estado persistente. No contiene reglas de negocio.
- **Repository:** acceso a datos mediante Spring Data JPA.
- **Service:** única capa responsable de reglas de negocio y validaciones de dominio.
- **Controller REST:** API JSON para JMeter y clientes externos.
- **Controller MVC:** endpoints para las vistas Thymeleaf.
- **DTO:** objetos de entrada y salida de la API.
- **Templates:** interfaz web.

## Regla aplicada

Las entidades no implementan `registrar`, `editar`, `eliminar`, cálculos de duración ni validaciones de negocio. Por ejemplo, el cálculo de duración de una permanencia se encuentra en `AccesoService` y la validación de pagos en `PagoService`.

## Relaciones

- `Persona` es superclase de `Socio` mediante `JOINED`.
- Una `Persona` puede tener cero o una `Imagen`.
- Una `Persona` puede tener muchos `RegistroAcceso`.
- Un `GrupoFamiliar` tiene un `Socio` titular.
- Un `GrupoFamiliar` puede tener muchas `Persona` como familiares.
- Un `GrupoFamiliar` puede tener muchos `Pago`.

## Cuotas

Cada `Pago` contiene el período `YYYY-MM`, importe, fecha, medio, estado y referencia. La combinación `(grupo_familiar_id, periodo)` tiene una restricción única en la base de datos.

Para `TRANSFERENCIA` y `MERCADO_PAGO` la referencia es obligatoria. Para `EFECTIVO` es opcional.
