function Usuario({nombre, edad}) {
    return (
        <div className="usuario">
            <h3>El nombre de usuario es: {nombre}</h3>
            <h3>La edad del usuario es: {edad}</h3>
        </div>
    )
}

function Ejemplo3() {
    return (
        <div>
            <Usuario nombre="pepe" edad={21} />
            <Usuario nombre="jorge" edad={23} />
        </div>
    )
}

export default Ejemplo3