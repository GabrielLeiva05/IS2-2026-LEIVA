package com.furnish.erp.service.impl;

import com.furnish.erp.domain.Empleado;
import com.furnish.erp.exception.BusinessException;
import com.furnish.erp.exception.ResourceNotFoundException;
import com.furnish.erp.repository.EmpleadoRepository;
import com.furnish.erp.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 * Implementación: EmpleadoServiceImpl
 * ============================================================================
 * @Service marca esta clase como un Bean de la capa de negocio (Spring la
 * detecta por @ComponentScan y la inyecta donde se necesite vía @Autowired
 * implícito por constructor).
 *
 * @RequiredArgsConstructor (Lombok) genera un constructor con todos los
 * campos "final" (aquí, el repository) -> Spring usa ESE constructor para
 * inyección de dependencias (inyección por constructor, la forma recomendada
 * frente a @Autowired en el campo, porque permite declarar el campo final,
 * facilita el testing con mocks y deja explícitas las dependencias).
 *
 * @Transactional a nivel de clase: cada método público se ejecuta dentro de
 * una transacción de base de datos administrada por Spring; si se lanza una
 * RuntimeException (por ejemplo BusinessException), la transacción hace
 * rollback automáticamente.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarActivos() {
        return empleadoRepository.findByEliminadoFalseOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado buscarPorId(Long id) {
        return empleadoRepository.findByIdEmpleadoAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el empleado con id " + id));
    }

    @Override
    public Empleado registrarEmpleado(Empleado empleado) {
        // Regla de negocio: el DNI es la clave natural del empleado, no puede
        // repetirse entre empleados activos ni inactivos (evita reingresar
        // datos duplicados por error).
        if (empleadoRepository.existsByDni(empleado.getDni())) {
            throw new BusinessException(
                    "Ya existe un empleado registrado con el DNI " + empleado.getDni());
        }
        empleado.setEliminado(false);
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado editarEmpleado(Long id, Empleado datosActualizados) {
        Empleado existente = buscarPorId(id);

        // Si cambia el DNI, se vuelve a validar unicidad contra otros empleados.
        if (!existente.getDni().equals(datosActualizados.getDni())
                && empleadoRepository.existsByDni(datosActualizados.getDni())) {
            throw new BusinessException(
                    "Ya existe un empleado registrado con el DNI " + datosActualizados.getDni());
        }

        existente.setNombre(datosActualizados.getNombre());
        existente.setDni(datosActualizados.getDni());
        existente.setCorreo(datosActualizados.getCorreo());
        existente.setCargo(datosActualizados.getCargo());
        return empleadoRepository.save(existente);
    }

    @Override
    public void eliminarEmpleado(Long id) {
        Empleado empleado = buscarPorId(id);
        // Baja lógica en lugar de DELETE físico: preserva la integridad
        // referencial con las órdenes de compra históricas del empleado.
        empleado.setEliminado(true);
        empleadoRepository.save(empleado);
    }
}
