package org.example;

public class BuilderDirector {

    private BuilderCasa builder;

    public BuilderDirector(BuilderCasa builder){
        this.builder = builder;
    }

    public void cambiarBuilder(BuilderCasa builder) {
        this.builder = builder;
    }

    //Casa de material, 2 pisos, con garage y sin piscina
    public void construirCasaDosPisosMaterial() {
        builder.reiniciar();
        builder.construirEstructura("Material");
        builder.construirPisos(2);
        builder.construirPiscina(false);
        builder.construirGarage(true);
        builder.construirJardin(false);
        builder.agregarExtra("Balcon");
    }

    //¿La ventaja acaso es que puedo crear un nuevo
    //metodo como yo quiera? y simplemente paso los argumentos correspondientes
    // a los respectivos metodos.

    public void construirCasaLujosa() {
        builder.reiniciar();
        builder.construirEstructura("Cuarzo");
        builder.construirPisos(4);

        builder.construirGarage(true);

        builder.agregarExtra("Balcon");
        builder.agregarExtra("Cancha de Futbol");
    }
}
