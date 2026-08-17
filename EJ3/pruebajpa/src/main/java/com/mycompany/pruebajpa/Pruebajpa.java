package com.mycompany.pruebajpa;

import com.mycompany.pruebajpa.logica.Alumno;
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
        
        Alumno alu = new Alumno(29, "maria", "lopez",new Date());
        
        control.crearAlumno(alu);
        
        //control.eliminarAlumno(30);
        
        /*alu.setApellido("otro");
        
        control.editarAlumno(alu);*/
        
        Alumno alu15 = control.traerAlumno(15);
        System.out.println("------------BÚSQUEDA INDIVIDUAL----------");
        System.out.println("El alumno es: " + alu15.toString());
        System.out.println("------------BÚSQUEDA DE TODOS------------");
        List<Alumno> listaAlumnos = control.traerListaAlumnos();
        for (Alumno a : listaAlumnos) {
            System.out.println("El alumno es: " + a.toString());
        }
        System.out.println("----------------------------------------");
    }
}
