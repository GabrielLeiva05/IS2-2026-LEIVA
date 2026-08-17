function Boton({text}) {
    return <button className="btn-buttonText">{text}</button>
}

function Ejemplo2() {
    return (
        <div className="tarjeta">
            <h2>Bienvenido</h2>
            <p className="parrafo">Esta componente es la de tarjeta.</p>
            <Boton text="Holaaaaa" />
        </div>
    )
}

export default Ejemplo2