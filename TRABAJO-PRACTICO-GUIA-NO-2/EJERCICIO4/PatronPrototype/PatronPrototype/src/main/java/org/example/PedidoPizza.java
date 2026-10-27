package org.example;

import java.util.ArrayList;
import java.util.List;

public class PedidoPizza implements PrototipoPizza<PedidoPizza>{


    private String tamano;
    private String tipoMasa;
    private List<String> ingredientes;

    //
    public PedidoPizza(String tamano, String tipoMasa, List<String> ingredientes) {
        this.tamano = tamano;
        this.tipoMasa = tipoMasa;
        this.ingredientes = new ArrayList<>(ingredientes);
    }

    //Esta es la clonacion, observar que le pasamos un objeto de tipo PedidoPizza
    public PedidoPizza(PedidoPizza prototipo){
        this.tamano = prototipo.tamano;
        this.tipoMasa = prototipo.tipoMasa;
        this.ingredientes = new ArrayList<>(prototipo.ingredientes);
    }

    @Override
    public PedidoPizza clonar() {
        return new PedidoPizza(this);
    }

    public void agregarIngrediente(String ingrediente){
        this.ingredientes.add(ingrediente);
    }

    @Override
    public String toString() {
        return "PedidoPizza{" +
                "tamano='" + tamano + '\'' +
                ", tipoMasa='" + tipoMasa + '\'' +
                ", ingredientes=" + ingredientes +
                '}';
    }
}
