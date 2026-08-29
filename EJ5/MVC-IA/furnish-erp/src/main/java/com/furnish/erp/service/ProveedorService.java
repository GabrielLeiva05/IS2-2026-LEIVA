package com.furnish.erp.service;

import com.furnish.erp.domain.Proveedor;

import java.util.List;

public interface ProveedorService {

    List<Proveedor> listarActivos();

    Proveedor buscarPorId(Long id);

    Proveedor registrarProveedor(Proveedor proveedor);

    Proveedor editarProveedor(Long id, Proveedor datosActualizados);

    void eliminarProveedor(Long id);
}
