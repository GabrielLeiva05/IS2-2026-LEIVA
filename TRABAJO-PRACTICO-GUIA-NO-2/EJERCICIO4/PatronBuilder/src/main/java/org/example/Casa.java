package org.example;

import java.util.ArrayList;
import java.util.List;

//el objeto a construir
public class Casa {
    private String tipoEstructura;
    private int pisos;
    private boolean piscina;
    private boolean garage;
    private boolean jardin;
    private List<String> extras = new ArrayList<>();


    public void setTipoEstructura(String tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }

    public void setPisos(int pisos) {
        this.pisos = pisos;
    }

    public void setPiscina(boolean piscina) {
        this.piscina = piscina;
    }

    public void setGarage(boolean garage) {
        this.garage = garage;
    }

    public void setJardin(boolean jardin) {
        this.jardin = jardin;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "tipoEstructura='" + tipoEstructura + '\'' +
                ", pisos=" + pisos +
                ", piscina=" + piscina +
                ", garage=" + garage +
                ", jardin=" + jardin +
                ", extras=" + extras +
                '}';
    }
}
