import { useState } from "react";
import "./App.css";
import { InputCiudad } from "./components/InputCiudad";
import { ObtenerClima } from "./api/ObtenerClima";
import { ButtonConsultar } from "./components/ButtonConsultar";
import { DisplayClima } from "./components/DisplayClima";

function App() {
  const [ciudad, setCiudad] = useState("");
  const [clima, setClima] = useState("");

  const handlerObtenerClima = () => {
    ObtenerClima(ciudad, setClima);
  };

  return (
    <>
      <div className="container">
        <h1 className="mt-3"> Busca el clima Actual</h1>
        <InputCiudad ciudad={ciudad} setCiudad={setCiudad} />
        <ButtonConsultar obtenerClima={handlerObtenerClima} />
        <DisplayClima clima={clima} />
      </div>
    </>
  );
}

export default App;