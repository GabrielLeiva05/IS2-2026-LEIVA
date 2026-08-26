package com.nexora.gestion.config;

/**
 * ============================================================================
 * Constantes de nombres de atributos guardados en HttpSession.
 * ============================================================================
 * Centralizar estos nombres en un solo lugar evita "strings mágicos"
 * repetidos y desincronizados entre AuthController y SesionInterceptor.
 * ============================================================================
 */
public final class SesionConstantes {

    private SesionConstantes() {
        // Clase de constantes: no se instancia.
    }

    /** Id de la entidad logueada (Usuario o Administrador) en sesión. */
    public static final String SESSION_ID = "SESSION_USUARIO_ID";

    /** Tipo de cuenta logueada: "USUARIO" o "ADMINISTRADOR". */
    public static final String SESSION_TIPO = "SESSION_TIPO_CUENTA";

    /** Nombre completo, cacheado en sesión para mostrarlo en el navbar sin
     *  tener que ir a la base de datos en cada request. */
    public static final String SESSION_NOMBRE = "SESSION_NOMBRE_COMPLETO";

    /** Id de la Compra "en borrador" (carrito) que el usuario está armando
     *  actualmente, mientras no la confirme con registrarCompra(). */
    public static final String SESSION_CARRITO_COMPRA_ID = "SESSION_CARRITO_COMPRA_ID";

    public static final String TIPO_USUARIO = "USUARIO";
    public static final String TIPO_ADMINISTRADOR = "ADMINISTRADOR";
}
