package org.example;

public class EnvioCamionCreator extends EnvioCreator{
    @Override
    protected Envio crearEnvio() {
        return new EnvioCamion();
    }
}
