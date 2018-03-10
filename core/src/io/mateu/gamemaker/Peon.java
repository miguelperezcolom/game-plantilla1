package io.mateu.gamemaker;

import com.badlogic.gdx.utils.XmlReader;

public class Peon extends Actor {

    private Peon proyectil;

    public Peon(Plantilla plantilla) {
        super(plantilla);
    }

    public Peon getProyectil() {
        return proyectil;
    }

    public void setProyectil(Peon proyectil) {
        this.proyectil = proyectil;
    }
}
