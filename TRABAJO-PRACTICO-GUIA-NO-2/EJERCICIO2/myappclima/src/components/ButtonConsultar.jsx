export const ButtonConsultar = ({ obtenerClima }) => {
  return (
    <>
      <button className="btn btn-primary" onClick={obtenerClima}>
        Consultar Clima
      </button>
    </>
  )
}