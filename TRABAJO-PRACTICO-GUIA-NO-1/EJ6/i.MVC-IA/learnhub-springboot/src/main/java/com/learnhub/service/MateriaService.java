package com.learnhub.service;

import com.learnhub.dto.MateriaDTO;
import com.learnhub.entity.Alumno;
import com.learnhub.entity.Materia;
import com.learnhub.entity.Profesor;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.AlumnoRepository;
import com.learnhub.repository.MateriaRepository;
import com.learnhub.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Reglas de negocio de Materia y asociación Profesor-Alumno. */
@Service
@RequiredArgsConstructor
public class MateriaService {
    private final MateriaRepository materiaRepository;
    private final ProfesorRepository profesorRepository;
    private final AlumnoRepository alumnoRepository;

    @Transactional
    public MateriaDTO registrarMateria(MateriaDTO dto) {
        Profesor profesor = obtenerProfesorActivo(dto.profesorId());
        Alumno alumno = obtenerAlumnoActivo(dto.alumnoId());

        Materia materia = new Materia();
        materia.setNombre(dto.nombre().trim());
        materia.setProfesor(profesor);
        materia.setAlumno(alumno);
        materia.registrarMateria();
        return toDTO(materiaRepository.save(materia));
    }

    @Transactional
    public MateriaDTO editarMateria(Long id, MateriaDTO dto) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));
        materia.setNombre(dto.nombre().trim());
        materia.setProfesor(obtenerProfesorActivo(dto.profesorId()));
        materia.setAlumno(obtenerAlumnoActivo(dto.alumnoId()));
        return toDTO(materiaRepository.save(materia));
    }

    @Transactional
    public void eliminarMateria(Long id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));
        materia.eliminarMateria();
        materiaRepository.save(materia);
    }

    @Transactional(readOnly = true)
    public List<MateriaDTO> listarPorProfesor(Long profesorId) {
        Profesor profesor = obtenerProfesorActivo(profesorId);
        return materiaRepository.findByProfesorAndEliminadoFalseOrderByNombreAsc(profesor)
                .stream().map(this::toDTO).toList();
    }

    /** Listado completo (incluye dadas de baja) para la gestión administrativa. */
    @Transactional(readOnly = true)
    public List<MateriaDTO> listarTodas() {
        return materiaRepository.findAll().stream()
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Materia obtener(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));
    }

    @Transactional(readOnly = true)
    public MateriaDTO obtenerDTO(Long id) {
        return toDTO(obtener(id));
    }

    /** Id del profesor dueño de la materia, útil para validar propiedad desde el área docente. */
    @Transactional(readOnly = true)
    public Long profesorIdDeMateria(Long materiaId) {
        return obtener(materiaId).getProfesor().getId();
    }

    private Profesor obtenerProfesorActivo(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Profesor no encontrado."));
        if (profesor.isEliminado()) throw new BusinessException("El profesor está dado de baja.");
        return profesor;
    }

    private Alumno obtenerAlumnoActivo(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Alumno no encontrado."));
        if (alumno.isEliminado()) throw new BusinessException("El alumno está dado de baja.");
        return alumno;
    }

    private MateriaDTO toDTO(Materia m) {
        return new MateriaDTO(m.getId(), m.getNombre(), m.getProfesor().getId(), m.getAlumno().getId(), m.isEliminado());
    }
}
