package com.mycompany.pruebajpa;

import com.mycompany.pruebajpa.logica.Alumno;
import com.mycompany.pruebajpa.logica.Carrera;
import com.mycompany.pruebajpa.logica.Controladora;
import com.mycompany.pruebajpa.logica.Materia;
import com.mycompany.pruebajpa.persistencia.ControladoraPersistencia;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class Pruebajpa {

    public static void main(String[] args) {
        
        Controladora control = new Controladora();
        
        //creamos lista de materias 
        LinkedList<Materia> listaMaterias = new LinkedList<Materia>();
        //Crear Carrera
        Carrera carre = new Carrera(91, "Lic. en Ciencias de la Computación", listaMaterias);
        //Guardar carrera en bd
        control.crearCarrera(carre);
        
        //crear materias
        Materia mate1 = new Materia(58, "Programación I", "Cuatrimestral", carre);
        Materia mate2 = new Materia(59, "Programación II", "Cuatrimestral", carre);
        Materia mate3 = new Materia(54, "Programación Avanzada", "Anual", carre);
        //guardar materias en bd
        control.crearMateria(mate1);
        control.crearMateria(mate2);
        control.crearMateria(mate3);
        //y agregamos materias a lista
        listaMaterias.add(mate1);
        listaMaterias.add(mate2);
        listaMaterias.add(mate3);
        
        // editamos lista de materias a nivel logico y bd
        carre.setListaMaterias(listaMaterias);
        control.editarCarrera(carre);
        
        //Crear alumno con carrera
        Alumno alu = new Alumno(2, "maria", "lopez",new Date(),carre);
        //guardamos el alumno en bd
        control.crearAlumno(alu);
        //vemos resultado
        System.out.println("-------------------------");
        System.out.println("---------------DATOS ALUMNO-------------");
        Alumno alu2 = control.traerAlumno(2);
        System.out.println("Alumno: "+alu2.getNombre() + " " + alu2.getApellido());
        System.out.println("Cursa la carrera de: " + alu2.getCarre().getNombre());
        
        
    }
}
