import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import Ejemplo1 from './Ejemplo1'
import Ejemplo2 from './Ejemplo2'
import Ejemplo3 from './Ejemplo3'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div>
      <Ejemplo1 />
      <hr />
      <Ejemplo2 />
      <hr />
      <Ejemplo3 />
    </div>
  )
}

export default App
