import { useCallback, useMemo, useState } from "react";
import ListaProductos from "./ListaProductos";
import { TemaProvider, useTema } from "./TemaContext";
import Encabezado from "./Encabezado";

function ContenidoPrincipal() {
    
    const {tema} = useTema();
    const [contador, setContador] = useState(0);
    const [filtro, setFiltro] = useState("");

    // Hacemos la simulacion de los datos con un arreglo
    const productos = [
        {id: 1, nombre: "Notebook", precio: 500},
        {id: 2, nombre: "Mouse", precio: 200},
        {id: 3, nombre: "Teclado", precio: 250},
        {id: 4, nombre: "Monitor", precio: 400}
    ];

    const productosFiltrados = useMemo(() => {
        console.log("Calculando productos filtrados");
        return productos.filter((producto) =>
            producto.nombre.toLowerCase().includes(filtro.toLowerCase())
        );
    }, [filtro]);

    const handleSeleccionar = useCallback((id) => {
        console.log(`Producto seleccionado con id: ${id}`);
    }, []);

    const estilos = {
        backgroundColor: tema == "claro" ? "#fff" : "#222",
        color : tema === "claro" ? "#000" : "#fff",
        minHeight: "100vh",
        padding: "20px",
        transition: "all 0.3s ease",
    };

    return (
        <div style={estilos}>
            <Encabezado />
            <h2>Ejemplo de Hooks de Rendimiento con Contexto</h2>

            <div>
                <button onClick={() => setContador(contador+1)}>Contador: {contador}</button>
                <p>Al hacer click, el componente App se re-renderiza, pero ListaProductos
                    no debería volver a renderizarse porque usamos React.Memo y useCallback.
                </p>
            </div>

            <input
                type="text"
                placeholder="Filtrar productos"
                value={filtro}
                onChange={(e) => setFiltro(e.target.value)}
            />

            <ListaProductos 
                productos={productosFiltrados}
                onSeleccionar ={handleSeleccionar}
            />

        </div>
        
    );

}

function App() {
    return (
        <TemaProvider>
            <ContenidoPrincipal />
        </TemaProvider>
    );
}

export default App;