package org.example;

//el Builder puede ser tanto una interface como una clase abstracta
public interface BuilderCasa {

    void reiniciar();
    void construirEstructura(String estructura);
    void construirPisos(int cantidad);
    void construirPiscina(boolean siONo);
    void construirGarage(boolean siONo);
    void construirJardin(boolean siONo);
    void agregarExtra(String extra);
}
