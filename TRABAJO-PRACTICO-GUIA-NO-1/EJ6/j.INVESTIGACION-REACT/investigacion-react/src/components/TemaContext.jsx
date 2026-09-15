// ---------------------------------
// Ejemplo para representar el contexto en React.
// ---------------------------------

import React, {createContext, useState, useContext} from "react";

const TemaContexto = createContext();

export function TemaProvider({children}) {
    const [tema, setTema] = useState("claro");

    const cambiarTema = () => {
        setTema((temaActual) => (temaActual === "claro" ? "oscuro" : "claro"));
    };

    return (
        <TemaContexto.Provider value={{tema, cambiarTema}}>
            {children}
        </TemaContexto.Provider>
    );
}

export function useTema() {
    return useContext(TemaContexto);
}