package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Patron Abstract Factory nos llega a servir
        //para el caso de cuando sabemos que tipos de creaciones vamos a tener

        //Se selecciona una familia de productos
        OutfitFactory factory = new OutfitClasicoFactory();

        //Cliente trabaja sin saber las clases concretas
        TiendaRopa tienda = new TiendaRopa(factory);

        //en caso de cambiar, solo se modificaria la linea 11 por otro Factory especifico.
        tienda.mostrarOutfit();

        //Abstract Factory esta construido sobre Factory Method.

    }
}