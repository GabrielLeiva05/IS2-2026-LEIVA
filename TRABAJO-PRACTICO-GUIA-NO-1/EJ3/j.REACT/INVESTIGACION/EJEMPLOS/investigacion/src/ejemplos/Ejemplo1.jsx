function Ejemplo1() {
    const nombre = "pepe";
    const hora = new Date().getHours();

    return (
        <div className="contenedor">
            <h1>Hola, {nombre}!!!</h1>
            {hora < 12 ? <p>Buenos días</p> : <p>Buenas tardes</p>}
        </div>
    )
}

// export default sirve para que cuando importemos en otro archivo no haga
// falta el uso de llaves.
export default Ejemplo1