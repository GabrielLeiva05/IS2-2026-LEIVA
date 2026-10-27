package org.example;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //creamos el prototipo
        PedidoPizza prototipoMuzza = new PedidoPizza("grande", "normal", List.of("muzarella", "salsa de tomate"));


        //Realizamos una variante
        PedidoPizza muzzaAceitunas = prototipoMuzza.clonar();
        muzzaAceitunas.agregarIngrediente("aceitunas");

        //Podemos usar la variante tambien
        PedidoPizza muzzaJamon = prototipoMuzza.clonar();
        muzzaJamon.agregarIngrediente("jamon");

        //Prototipo Especial
        PedidoPizzaEspecial prototipoEspecial = new PedidoPizzaEspecial("mediana","fina", List.of("muzarella", "salsa de tomate"), true);

        //Variante de la especial
        PedidoPizzaEspecial especialConAnana = prototipoEspecial.clonar();
        especialConAnana.agregarIngrediente("anana");

        System.out.println("Prototipo base: " + prototipoMuzza);
        System.out.println("Variante 1: " + muzzaAceitunas);
        System.out.println("Variante 2: " + muzzaJamon);
        System.out.println("Prototipo Especial: " + prototipoEspecial);
        System.out.println("Variante Especial 1: " + especialConAnana);
    }
}