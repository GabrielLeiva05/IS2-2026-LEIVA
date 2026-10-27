# React Spring Boot Authentication - Step by Step Tutorial

What you will learn
## 1. Spring Security Using Security Filter Chain
## 2. CORS Configuration
## 3. Basic Authentication from React
## 4. Storing Variables in a .env File
## 5. Using Interceptors to manage requests

## Credenciales de prueba

Al iniciar la aplicación, la base de datos de desarrollo crea el usuario `admin` con contraseña `password`.
El endpoint de login recibe un `POST /login` con JSON, por ejemplo:

```json
{
  "username": "admin",
  "password": "password"
}
```

Estas credenciales son solo para desarrollo; reemplázalas antes de exponer la aplicación.
