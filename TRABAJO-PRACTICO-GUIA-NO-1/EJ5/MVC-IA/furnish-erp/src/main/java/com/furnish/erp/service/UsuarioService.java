package com.furnish.erp.service;

import com.furnish.erp.domain.Usuario;
import com.furnish.erp.dto.UsuarioForm;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Interfaz de servicio: UsuarioService.
 * Extiende UserDetailsService (contrato de Spring Security) para que este
 * mismo Service pueda ser usado directamente por el AuthenticationProvider
 * en el proceso de login (ver SecurityConfig), evitando duplicar la lógica
 * de carga de usuarios en dos lugares distintos.
 */
public interface UsuarioService extends UserDetailsService {

    List<Usuario> listarTodos();

    Usuario buscarPorId(Long id);

    /** Alta de usuario: valida unicidad de username y aplica el hash BCrypt
     *  a la contraseña antes de persistir. */
    Usuario registrarUsuario(UsuarioForm form);

    /** Edición: si el campo password del form viene vacío, se conserva el
     *  hash de la contraseña actual (no se pisa con un valor vacío). */
    Usuario editarUsuario(Long id, UsuarioForm form);

    void eliminarUsuario(Long id);
}
