package com.nexora.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * ENTIDAD "Administrador" (Modelo) — extiende Persona
 * ============================================================================
 * Corresponde a la clase "Aministrador" (sic, "Administrador") del diagrama
 * UML, que también "Extends" (hereda de) Persona.
 *
 * Igual que en Usuario: los métodos de negocio complejos (iniciarSesion,
 * desbloquearUsuario, gestionarUsuario) NO se implementan aquí con lógica
 * real — son orquestados por {@code AdministradorServiceImpl}, que es quien
 * coordina validaciones, acceso a repositorios de Usuario/Producto/
 * Inventario, etc. Esta entidad solo representa el estado persistente.
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok).
 * ============================================================================
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Persona {

    /** Identificador único (PK) — autogenerado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Contraseña de acceso (hasheada). */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** Nivel de acceso / rol interno del administrador (ej: "SUPERADMIN",
     *  "SOPORTE", "INVENTARIO"). Se usa como dato descriptivo; las reglas de
     *  autorización efectivas se resuelven en la capa de Servicio. */
    @Column(name = "nivel_acceso", nullable = false, length = 40)
    private String nivelAcceso;

    public Administrador() {
        // Constructor vacío requerido por JPA/Hibernate.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
}
