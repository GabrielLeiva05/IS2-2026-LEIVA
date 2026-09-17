package com.learnhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.Instant;

/**
 * Cabecera de revisión de Hibernate Envers. Guarda quién realizó una
 * modificación además de la fecha técnica de la revisión.
 */
@Entity
@Table(name = "audit_revision")
@RevisionEntity(AuditRevisionListener.class)
@Getter
@Setter
public class AuditRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Long id;

    @RevisionTimestamp
    private long timestamp;

    @Column(name = "usuario_email", length = 180)
    private String usuarioEmail;

    public Instant getFecha() {
        return Instant.ofEpochMilli(timestamp);
    }
}
