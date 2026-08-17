//Paquetes
package com.mycompany.pruebajpa.logica;

//Imports
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author Gabriel
 */

@Entity
public class Alumno implements Serializable {
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @Basic
    private String nombre;
    private String apellido;
    @Temporal(TemporalType.DATE)
    private Date fechaNac;

    public Alumno() {
    }

    public Alumno(int id, String nombre, String apellido, Date fechaNac) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNac = fechaNac;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }
    
    
}
