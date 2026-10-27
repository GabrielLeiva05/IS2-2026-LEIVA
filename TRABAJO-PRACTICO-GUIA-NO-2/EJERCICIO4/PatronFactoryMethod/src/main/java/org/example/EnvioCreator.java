package org.example;


//Esta va a ser abstracta.
//La clase abstracta no sabe que clase especifica implementa la interfaz
//De igual manera, logica en comun ya sea cualquier clase (?
public abstract class EnvioCreator {

    //Factory method
    protected abstract Envio crearEnvio();

    //Logica en comun que van a tener todos los tipos de envio
    public void procesarEnvio(){
        //Necesitamos crear las distintas implementaciones de crearEnvio
        Envio envio = crearEnvio();

        //enviarPaquete es el metodo de la interface Envio
        envio.enviarPaquete();
    }

}
