package com.learnhub.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Superclase de persistencia compartida por las entidades del dominio.
 *
 * <p>Además del identificador, centraliza auditoría técnica y control de
 * concurrencia. Spring Data JPA completa automáticamente createdAt,
 * updatedAt, createdBy y updatedBy gracias a @EnableJpaAuditing.</p>
 *
 * <p>@Version implementa optimistic locking. Si dos usuarios intentan
 * modificar simultáneamente el mismo registro, Hibernate detecta que la
 * versión cambió y evita sobrescribir silenciosamente el trabajo anterior.</p>
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;
}
