import { useState } from "react";

function Ejemplo1() {

    const [contador, setContador] = useState(0);

    return (
        <section>
            <h2>Ejemplo1 - React DOM</h2>
            <p>Este contenido está siendo renderizado por React DOM.</p>
            <p>Contador: {contador}</p>

            <button onClick={() => setContador(contador+1)}>Incrementar</button>
        </section>
    );

}

export default Ejemplo1;