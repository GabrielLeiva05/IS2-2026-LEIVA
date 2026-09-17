package com.learnhub.service;

import com.learnhub.dto.CambiarPasswordDTO;
import com.learnhub.dto.ProfesorDTO;
import com.learnhub.dto.ProfesorFormDTO;
import com.learnhub.dto.RegistroProfesorDTO;
import com.learnhub.entity.Profesor;
import com.learnhub.entity.Rol;
import com.learnhub.entity.Usuario;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.ProfesorRepository;
import com.learnhub.repository.RolRepository;
import com.learnhub.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Capa de reglas de negocio de Profesor.
 *
 * <p>El controller no decide unicidad, roles, hashing ni bajas lógicas. Todo
 * eso vive aquí para que las reglas sean reutilizables desde otros adapters.</p>
 */
@Service
@RequiredArgsConstructor
public class ProfesorService {
    private final ProfesorRepository profesorRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ProfesorDTO registrarProfesor(RegistroProfesorDTO dto) {
        String email = normalizarEmail(dto.email());

        if (!dto.password().equals(dto.confirmPassword())) {
            throw new BusinessException("Las contraseñas no coinciden.");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("Ya existe una cuenta con ese correo.");
        }
        if (dto.fechaNacimiento().isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser futura.");
        }

        Rol rol = rolRepository.findByNombre("DOCENTE")
                .orElseThrow(() -> new BusinessException("El rol DOCENTE no está configurado."));

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(dto.password()));
        usuario.setHabilitado(true);
        usuario.getRoles().add(rol);

        Profesor profesor = new Profesor();
        profesor.setNombre(dto.nombre().trim());
        profesor.setApellido(dto.apellido().trim());
        profesor.setEspecialidad(dto.especialidad().trim());
        profesor.setSexo(dto.sexo());
        profesor.setFechaNacimiento(dto.fechaNacimiento());
        profesor.setUsuario(usuario);
        profesor.registrarProfesor();

        try {
            Profesor guardado = profesorRepository.save(profesor);
            eventPublisher.publishEvent(new ProfesorRegistradoEvent(email, guardado.getNombre()));
            return toDTO(guardado);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("No fue posible registrar el profesor: existe un dato único duplicado.");
        }
    }

    /** Equivalente funcional a listarProfesores/registrarProfesor del diagrama. */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> listarProfesores() {
        return profesorRepository.findByEliminadoFalseOrderByApellidoAscNombreAsc()
                .stream().map(this::toDTO).toList();
    }

    /** Listado completo (incluye dados de baja) para la gestión administrativa. */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> listarTodos() {
        return profesorRepository.findAll().stream()
                .sorted((a, b) -> {
                    int cmp = a.getApellido().compareToIgnoreCase(b.getApellido());
                    return cmp != 0 ? cmp : a.getNombre().compareToIgnoreCase(b.getNombre());
                })
                .map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProfesorDTO buscarPorEmail(String email) {
        return toDTO(obtenerPorEmail(email));
    }

    @Transactional(readOnly = true)
    public ProfesorDTO obtenerPorId(Long id) {
        return toDTO(obtenerEntidadPorId(id));
    }

    @Transactional
    public ProfesorDTO editarProfesor(String email, ProfesorFormDTO dto) {
        return editar(obtenerPorEmail(email), dto);
    }

    /** Variante administrativa: permite a un ADMIN editar a cualquier profesor por id. */
    @Transactional
    public ProfesorDTO editarProfesorPorId(Long id, ProfesorFormDTO dto) {
        return editar(obtenerEntidadPorId(id), dto);
    }

    @Transactional
    public void eliminarProfesor(String email) {
        Profesor profesor = obtenerPorEmail(email);
        profesor.eliminarProfesor();
        profesorRepository.save(profesor);
    }

    /** Variante administrativa: baja lógica de un profesor por id. */
    @Transactional
    public void eliminarProfesorPorId(Long id) {
        Profesor profesor = obtenerEntidadPorId(id);
        profesor.eliminarProfesor();
        profesorRepository.save(profesor);
    }

    private ProfesorDTO editar(Profesor profesor, ProfesorFormDTO dto) {
        profesor.setNombre(dto.nombre().trim());
        profesor.setApellido(dto.apellido().trim());
        profesor.setEspecialidad(dto.especialidad().trim());
        profesor.setSexo(dto.sexo());
        profesor.setFechaNacimiento(dto.fechaNacimiento());
        return toDTO(profesorRepository.save(profesor));
    }

    private Profesor obtenerEntidadPorId(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Profesor no encontrado."));
        return profesor;
    }

    @Transactional
    public void cambiarPassword(String email, CambiarPasswordDTO dto) {
        if (!dto.nuevaPassword().equals(dto.confirmarNuevaPassword())) {
            throw new BusinessException("Las nuevas contraseñas no coinciden.");
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(normalizarEmail(email))
                .orElseThrow(() -> new BusinessException("Usuario no encontrado."));

        if (!passwordEncoder.matches(dto.passwordActual(), usuario.getPasswordHash())) {
            throw new BusinessException("La contraseña actual no es correcta.");
        }
        if (passwordEncoder.matches(dto.nuevaPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("La nueva contraseña debe ser diferente.");
        }

        usuario.setPasswordHash(passwordEncoder.encode(dto.nuevaPassword()));
        usuarioRepository.save(usuario);
    }

    private Profesor obtenerPorEmail(String email) {
        Profesor profesor = profesorRepository.findByUsuarioEmailIgnoreCase(normalizarEmail(email))
                .orElseThrow(() -> new BusinessException("Profesor no encontrado."));
        if (profesor.isEliminado()) {
            throw new BusinessException("El profesor se encuentra dado de baja.");
        }
        return profesor;
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private ProfesorDTO toDTO(Profesor profesor) {
        return new ProfesorDTO(profesor.getId(), profesor.getNombre(), profesor.getApellido(),
                profesor.getEspecialidad(), profesor.getSexo(), profesor.getFechaNacimiento(),
                profesor.getUsuario().getEmail(), profesor.isEliminado());
    }
}
