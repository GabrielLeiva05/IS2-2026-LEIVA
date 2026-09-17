package com.learnhub.entity;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * En cada revisión Envers obtiene el usuario autenticado desde Spring
 * Security. Para operaciones públicas, como el alta de un profesor, se
 * registra ANONIMO.
 */
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevision revision = (AuditRevision) revisionEntity;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            revision.setUsuarioEmail(authentication.getName());
        } else {
            revision.setUsuarioEmail("ANONIMO");
        }
    }
}
