package com.furnish.erp.service;

import com.furnish.erp.domain.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> listarActivos();

    List<Producto> buscarPorNombre(String texto);

    Producto buscarPorId(Long id);

    Producto cargarProducto(Producto producto);

    Producto editarProducto(Long id, Producto datosActualizados);

    /** Regla de negocio: no se permite eliminar (ni siquiera lógicamente) un
     *  producto que todavía tenga stock positivo, para evitar perder
     *  trazabilidad de mercadería existente. */
    void eliminarProducto(Long id);
}
