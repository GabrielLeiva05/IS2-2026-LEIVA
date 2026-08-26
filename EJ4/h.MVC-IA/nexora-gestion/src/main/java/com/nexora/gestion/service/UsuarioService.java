package com.nexora.gestion.service;

import com.nexora.gestion.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * INTERFAZ "UsuarioService" (Capa de Servicio)
 * ============================================================================
 * Contrato de las REGLAS DE NEGOCIO relacionadas con Usuario. Programar
 * contra una interfaz (y no directamente contra la clase de implementación)
 * es una buena práctica: permite desacoplar al Controller de los detalles
 * de implementación y facilita el testing (se puede mockear la interfaz).
 *
 * Estos métodos son el "traductor" de los métodos del diagrama UML
 * (iniciarSesion, sumarIntentoFallido, bloquear, resetearIntentos) a
 * operaciones de negocio completas, que incluyen validación + persistencia.
 * ============================================================================
 */
public interface UsuarioService {

    /** Registra un nuevo Usuario (regla: valida datos, hashea password,
     *  evita correos duplicados). */
    Usuario registrar(Usuario usuario);

    /**
     * Regla de negocio de inicio de sesión:
     *  - Si el usuario no existe -> CredencialesInvalidasException.
     *  - Si está bloqueado -> UsuarioBloqueadoException.
     *  - Si la password no matchea -> suma intento fallido, si llega a 3
     *    intentos lo bloquea automáticamente, y lanza CredencialesInvalidasException.
     *  - Si es correcta -> resetea intentos y devuelve el Usuario autenticado.
     */
    Usuario iniciarSesion(String correo, String password);

    /** Lista todos los usuarios (para el panel de administración). */
    List<Usuario> listarTodos();

    Optional<Usuario> buscarPorId(Long id);

    /** Bloquea manualmente a un usuario (acción disparada por un
     *  Administrador, ver AdministradorService.desbloquearUsuario para el
     *  camino inverso). */
    void bloquear(Long usuarioId);

    /** Desbloquea a un usuario y reinicia sus intentos fallidos. */
    void desbloquear(Long usuarioId);

    /** Elimina un usuario (regla: no se permite si tiene compras asociadas,
     *  para preservar la integridad del historial). */
    void eliminar(Long usuarioId);
}
