# Pruebas JMeter - Club Gestión

Los planes están preparados para la aplicación final con Spring Security.

Credenciales de prueba: `admin / admin123`.

Antes de ejecutar:
1. Levantar la aplicación en `http://localhost:8080`.
2. Tener MySQL y la base `club_gestion` configuradas.
3. Abrir cada `.jmx` en Apache JMeter.

Planes:
- `functional.jmx`: flujo funcional autenticado, 1 usuario.
- `load.jmx`: carga, 10 usuarios y ramp-up de 10 s.
- `stress.jmx`: stress, 100 usuarios y ramp-up de 90 s.

Cada plan obtiene el token CSRF de `/login`, inicia sesión y mantiene la cookie de sesión para las llamadas API.

## Correcciones aplicadas (revisión QA)

1. **Bug reportado**: el elemento `HTTP Request Defaults` (`ConfigTestElement`) no tenía la propiedad `HTTPsampler.Arguments`, obligatoria en JMeter 5.6.x para ese componente. Esto disparaba el warning/error `Property HTTPsampler.Arguments is unset for element ...ConfigTestElement@...` al abrir o ejecutar los planes. Se agregó el `elementProp` vacío correspondiente en los tres `.jmx`.
2. **Thread Group desalineado con la estrategia documentada**: `load.jmx` y `stress.jmx` tenían 1 usuario / 1 s de ramp-up (una copia literal de `functional.jmx`), lo que no reflejaba lo descrito en `docs/estrategia.md`. Se corrigió a:
   - `load.jmx`: 10 usuarios, ramp-up 10 s.
   - `stress.jmx`: 100 usuarios, ramp-up 90 s.
3. **Sin validaciones de resultado**: ningún sampler verificaba el código de respuesta, por lo que un flujo roto (ej. login fallido) podía pasar inadvertido. Se agregó un `Response Assertion` por request clave (200 para GET, 200/302/303 para el login, 200/201 para las creaciones).
4. **Sin listeners**: se agregaron `Summary Report` e `Informe Agregado` (y `Ver Resultados en Árbol` solo en `functional.jmx`, por ser de 1 usuario) para poder revisar resultados sin configurar nada manualmente. Los `.jtl` se generan en la misma carpeta donde se ejecute JMeter.

Con esto los tres planes abren y corren sin el error de `HTTPsampler.Arguments`, y `load`/`stress` ya generan la carga real que describen sus nombres.
