package com.furnish.erp.service.impl;

import com.furnish.erp.domain.Proveedor;
import com.furnish.erp.exception.ResourceNotFoundException;
import com.furnish.erp.repository.ProveedorRepository;
import com.furnish.erp.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByEliminadoFalseOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor buscarPorId(Long id) {
        return proveedorRepository.findByIdProveedorAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el proveedor con id " + id));
    }

    @Override
    public Proveedor registrarProveedor(Proveedor proveedor) {
        proveedor.setEliminado(false);
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor editarProveedor(Long id, Proveedor datosActualizados) {
        Proveedor existente = buscarPorId(id);
        existente.setNombre(datosActualizados.getNombre());
        existente.setTelefono(datosActualizados.getTelefono());
        existente.setDireccion(datosActualizados.getDireccion());
        existente.setCorreo(datosActualizados.getCorreo());
        return proveedorRepository.save(existente);
    }

    @Override
    public void eliminarProveedor(Long id) {
        Proveedor proveedor = buscarPorId(id);
        proveedor.setEliminado(true);
        proveedorRepository.save(proveedor);
    }
}
