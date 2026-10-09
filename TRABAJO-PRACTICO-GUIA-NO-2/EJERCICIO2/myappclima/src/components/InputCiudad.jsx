import React from "react";

export const InputCiudad = ({ ciudad, setCiudad }) => {
  return (
    <>
      <input
        className="btn btn-white"
        type="text"
        placeholder="Ingrese ciudad"
        value={ciudad}
        onChange={(e) => setCiudad(e.target.value)}
      />
    </>
  );
};
