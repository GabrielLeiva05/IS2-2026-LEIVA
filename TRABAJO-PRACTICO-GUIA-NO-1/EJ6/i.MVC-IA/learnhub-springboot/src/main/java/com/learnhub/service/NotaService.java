package com.learnhub.service;

import com.learnhub.dto.NotaDTO;
import com.learnhub.entity.Materia;
import com.learnhub.entity.Nota;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.MateriaRepository;
import com.learnhub.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/** Reglas de negocio de Nota. */
@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;
    private final MateriaRepository materiaRepository;

    @Transactional
    public NotaDTO registrarNota(NotaDTO dto) {
        validar(dto);
        Materia materia = materiaRepository.findById(dto.materiaId())
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));
        if (materia.isEliminado()) throw new BusinessException("No se puede calificar una materia dada de baja.");

        Nota nota = new Nota();
        nota.setFecha(dto.fecha());
        nota.setValor(dto.valor());
        nota.setMateria(materia);
        return toDTO(notaRepository.save(nota));
    }

    @Transactional
    public NotaDTO editarNota(Long id, NotaDTO dto) {
        validar(dto);
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Nota no encontrada."));
        Materia materia = materiaRepository.findById(dto.materiaId())
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));
        nota.setFecha(dto.fecha());
        nota.setValor(dto.valor());
        nota.setMateria(materia);
        return toDTO(notaRepository.save(nota));
    }

    @Transactional(readOnly = true)
    public List<NotaDTO> listarPorMateria(Long materiaId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new BusinessException("Materia no encontrada."));
        return notaRepository.findByMateriaOrderByFechaDesc(materia).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public NotaDTO obtenerDTO(Long id) {
        return toDTO(notaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Nota no encontrada.")));
    }

    /** Devuelve el id del profesor dueño de la materia de una nota, para validar propiedad. */
    @Transactional(readOnly = true)
    public Long profesorIdDeNota(Long notaId) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new BusinessException("Nota no encontrada."));
        return nota.getMateria().getProfesor().getId();
    }

    private void validar(NotaDTO dto) {
        if (dto.valor() == null || dto.valor() < 0 || dto.valor() > 10) {
            throw new BusinessException("La nota debe estar entre 0 y 10.");
        }
        if (dto.fecha() == null || dto.fecha().isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de la nota no puede ser futura.");
        }
    }

    private NotaDTO toDTO(Nota n) {
        return new NotaDTO(n.getId(), n.getFecha(), n.getValor(), n.getMateria().getId());
    }
}
