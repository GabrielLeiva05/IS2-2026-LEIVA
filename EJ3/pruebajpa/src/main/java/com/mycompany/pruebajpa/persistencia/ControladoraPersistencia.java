package com.mycompany.pruebajpa.persistencia;

import com.mycompany.pruebajpa.logica.Alumno;

/**
 *
 * @author Gabriel
 */
public class ControladoraPersistencia {
    AlumnoJpaController aluJpa = new AlumnoJpaController();

    public void crearAlumno(Alumno alu) {
        aluJpa.create(alu);
    }
    
}
