package com.club.repository;
import com.club.audit.RevisionAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RevisionAuditoriaRepository extends JpaRepository<RevisionAuditoria,Integer> {}
