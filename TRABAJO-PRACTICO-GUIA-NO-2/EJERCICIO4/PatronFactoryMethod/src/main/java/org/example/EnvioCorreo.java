package org.example;

public class EnvioCorreo implements Envio{

    @Override
    public void enviarPaquete() {
        System.out.println("Enviando paquete mediante correo postal");
    }
}
