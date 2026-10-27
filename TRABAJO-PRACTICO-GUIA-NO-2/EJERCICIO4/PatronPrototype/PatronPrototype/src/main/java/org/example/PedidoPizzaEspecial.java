package org.example;

import java.util.List;

public class PedidoPizzaEspecial extends PedidoPizza {

   private boolean bordeRelleno;

    public PedidoPizzaEspecial(String tamano, String tipoMasa, List<String> ingredientes, boolean bordeRelleno) {
        super(tamano, tipoMasa, ingredientes);
        this.bordeRelleno = bordeRelleno;
    }

    public PedidoPizzaEspecial(PedidoPizzaEspecial prototipo) {
        super(prototipo);
        this.bordeRelleno = prototipo.bordeRelleno;
    }

    @Override
    public PedidoPizzaEspecial clonar() {
        return new PedidoPizzaEspecial(this);
    }

    @Override
    public String toString() {
        return "PedidoPizzaEspecial{" +
                "bordeRelleno=" + bordeRelleno +
                " " + super.toString() +
                '}';
    }
}
