package com.furnish.erp.service.impl;

import com.furnish.erp.domain.Empleado;
import com.furnish.erp.domain.Usuario;
import com.furnish.erp.exception.BusinessException;
import com.furnish.erp.exception.ResourceNotFoundException;
import com.furnish.erp.repository.EmpleadoRepository;
import com.furnish.erp.repository.UsuarioRepository;
import com.furnish.erp.dto.UsuarioForm;
import com.furnish.erp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 * Implementación: UsuarioServiceImpl
 * ============================================================================
 * Cumple un doble rol:
 *   1) Service de negocio "tradicional" (alta/baja/edición de usuarios).
 *   2) UserDetailsService de Spring Security: el método loadUserByUsername()
 *      es invocado automáticamente por el AuthenticationManager cada vez que
 *      alguien intenta iniciar sesión (ver SecurityConfig), traduciendo
 *      nuestra entidad Usuario al contrato UserDetails que entiende el
 *      framework de seguridad.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;
    // PasswordEncoder (bean BCrypt definido en SecurityConfig) para nunca
    // guardar ni comparar contraseñas en texto plano.
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario o contraseña inválidos"));

        // Construye el objeto UserDetails que Spring Security usa para
        // validar la contraseña (compara el hash) y autorizar rutas según
        // el rol -> se antepone "ROLE_" porque es la convención que exige
        // hasRole()/hasAuthority() de Spring Security.
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!usuario.isHabilitado())
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));
    }

    @Override
    public Usuario registrarUsuario(UsuarioForm form) {
        if (usuarioRepository.existsByUsername(form.getUsername())) {
            throw new BusinessException("Ya existe un usuario con el nombre '" + form.getUsername() + "'");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new BusinessException("La contraseña es obligatoria para dar de alta un usuario");
        }

        Empleado empleado = resolverEmpleadoOpcional(form.getIdEmpleado());

        Usuario usuario = Usuario.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword())) // Hash BCrypt
                .rol(form.getRol())
                .habilitado(form.isHabilitado())
                .empleado(empleado)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario editarUsuario(Long id, UsuarioForm form) {
        Usuario existente = buscarPorId(id);

        if (!existente.getUsername().equals(form.getUsername())
                && usuarioRepository.existsByUsername(form.getUsername())) {
            throw new BusinessException("Ya existe un usuario con el nombre '" + form.getUsername() + "'");
        }

        existente.setUsername(form.getUsername());
        existente.setRol(form.getRol());
        existente.setHabilitado(form.isHabilitado());
        existente.setEmpleado(resolverEmpleadoOpcional(form.getIdEmpleado()));

        // Solo se re-encripta y actualiza la contraseña si el usuario cargó
        // un valor nuevo; de lo contrario se conserva el hash existente.
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    private Empleado resolverEmpleadoOpcional(Long idEmpleado) {
        if (idEmpleado == null) return null;
        return empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el empleado con id " + idEmpleado));
    }
}
