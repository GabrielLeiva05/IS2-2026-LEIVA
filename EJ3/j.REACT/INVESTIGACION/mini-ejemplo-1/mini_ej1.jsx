function saludar() {
    const nombre = "pepe";
    const hora = new Date.getHours();

    return (
        <div className="contenedor">
            <h1>Hola, {nombre}!!!</h1>
            {hora < 12 ? <p>Buenos días</p> : <p>Buenas tardes</p>}
        </div>
    )
}