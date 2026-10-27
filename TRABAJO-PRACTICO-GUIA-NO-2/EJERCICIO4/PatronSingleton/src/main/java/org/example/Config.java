package org.example;

public class Config {
    //Las 3 componentes de un singleton
    //una var. estatica
    private static Config instancia;

    //clase con constructor privado

    private Config () {
        System.out.println("Inicializando config en el sistema");
    }

    //un metodo publico que sea estatico
    public static Config getInstancia(){
        if (instancia == null){
            instancia = new Config();
        }
        return instancia;
    }
}
