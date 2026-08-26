package com.nexora.gestion.service;

import com.nexora.gestion.model.Producto;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de reglas de negocio de Producto: registrarProducto(),
 * editarProducto() y eliminarProducto() del diagrama UML.
 */
public interface ProductoService {

    /** Alta de un producto nuevo (regla: nombre obligatorio, precio > 0). */
    Producto registrarProducto(Producto producto);

    /** Edición de un producto existente (regla: el producto debe existir). */
    Producto editarProducto(Long id, Producto datosNuevos);

    /** Baja de un producto (regla: si tiene historial de ventas/movimientos
     *  se hace baja lógica -activo=false-; si no tiene historial, se borra
     *  físicamente). */
    void eliminarProducto(Long id);

    List<Producto> listarActivos();

    List<Producto> listarTodos();

    Optional<Producto> buscarPorId(Long id);

    List<Producto> buscarPorNombre(String nombre);
}
