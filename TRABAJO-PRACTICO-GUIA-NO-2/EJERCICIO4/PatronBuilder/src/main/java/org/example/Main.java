package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Builder Concreto
        BuilderConcretoCasa builder = new BuilderConcretoCasa();

        //Director (opcional)
        BuilderDirector director = new BuilderDirector(builder);

        director.construirCasaDosPisosMaterial();
        Casa casa1 = builder.obtenerResultado();
        System.out.println("Casa 1:" + casa1.toString());


        //Observar como builder, deja de tener referencia a casa1 y pasa a tener uan nueva
        director.construirCasaLujosa();
        Casa casa2 = builder.obtenerResultado();
        System.out.println("Casa 2:" + casa2.toString());
    }
}