package com.mycompany.pruebajpa.logica;

import com.mycompany.pruebajpa.persistencia.ControladoraPersistencia;

/**
 *
 * @author Gabriel
 */
public class Controladora {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    
    public void crearAlumno(Alumno alu) {
        controlPersis.crearAlumno(alu);
    }
    
    public void eliminarAlumno(int id) {
        controlPersis.eliminarAlumno(id);
    }
    
    public void editarAlumno(Alumno alu) {
        controlPersis.editarAlumno(alu);
    }
}
