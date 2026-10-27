package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Es decir, nosotros creamos los creadores correspondientes
        //pero estos creadores a su vez, cuando llamen a procesarEnvio
        //sus clases tienen una implementacion distinta
        //respecto a lo que es crearEnvio()
        EnvioCreator envioCorreo = new EnvioCorreoCreator();
        envioCorreo.procesarEnvio();

        EnvioCreator envioMoto = new EnvioMotoCreator();
        envioMoto.procesarEnvio();

        EnvioCreator envioCamion = new EnvioCamionCreator();
        envioCamion.procesarEnvio();

        //¿se podria usar factory method por ejemplo
        //para la implementacion de tipos de pago?

    }
}