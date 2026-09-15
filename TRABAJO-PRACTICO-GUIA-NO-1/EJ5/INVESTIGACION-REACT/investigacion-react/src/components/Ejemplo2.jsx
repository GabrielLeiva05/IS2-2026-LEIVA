import { BrowserRouter, Routes, Route, Link } from "react-router";

function Inicio() {
    return (
        <div>
            <h3>Inicio</h3>
            <p>Esta es la página principal de React Router.</p>
        </div>
    );
}

function Informacion() {
    return (
        <div>
            <h3>Información</h3>
            <p>Esta página nos muestra cómo React Router cambia el componente según la URL.</p>
        </div>
    );
}

function Contacto() {
    return (
        <div>
            <h3>Contacto</h3>
            <p>Página de contacto.</p>
        </div>
    );
}

function Ejemplo2() {

    return (
        <BrowserRouter>
            <section>
                <h2>Ejemplo 2- React Router</h2>

                <nav>
                    <Link to={"/ejemplo2"}>
                        Inicio
                    </Link>{' | '}
                    <Link to={"/ejemplo2/informacion"}>
                        Informacion
                    </Link>{' | '}
                    <Link to={"/ejemplo2/contacto"}>
                        Contacto
                    </Link>
                </nav>

                <Routes>
                    <Route
                        path="/ejemplo2"
                        element={<Inicio/>}
                    />
                    <Route
                        path="/ejemplo2/informacion"
                        element={<Informacion/>}
                    />
                    <Route
                        path="/ejemplo2/contacto"
                        element={<Contacto/>}
                    />
                </Routes>

            </section>
        </BrowserRouter>
    );

}

export default Ejemplo2;