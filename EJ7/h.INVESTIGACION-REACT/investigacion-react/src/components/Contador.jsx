import { useState } from "react";

function Contador() {
    const [contador, setContador] = useState(0);

    return (
        <div>
            <h1>Contador</h1>

            <p>Valor: {contador}</p>

            <button onClick={() => setContador(contador + 1)}>
                Incrementar
            </button>
        </div>
    );
}

export default Contador;