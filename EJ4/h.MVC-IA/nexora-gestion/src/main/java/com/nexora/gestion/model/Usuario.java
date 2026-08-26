package com.nexora.gestion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * ENTIDAD "Usuario" (Modelo) — extiende Persona
 * ============================================================================
 * Representa la tabla "usuarios" en MySQL. Mapea 1 a 1 la clase "Usuario"
 * del diagrama UML, que "Extends" (hereda de) "Persona".
 *
 * RELACIONES (fiel al diagrama UML):
 *  - Usuario "1...*" Compra  -> un Usuario puede tener 0..N Compras
 *    (relación de agregación, mapeada como @OneToMany desde el lado "uno").
 *  - Usuario -> EstadoUsuario (enum) -> el campo "estado" refleja el símbolo
 *    "ENUM estadoUsuario" del diagrama, relacionado con Usuario.
 *
 * IMPORTANTE: esta clase es intencionalmente "anémica" en cuanto a REGLAS DE
 * NEGOCIO. Los métodos que aquí se ven (sumarIntentoFallido, bloquear,
 * resetearIntentos, iniciarSesion) son mutaciones de estado simples y
 * puntuales del propio objeto (consistentes con el diagrama UML), pero la
 * DECISIÓN de negocio (cuántos intentos fallidos se permiten antes de
 * bloquear, si la contraseña es válida contra el hash almacenado, etc.) se
 * calcula y orquesta siempre desde {@code UsuarioServiceImpl}, que es quien
 * efectivamente decide y coordina la persistencia. Esto respeta el
 * requerimiento de mantener las reglas de negocio en la capa de Servicio.
 *
 * NOTA: getters/setters escritos explícitamente (sin Lombok), ver el
 * comentario de la clase Persona para el motivo.
 * ============================================================================
 */
@Entity
@Table(name = "usuarios")
public class Usuario extends Persona {

    /** Identificador único (PK) — autogenerado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Contraseña de acceso. Se almacena ya "hasheada" (nunca en texto
     *  plano) — el hashing se realiza en la capa de Servicio. */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** Cantidad de intentos fallidos de inicio de sesión consecutivos. */
    @Column(name = "intentos", nullable = false)
    private Integer intentos = 0;

    /** Bandera de bloqueo manual/automático de la cuenta. */
    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = Boolean.FALSE;

    /** Estado lógico del usuario, ligado al ENUM "estadoUsuario" del UML. */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    /**
     * Lado "1" de la relación Usuario (1) --- (1..*) Compra.
     * mappedBy indica que la FK "usuario_id" vive en la tabla "compras"
     * (el dueño de la relación es Compra). cascade = ALL + orphanRemoval
     * porque el ciclo de vida de las compras de un usuario está atado a su
     * historial (si se elimina el usuario en cascada se eliminan sus
     * compras); en la práctica los Services evitan el borrado físico de
     * usuarios con historial, prefiriendo el bloqueo lógico.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Compra> compras = new ArrayList<>();

    public Usuario() {
        // Constructor vacío requerido por JPA/Hibernate.
    }

    // ==========================================================================
    // Getters y Setters explícitos
    // ==========================================================================

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

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    // ==========================================================================
    // Métodos de la clase "Usuario" del diagrama UML.
    // Son mutadores de estado simples; la orquestación de la regla de negocio
    // ("¿cuántos intentos antes de bloquear?", "¿la password matchea?") está
    // en UsuarioServiceImpl.
    // ==========================================================================

    /** Incrementa en 1 el contador de intentos fallidos. */
    public void sumarIntentoFallido() {
        this.intentos = (this.intentos == null ? 0 : this.intentos) + 1;
    }

    /** Bloquea la cuenta: pone bloqueado=true y estado=BLOQUEADO. */
    public void bloquear() {
        this.bloqueado = Boolean.TRUE;
        this.estado = EstadoUsuario.BLOQUEADO;
    }

    /** Desbloquea la cuenta y reinicia el contador de intentos. */
    public void resetearIntentos() {
        this.intentos = 0;
        this.bloqueado = Boolean.FALSE;
        this.estado = EstadoUsuario.ACTIVO;
    }
}
