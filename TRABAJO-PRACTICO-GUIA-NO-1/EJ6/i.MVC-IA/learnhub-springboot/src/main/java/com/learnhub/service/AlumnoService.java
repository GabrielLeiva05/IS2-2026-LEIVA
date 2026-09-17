package com.learnhub.service;

import com.learnhub.dto.AlumnoDTO;
import com.learnhub.entity.Alumno;
import com.learnhub.entity.Aula;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.AlumnoRepository;
import com.learnhub.repository.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/** Reglas de negocio de Alumno, incluida la baja lógica. */
@Service
@RequiredArgsConstructor
public class AlumnoService {
    private final AlumnoRepository alumnoRepository;
    private final AulaRepository aulaRepository;

    @Transactional
    public AlumnoDTO registrarAlumno(AlumnoDTO dto) {
        validarFecha(dto.fechaNacimiento());
        Alumno alumno = new Alumno();
        alumno.setNombre(dto.nombre().trim());
        alumno.setApellido(dto.apellido().trim());
        alumno.setFechaNacimiento(dto.fechaNacimiento());
        alumno.registrarAlumno();
        Alumno guardado = alumnoRepository.save(alumno);
        asignarAulas(guardado, dto.aulaIds());
        return toDTO(guardado);
    }

    @Transactional
    public AlumnoDTO editarAlumno(Long id, AlumnoDTO dto) {
        validarFecha(dto.fechaNacimiento());
        Alumno alumno = obtener(id);
        alumno.setNombre(dto.nombre().trim());
        alumno.setApellido(dto.apellido().trim());
        alumno.setFechaNacimiento(dto.fechaNacimiento());

        // Como Aula es el lado propietario de la relación, primero quitamos
        // al alumno de las filas existentes en la tabla intermedia.
        for (Aula aulaActual : new HashSet<>(alumno.getAulas())) {
            aulaActual.getAlumnos().remove(alumno);
            aulaRepository.save(aulaActual);
        }
        alumno.getAulas().clear();
        asignarAulas(alumno, dto.aulaIds());
        return toDTO(alumnoRepository.save(alumno));
    }

    @Transactional
    public void eliminarAlumno(Long id) {
        Alumno alumno = obtener(id);
        alumno.eliminarAlumno();
        alumnoRepository.save(alumno);
    }

    @Transactional(readOnly = true)
    public List<AlumnoDTO> listarAlumnos() {
        return alumnoRepository.findByEliminadoFalseOrderByApellidoAscNombreAsc().stream()
                .map(this::toDTO).toList();
    }

    /** Listado completo (incluye dados de baja) para la gestión administrativa. */
    @Transactional(readOnly = true)
    public List<AlumnoDTO> listarTodos() {
        return alumnoRepository.findAll().stream()
                .sorted((a, b) -> {
                    int cmp = a.getApellido().compareToIgnoreCase(b.getApellido());
                    return cmp != 0 ? cmp : a.getNombre().compareToIgnoreCase(b.getNombre());
                })
                .map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Alumno obtener(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Alumno no encontrado."));
    }

    @Transactional(readOnly = true)
    public AlumnoDTO obtenerDTO(Long id) {
        return toDTO(obtener(id));
    }

    private void asignarAulas(Alumno alumno, java.util.Set<Long> aulaIds) {
        if (aulaIds == null) return;
        for (Long aulaId : aulaIds) {
            Aula aula = aulaRepository.findById(aulaId)
                    .orElseThrow(() -> new BusinessException("Aula no encontrada: " + aulaId));
            // Aula es el lado propietario de la relación ManyToMany.
            aula.getAlumnos().add(alumno);
            alumno.getAulas().add(aula);
            aulaRepository.save(aula);
        }
    }

    private void validarFecha(LocalDate fecha) {
        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser futura.");
        }
    }

    private AlumnoDTO toDTO(Alumno alumno) {
        return new AlumnoDTO(alumno.getId(), alumno.getNombre(), alumno.getApellido(),
                alumno.getFechaNacimiento(), new HashSet<>(alumno.getAulas().stream().map(Aula::getId).toList()),
                alumno.isEliminado());
    }
}
