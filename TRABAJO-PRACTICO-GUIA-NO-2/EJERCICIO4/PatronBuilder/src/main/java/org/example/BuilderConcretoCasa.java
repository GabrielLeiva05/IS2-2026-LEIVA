package org.example;

import java.util.ArrayList;
import java.util.List;

//builder concreto
public class BuilderConcretoCasa implements BuilderCasa{

    private Casa resultado;
    private List<String> extras = new ArrayList<>();
    @Override
    public void reiniciar() {
        this.resultado = new Casa();
        this.extras = new ArrayList<>();
    }

    @Override
    public void construirEstructura(String tipoEstructura) {
        resultado.setTipoEstructura(tipoEstructura);
    }

    @Override
    public void construirPisos(int cantidad) {
        resultado.setPisos(cantidad);
    }

    @Override
    public void construirPiscina(boolean siONo) {
        resultado.setPiscina(siONo);
    }

    @Override
    public void construirGarage(boolean siONo) {
        resultado.setGarage(siONo);
    }

    @Override
    public void construirJardin(boolean siONo) {
        resultado.setJardin(siONo);
    }

    @Override
    public void agregarExtra(String extra) {
        this.extras.add(extra);
        resultado.setExtras(extras);
    }

    public Casa obtenerResultado(){
        return resultado;
    }
}
