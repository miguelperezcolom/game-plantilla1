package io.mateu.gamemaker;


import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.List;

public class Juego {

    private final MyGdxGame game;
    private float ancho;
    private float alto;

    private List<Nivel> niveles = new ArrayList<>();
    private Nivel nivelActual;
    private Menu menu;


    public Juego(MyGdxGame game, XmlReader.Element xml) {
        this.game = game;
        for (XmlReader.Element e : xml.getChildrenByName("nivel")) niveles.add(new Nivel(game, e));
        menu = new Menu(this, game, xml.getChildByName("menu"));
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public Nivel getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(Nivel nivelActual) {
        this.nivelActual = nivelActual;
        game.setScreen((nivelActual != null)?nivelActual:menu);
        if (nivelActual != null) nivelActual.start();
    }

    public void start() {
        game.setScreen((nivelActual != null)?nivelActual:menu);
    }

}
