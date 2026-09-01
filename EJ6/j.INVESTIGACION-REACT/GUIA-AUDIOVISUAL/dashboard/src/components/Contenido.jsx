import Aspirantes from "./Aspirantes";
import Preventa from "./Preventa";
import Profesiones from "./Profesiones";

function Contenido() {
    return (
        <>
            <main className="content-wrap">
                <Preventa />
                <Aspirantes />
                <Profesiones />
            </main>
        </>
    )
}

export default Contenido;