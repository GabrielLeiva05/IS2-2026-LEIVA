import React, { useState, useEffect } from 'react'

function Ejemplo3() {
    const [hora, setHora] = useState(new Date())

    useEffect(() => {
        // Actualiza la hora cada 1000ms.
        const intervalo = setInterval(() =>{
            setHora(new Date())
        }, 1000)

        return () => clearInterval(i)
    }, [])

    return (
        <div>
            <h2>Hora actual: {hora.toLocaleTimeString()}</h2>
        </div>
    )
}

export default Ejemplo3