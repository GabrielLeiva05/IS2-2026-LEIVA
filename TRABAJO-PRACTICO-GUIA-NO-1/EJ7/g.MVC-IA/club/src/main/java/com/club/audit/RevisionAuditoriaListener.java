package com.club.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RevisionAuditoriaListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        RevisionAuditoria revision = (RevisionAuditoria) revisionEntity;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        revision.setUsuario(auth != null && auth.isAuthenticated() && auth.getName() != null ? auth.getName() : "system");
    }
}
