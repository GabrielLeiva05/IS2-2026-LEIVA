package com.club.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "revinfo")
@RevisionEntity(RevisionAuditoriaListener.class)
@Getter @Setter @NoArgsConstructor
public class RevisionAuditoria extends DefaultRevisionEntity {
    private String usuario;
}
