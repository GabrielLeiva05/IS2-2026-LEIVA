package com.learnhub.service;

import com.learnhub.dto.NotaDTO;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.MateriaRepository;
import com.learnhub.repository.NotaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba de una regla de negocio que no depende de la vista. */
@ExtendWith(MockitoExtension.class)
class NotaServiceTest {
    @Mock NotaRepository notaRepository;
    @Mock MateriaRepository materiaRepository;
    @InjectMocks NotaService notaService;

    @Test
    void rechazaNotaFueraDeEscala() {
        NotaDTO dto = new NotaDTO(null, LocalDate.now(), 11f, 1L);
        assertThrows(BusinessException.class, () -> notaService.registrarNota(dto));
    }
}
