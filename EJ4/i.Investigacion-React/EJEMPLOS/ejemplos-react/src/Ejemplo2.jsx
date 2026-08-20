import React, { useState } from "react";

function Ejemplo2() {
    const [nombre, setNombre] = useState('')
    const [mensaje, setMensaje] = useState('')

    // onChange: se dispara cada vez que el usuario escribe
    const manejarCambio = (e) => {
        setNombre(e.target.value)
    }

    // onSubmit: se dispara al enviar un formulario
    const manejarEnvio = (e) => {
        e.preventDefault()
        setMensaje(`Hola ${nombre}`)
    }

    return (
        <form onSubmit={manejarEnvio}>
            <input
            type="text"
            value={nombre}
            onChange={manejarCambio}
            placeholder="Tu nombre:"
            />
            <button type="submit">Saludar</button>
            {mensaje && <p>{mensaje}</p>}
        </form>
    )
}

export default Ejemplo2