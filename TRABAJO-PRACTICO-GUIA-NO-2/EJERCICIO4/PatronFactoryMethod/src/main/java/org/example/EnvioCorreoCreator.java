package org.example;

public class EnvioCorreoCreator extends EnvioCreator{

    @Override
    protected Envio crearEnvio() {
        return new EnvioCorreo();
    }
}
