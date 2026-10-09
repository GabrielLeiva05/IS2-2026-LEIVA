import React from 'react';
import {traducirDescripcion} from './Traduccion';

export const DisplayClima = ({ clima }) => {
    try {
        if (!clima) {
            return <div>No hay datos de clima disponibles.</div>;
        }
        const climaData = JSON.parse(clima);

        const temperaturaCelsius = climaData.main.temp - 273.15; // Convertir de Kelvin a Celsius

        return (
            <>
                <div className="clima">
                    <table className="table table-striped table-bordered">
                        <thead className="thead-dark">
                            <tr>
                                <th scope="col">Ciudad</th>
                                <th scope="col">Temperatura (°C)</th>
                                <th scope="col">Descripción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{climaData.name}</td>
                                <td>{temperaturaCelsius.toFixed(2)}</td>
                                <td>{traducirDescripcion(climaData.weather[0].description)}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </>
        );
    } catch (error) {
        console.error('Error parsing clima data:', error);
        return <div>Error al mostrar los datos de clima.</div>;
    }

}
