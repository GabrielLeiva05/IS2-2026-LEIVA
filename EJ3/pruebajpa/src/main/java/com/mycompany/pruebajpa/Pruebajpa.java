package com.mycompany.pruebajpa;

import com.mycompany.pruebajpa.logica.Alumno;
import com.mycompany.pruebajpa.logica.Controladora;
import com.mycompany.pruebajpa.persistencia.ControladoraPersistencia;
import java.util.Date;

/**
 *
 * @author Gabriel
 */
public class Pruebajpa {

    public static void main(String[] args) {
        
        Controladora control = new Controladora();
        
        Alumno alu = new Alumno(30, "jorge", "lopez",new Date());
        
        control.crearAlumno(alu);
        
        //control.eliminarAlumno(30);
        
        alu.setApellido("otro");
        
        control.editarAlumno(alu);
    }
}
