import React, { useState } from "react";

function Ejemplo1() {
    const [contador, setContador] = useState(0)
    const incrementar = () => setContador(contador+1)
    const decrementar = () => setContador(contador-1)
    const reiniciarContador = () => setContador(0)

    return (
        <div>
            <h2>Valor Actual del Contador: {contador}</h2>
            <button onClick={incrementar}>Incrementar</button>
            <button onClick={decrementar}>Decrementar</button>
            <button onClick={reiniciarContador}>Reiniciar</button>
        </div>
    )
}

export default Ejemplo1