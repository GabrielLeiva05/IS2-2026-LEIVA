package com.furnish.erp.service;

import com.furnish.erp.domain.Empleado;

import java.util.List;

/**
 * ============================================================================
 * Interfaz de servicio: EmpleadoService
 * ============================================================================
 * Contiene las reglas de negocio de la clase "Empleado" del diagrama:
 * registrarEmpleado(), editarEmpleado(), eliminarEmpleado(). Se define como
 * interfaz (y no solo la implementación) siguiendo el principio de
 * inversión de dependencias: el Controller depende de esta abstracción,
 * no de una implementación concreta, lo que facilita testing (mocks) y
 * eventuales cambios de implementación sin tocar los controladores.
 *
 * REGLA DE ARQUITECTURA (pedida por el cliente): esta es la ÚNICA capa
 * autorizada a decidir reglas de negocio (validaciones de dominio, unicidad,
 * bajas lógicas, etc.). Los Controllers solo orquestan HTTP <-> Service, y
 * los Repository solo hacen acceso a datos.
 * ============================================================================
 */
public interface EmpleadoService {

    /** Lista todos los empleados activos (no eliminados), ordenados por nombre. */
    List<Empleado> listarActivos();

    /** Busca un empleado activo por id; lanza ResourceNotFoundException si no existe. */
    Empleado buscarPorId(Long id);

    /** Regla de negocio: valida que el DNI no esté duplicado antes de guardar. */
    Empleado registrarEmpleado(Empleado empleado);

    /** Regla de negocio: actualiza los datos editables de un empleado existente. */
    Empleado editarEmpleado(Long id, Empleado datosActualizados);

    /** Baja lógica: marca eliminado=true en lugar de borrar físicamente el
     *  registro, preservando la integridad histórica de las órdenes de compra
     *  que ese empleado haya generado. */
    void eliminarEmpleado(Long id);
}
