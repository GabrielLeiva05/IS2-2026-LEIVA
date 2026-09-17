package com.learnhub.service;

import com.learnhub.dto.AuditEntryDTO;
import com.learnhub.entity.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Consulta histórica de Envers. Se expone mediante DTOs y sólo a ADMIN.
 *
 * <p>Se centraliza la consulta para evitar que un Controller tenga que
 * conocer APIs de Hibernate Envers.</p>
 */
@Service
@RequiredArgsConstructor
public class AuditService {
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<AuditEntryDTO> historialProfesor(Long id) {
        return historial(Profesor.class, id);
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDTO> historialAlumno(Long id) {
        return historial(Alumno.class, id);
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDTO> historialMateria(Long id) {
        return historial(Materia.class, id);
    }

    private <T> List<AuditEntryDTO> historial(Class<T> tipo, Long id) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = reader.createQuery()
                .forRevisionsOfEntity(tipo, false, true)
                .add(org.hibernate.envers.query.AuditEntity.id().eq(id))
                .addOrder(org.hibernate.envers.query.AuditEntity.revisionNumber().asc())
                .getResultList();

        List<AuditEntryDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            AuditRevision revision = (AuditRevision) row[1];
            RevisionType revisionType = (RevisionType) row[2];
            result.add(new AuditEntryDTO(tipo.getSimpleName(), id, revision.getId(), revision.getFecha(),
                    revision.getUsuarioEmail(), revisionType.name()));
        }
        return result;
    }
}
