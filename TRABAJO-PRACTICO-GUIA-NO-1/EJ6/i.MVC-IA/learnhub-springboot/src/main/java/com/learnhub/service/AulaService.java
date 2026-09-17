package com.learnhub.service;

import com.learnhub.dto.AulaDTO;
import com.learnhub.entity.Aula;
import com.learnhub.entity.Grado;
import com.learnhub.entity.Alumno;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.AulaRepository;
import com.learnhub.repository.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Reglas de negocio de Aula. */
@Service
@RequiredArgsConstructor
public class AulaService {
    private final AulaRepository aulaRepository;
    private final GradoService gradoService;
    private final AlumnoRepository alumnoRepository;

    @Transactional
    public AulaDTO crear(AulaDTO dto) {
        Grado grado = gradoService.obtenerEntidad(dto.gradoId());
        Aula aula = new Aula();
        aula.setDivision(dto.division().trim());
        grado.agregarAula(aula);
        return toDTO(aulaRepository.save(aula));
    }

    @Transactional(readOnly = true)
    public List<AulaDTO> listarPorGrado(Long gradoId) {
        Grado grado = gradoService.obtenerEntidad(gradoId);
        return aulaRepository.findByGradoOrderByDivisionAsc(grado).stream().map(this::toDTO).toList();
    }

    /** Listado completo de aulas de todos los grados, usado por formularios administrativos. */
    @Transactional(readOnly = true)
    public List<AulaDTO> listarTodas() {
        return aulaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public void asociarAlumno(Long aulaId, Long alumnoId) {
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new BusinessException("Aula no encontrada."));
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new BusinessException("Alumno no encontrado."));
        if (alumno.isEliminado()) {
            throw new BusinessException("No se puede asociar un alumno dado de baja.");
        }
        aula.getAlumnos().add(alumno);
        alumno.getAulas().add(aula);
        aulaRepository.save(aula);
    }

    @Transactional(readOnly = true)
    public Aula obtenerEntidad(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Aula no encontrada."));
    }

    @Transactional(readOnly = true)
    public AulaDTO obtenerDTO(Long id) {
        return toDTO(obtenerEntidad(id));
    }

    /** Alumnos activos que todavía no están asociados a esta aula, para el formulario de alta. */
    @Transactional(readOnly = true)
    public List<Alumno> alumnosDisponibles(Long aulaId) {
        Aula aula = obtenerEntidad(aulaId);
        return alumnoRepository.findByEliminadoFalseOrderByApellidoAscNombreAsc().stream()
                .filter(a -> !aula.getAlumnos().contains(a))
                .toList();
    }

    /**
     * Alumnos ya asociados al aula, resueltos dentro de la transacción para
     * evitar LazyInitializationException al renderizar la vista (open-in-view=false).
     */
    @Transactional(readOnly = true)
    public List<Alumno> alumnosDelAula(Long aulaId) {
        Aula aula = obtenerEntidad(aulaId);
        return aula.getAlumnos().stream()
                .sorted((a, b) -> {
                    int cmp = a.getApellido().compareToIgnoreCase(b.getApellido());
                    return cmp != 0 ? cmp : a.getNombre().compareToIgnoreCase(b.getNombre());
                })
                .toList();
    }

    private AulaDTO toDTO(Aula aula) {
        return new AulaDTO(aula.getId(), aula.getDivision(), aula.getGrado().getId(), aula.obtenerCantidadAlumnos());
    }
}
