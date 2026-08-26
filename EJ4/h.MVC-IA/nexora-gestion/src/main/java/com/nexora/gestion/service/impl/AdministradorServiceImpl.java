package com.nexora.gestion.service.impl;

import com.nexora.gestion.exception.CredencialesInvalidasException;
import com.nexora.gestion.exception.EmailDuplicadoException;
import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.model.Administrador;
import com.nexora.gestion.repository.AdministradorRepository;
import com.nexora.gestion.service.AdministradorService;
import com.nexora.gestion.service.UsuarioService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================================
 * IMPLEMENTACIÓN "AdministradorServiceImpl" (Capa de Servicio)
 * ============================================================================
 * Nótese cómo esta clase ORQUESTA a otro servicio (UsuarioService) para
 * implementar el método "desbloquearUsuario()" del diagrama UML: la clase
 * Administrador, en el diagrama, define esa operación porque
 * CONCEPTUALMENTE es una acción que un administrador ejecuta sobre un
 * usuario, pero la MUTACIÓN de datos de Usuario la sigue realizando
 * UsuarioService (principio de responsabilidad única: cada Service es dueño
 * de las reglas de su propia entidad).
 * ============================================================================
 */
@Service
@Transactional
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdministradorServiceImpl(AdministradorRepository administradorRepository,
                                     UsuarioService usuarioService) {
        this.administradorRepository = administradorRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public Administrador registrar(Administrador administrador) {
        if (administradorRepository.existsByCorreo(administrador.getCorreo())) {
            throw new EmailDuplicadoException(
                    "Ya existe un administrador registrado con el correo: " + administrador.getCorreo());
        }
        administrador.setPassword(passwordEncoder.encode(administrador.getPassword()));
        return administradorRepository.save(administrador);
    }

    @Override
    @Transactional(readOnly = true)
    public Administrador iniciarSesion(String correo, String password) {
        Administrador administrador = administradorRepository.findByCorreo(correo)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos."));

        if (!passwordEncoder.matches(password, administrador.getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos.");
        }
        return administrador;
    }

    @Override
    public void desbloquearUsuario(Long administradorId, Long usuarioId) {
        // Regla de negocio: el administrador que ejecuta la acción debe existir
        // (defensa adicional, más allá de que el Controller ya valida la sesión).
        administradorRepository.findById(administradorId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Administrador no encontrado: id=" + administradorId));

        usuarioService.desbloquear(usuarioId);
    }
}
