# Decisiones de diseño

## Persona / Socio
`Persona` es la superclase y `Socio` hereda de ella. La estrategia JPA es `JOINED`: los atributos comunes se almacenan en `personas` y los específicos de socio en `socios`.

## Grupo familiar
El grupo tiene un `Socio` titular y una colección de `Persona` como familiares. La pertenencia se modela desde `Persona` hacia `GrupoFamiliar`, evitando una tabla intermedia innecesaria.

## Imagen
Se modela `Persona 1 — 0..1 Imagen`: una persona puede no tener foto o tener una sola imagen vigente. Es la cardinalidad semánticamente esperable para una imagen facial.

## Registro de acceso
Se modela `Persona 1 — 0..* RegistroAcceso`: una persona puede registrar múltiples entradas/salidas a lo largo del tiempo.

## Cuotas y pagos
La cuota mensual vive en `GrupoFamiliar.cuotaMensual`. Cada `Pago` representa el pago de una cuota de un período determinado. La combinación `grupo_familiar_id + periodo` es única para pagos.

El enum `MedioPago` contempla los tres medios pedidos: efectivo, transferencia y Mercado Pago.

## Mercado Pago
El sistema registra que el medio utilizado fue Mercado Pago. No integra una pasarela externa de cobro. En una futura integración, se recomienda introducir una interfaz/adaptador para no acoplar el dominio a la pasarela.
