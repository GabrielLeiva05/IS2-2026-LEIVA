import Acceso from "./components/Acceso";
import Contenido from "./components/Contenido";
import Menu from "./components/Menu";


function App() {
  return(
    <>
      <div className="dashboard">
        <Acceso />
        <Menu />
        <Contenido />
      </div>
    </>
  )
}

export default App;