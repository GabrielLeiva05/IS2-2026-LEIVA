import React from "react";
import { useTema } from "./TemaContext";

function Encabezado() {
    return (
        <header>
            <h2>Panel de Control</h2>
            <BotonTema />
        </header>
    );
}

function BotonTema() {
    const {tema,cambiarTema} = useTema();

    return (
        <button onClick={cambiarTema}>
            Tema Actual: {tema} - Haga click para cambiarlo
        </button>
    );
}

export default Encabezado;