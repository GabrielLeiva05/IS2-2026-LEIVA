# Estrategia de testing

## Unitarias
Se cubren reglas de negocio de SocioService y PagoService con JUnit 5 + Mockito.

## Carga
10 usuarios concurrentes con ramp-up de 10 segundos. Se observa throughput, errores y percentiles.

## Stress
100 usuarios concurrentes con ramp-up de 90 segundos. Se observa degradación de rendimiento, errores HTTP, p95/p99 y throughput.

Los escenarios usan usuarios ADMIN porque las operaciones de escritura están protegidas por Spring Security.
