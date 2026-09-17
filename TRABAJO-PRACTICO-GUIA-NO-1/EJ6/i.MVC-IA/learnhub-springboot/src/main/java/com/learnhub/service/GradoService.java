package com.learnhub.service;

import com.learnhub.dto.GradoDTO;
import com.learnhub.entity.Grado;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.GradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Reglas de negocio y persistencia de Grado. */
@Service
@RequiredArgsConstructor
public class GradoService {
    private final GradoRepository gradoRepository;

    @Transactional
    public GradoDTO crear(GradoDTO dto) {
        String nivel = dto.nivel().trim();
        if (gradoRepository.existsByNivelIgnoreCase(nivel)) {
            throw new BusinessException("Ya existe un grado con ese nivel.");
        }
        Grado grado = new Grado();
        grado.setNivel(nivel);
        return toDTO(gradoRepository.save(grado));
    }

    @Transactional(readOnly = true)
    public List<GradoDTO> listar() {
        return gradoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Grado obtenerEntidad(Long id) {
        return gradoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Grado no encontrado."));
    }

    @Transactional(readOnly = true)
    public GradoDTO obtenerDTO(Long id) {
        return toDTO(obtenerEntidad(id));
    }

    private GradoDTO toDTO(Grado g) { return new GradoDTO(g.getId(), g.getNivel()); }
}
