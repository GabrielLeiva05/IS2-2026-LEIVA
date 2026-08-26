package com.nexora.gestion.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ============================================================================
 * INTERCEPTOR "SesionInterceptor" (Capa de Configuración / Controlador)
 * ============================================================================
 * Un {@link HandlerInterceptor} se ejecuta ANTES de que la petición llegue a
 * un método @Controller. Se usa acá como un mini "guardia de seguridad" MVC:
 * protege rutas que requieren estar autenticado, sin tener que repetir el
 * mismo chequeo "if (session == null) ..." al principio de cada método de
 * cada Controller (evita duplicación y olvidos).
 *
 * Reglas de autorización aplicadas (ver registro de rutas en WebConfig):
 *   - "/admin/**"   -> requiere sesión de ADMINISTRADOR.
 *   - "/compras/**" -> requiere sesión de USUARIO.
 *
 * Si no se cumple la condición, redirige a /login (no se lanza una excepción
 * HTTP cruda: se prioriza una buena experiencia de usuario en una app MVC
 * tradicional con vistas server-side).
 * ============================================================================
 */
public class SesionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        boolean requiereAdmin = uri.startsWith("/admin");
        boolean requiereUsuario = uri.startsWith("/compras");

        if (!requiereAdmin && !requiereUsuario) {
            return true; // Ruta pública: se deja pasar.
        }

        Object tipo = (session != null) ? session.getAttribute(SesionConstantes.SESSION_TIPO) : null;

        if (requiereAdmin && !SesionConstantes.TIPO_ADMINISTRADOR.equals(tipo)) {
            response.sendRedirect(request.getContextPath() + "/login?error=acceso-restringido");
            return false;
        }

        if (requiereUsuario && !SesionConstantes.TIPO_USUARIO.equals(tipo)) {
            response.sendRedirect(request.getContextPath() + "/login?error=acceso-restringido");
            return false;
        }

        return true;
    }
}
