package com.mycompany.pruebajpa;

import com.mycompany.pruebajpa.logica.Alumno;
import com.mycompany.pruebajpa.logica.Carrera;
import com.mycompany.pruebajpa.logica.Controladora;
import com.mycompany.pruebajpa.persistencia.ControladoraPersistencia;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class Pruebajpa {

    public static void main(String[] args) {
        
        Controladora control = new Controladora();
        
        //Crear Carrera
        Carrera carre = new Carrera(91, "Lic. en Ciencias de la Computación");
        //Guardar carrera en bd
        control.crearCarrera(carre);
        //Crear alumno con carrera
        Alumno alu = new Alumno(2, "maria", "lopez",new Date(),carre);
        //guardamos el alumno en bd
        control.crearAlumno(alu);
        //vemos resultado
        System.out.println("-------------------------");
        System.out.println("---------------DATOS ALUMNO-------------");
        Alumno alu23 = control.traerAlumno(23);
        System.out.println("Alumno: "+alu23.getNombre() + " " + alu23.getApellido());
        System.out.println("Cursa la carrera de: " + alu23.getCarre().getNombre());
        
        
    }
}
