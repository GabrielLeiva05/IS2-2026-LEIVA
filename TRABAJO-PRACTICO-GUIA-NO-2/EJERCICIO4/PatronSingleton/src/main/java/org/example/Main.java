package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //Se representan como ejemplo dos funcionalidades distintas del sistema.
        //Autenticacion del sistema
        System.out.println("Modulo de autenticacion");
        Config config1 = Config.getInstancia();


        //Reportes
        System.out.println("Modulo de reportes");
        Config config2 = Config.getInstancia();

        //Comprobacion
        System.out.println("¿las referencias son iguales?");
        System.out.println(config1 ==  config2);

        System.out.println(config1.getInstancia());
        System.out.println(config2);
    }
}