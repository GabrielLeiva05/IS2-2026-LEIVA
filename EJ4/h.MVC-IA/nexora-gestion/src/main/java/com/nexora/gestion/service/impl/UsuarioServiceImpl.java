package com.nexora.gestion.service.impl;

import com.nexora.gestion.exception.CredencialesInvalidasException;
import com.nexora.gestion.exception.EmailDuplicadoException;
import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.exception.UsuarioBloqueadoException;
import com.nexora.gestion.model.EstadoUsuario;
import com.nexora.gestion.model.Usuario;
import com.nexora.gestion.repository.UsuarioRepository;
import com.nexora.gestion.service.UsuarioService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * IMPLEMENTACIÓN "UsuarioServiceImpl" (Capa de Servicio)
 * ============================================================================
 * @Service marca esta clase como un Bean de la capa de negocio para que
 * Spring la administre y la inyecte automáticamente donde se necesite
 * (ej. en UsuarioController mediante inyección por constructor).
 *
 * @Transactional a nivel de clase asegura que cada método público se ejecute
 * dentro de una transacción de base de datos: si algo falla a mitad de
 * camino (una excepción no controlada), Spring hace ROLLBACK automático de
 * todos los cambios realizados por ese método, evitando estados
 * inconsistentes (ej. un usuario a medio actualizar).
 *
 * AQUÍ es donde realmente viven las REGLAS DE NEGOCIO de Usuario: máximo de
 * intentos fallidos permitidos, verificación de contraseña, bloqueo
 * automático, etc. El Controller NUNCA decide esto: solo invoca estos
 * métodos y reacciona a su resultado (éxito / excepción).
 * ============================================================================
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    /** Regla de negocio: cantidad máxima de intentos fallidos antes de
     *  bloquear automáticamente la cuenta. */
    private static final int MAX_INTENTOS_FALLIDOS = 3;

    private final UsuarioRepository usuarioRepository;

    /** Codificador de contraseñas (hashing con BCrypt: algoritmo de
     *  hashing lento y con "salt" incorporado, resistente a ataques de
     *  fuerza bruta y rainbow tables — nunca se guardan passwords en texto
     *  plano). */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Inyección de dependencias por CONSTRUCTOR (preferida sobre @Autowired
     * en campos): hace explícitas las dependencias obligatorias, facilita
     * el testing (se puede instanciar la clase pasando mocks) y permite que
     * el campo sea "final" (inmutable tras la construcción).
     */
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        // Regla de negocio: no permitir correos duplicados.
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new EmailDuplicadoException("Ya existe un usuario registrado con el correo: " + usuario.getCorreo());
        }
        // Regla de negocio: la contraseña nunca se persiste en texto plano.
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setBloqueado(Boolean.FALSE);
        usuario.setIntentos(0);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario iniciarSesion(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos."));

        // Regla de negocio: un usuario bloqueado no puede iniciar sesión,
        // sin importar si la contraseña que ingresó es correcta o no.
        if (Boolean.TRUE.equals(usuario.getBloqueado()) || usuario.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw new UsuarioBloqueadoException(
                    "Tu cuenta está bloqueada por intentos fallidos. Contactá a un administrador.");
        }

        boolean passwordCorrecta = passwordEncoder.matches(password, usuario.getPassword());

        if (!passwordCorrecta) {
            // Delegamos la MUTACIÓN de estado al método del modelo, pero la
            // REGLA (a partir de cuántos intentos se bloquea) se decide acá.
            usuario.sumarIntentoFallido();
            if (usuario.getIntentos() >= MAX_INTENTOS_FALLIDOS) {
                usuario.bloquear();
            }
            usuarioRepository.save(usuario);
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos.");
        }

        // Login exitoso: reiniciamos el contador de intentos fallidos.
        usuario.resetearIntentos();
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void bloquear(Long usuarioId) {
        Usuario usuario = obtenerOFallar(usuarioId);
        usuario.bloquear();
        usuarioRepository.save(usuario);
    }

    @Override
    public void desbloquear(Long usuarioId) {
        Usuario usuario = obtenerOFallar(usuarioId);
        usuario.resetearIntentos();
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long usuarioId) {
        Usuario usuario = obtenerOFallar(usuarioId);
        // Regla de negocio: preservar integridad del historial de compras.
        if (usuario.getCompras() != null && !usuario.getCompras().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar un usuario con compras registradas. Bloquéelo en su lugar.");
        }
        usuarioRepository.delete(usuario);
    }

    private Usuario obtenerOFallar(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: id=" + usuarioId));
    }
}
