// ------------------------------------
// Ejemplo practico para mostrar como funcionan los hooks.
// ------------------------------------

import React, {useState, useMemo, useCallback} from "react";

const ListaProductos = React.memo(({productos, onSeleccionar}) => {
    console.log("Renderizando ListaProductos");

    return (
        <ul>
            {productos.map((producto) => (
                <li key={producto.id} onClick={() => onSeleccionar(producto.id)}>
                    {producto.nombre} - ${producto.precio}
                </li>
            ))}
        </ul>
    );


});

export default ListaProductos;