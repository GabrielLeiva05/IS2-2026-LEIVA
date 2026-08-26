package com.nexora.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

/**
 * ============================================================================
 * CLASE ABSTRACTA "Persona" (Modelo)
 * ============================================================================
 * Corresponde a la clase "Persona" del diagrama UML. No es una entidad JPA
 * "completa" (no tiene tabla propia ni @Id), sino una {@link MappedSuperclass}:
 * esto le indica a JPA/Hibernate que las columnas aquí definidas deben ser
 * "heredadas" e incluidas dentro de la tabla de cada subclase concreta
 * (estrategia de mapeo TABLE_PER_CLASS de facto, una tabla por subclase:
 * "usuarios" y "administradores"), tal como en el diagrama UML el "id" está
 * definido en cada subclase (Usuario, Administrador) y no en Persona.
 *
 * Esta clase NO contiene reglas de negocio complejas: solo agrupa atributos
 * comunes y un método utilitario simple (getNombreCompleto). Cualquier regla
 * de negocio real (login, bloqueo de usuario, alta de compras, etc.) vive en
 * la capa de Servicio (paquete service), nunca en el controlador ni en la
 * vista, respetando la separación de responsabilidades de la arquitectura
 * MVC solicitada.
 *
 * NOTA: los getters/setters se escriben EXPLÍCITAMENTE (sin Lombok) para que
 * el proyecto compile sin depender de que el "annotation processor" de
 * Lombok esté correctamente activado en el entorno de cada desarrollador
 * (IDE, versión de Maven, JDK, etc.), evitando así errores de compilación
 * como "cannot find symbol: method getX()" en configuraciones donde Lombok
 * no se procesa correctamente.
 * ============================================================================
 */
@MappedSuperclass
public abstract class Persona {

    /** Nombre de pila de la persona. */
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    /** Apellido de la persona. */
    @Column(name = "apellido", nullable = false, length = 80)
    private String apellido;

    /** Número de documento de identidad. */
    @Column(name = "documento", nullable = false)
    private Integer documento;

    /** Fecha de nacimiento (se usa LocalDate, el tipo moderno de Java
     *  equivalente al "Date" del diagrama UML). */
    @Column(name = "fecha_de_nacimiento")
    private LocalDate fechaDeNacimiento;

    /** Correo electrónico de contacto / inicio de sesión. */
    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    // ==========================================================================
    // Getters y Setters explícitos
    // ==========================================================================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Método "getNombreCompleto()" del diagrama UML.
     * No es una regla de negocio (no decide nada, no valida nada): es un
     * simple método de formateo de datos propios del objeto, por eso puede
     * vivir en el modelo sin romper la separación de capas.
     */
    public String getNombreCompleto() {
        return (nombre == null ? "" : nombre) + " " + (apellido == null ? "" : apellido);
    }
}
