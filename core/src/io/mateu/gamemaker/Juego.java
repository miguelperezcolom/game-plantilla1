package io.mateu.gamemaker;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.List;

public class Juego {

    private final Preferences prefs;
    public Skin skin;
    private final MyGdxGame game;
    private static Juego instancia;


    private Sound levelUpSound;

    private float ancho;
    private float alto;

    private List<Nivel> niveles = new ArrayList<>();
    private Nivel nivelActual;
    private Menu menu;
    public FinPartida finPartida;

    public int puntos = 0;
    public int vidas = 3;
    private AssetManager assetManager;


    public Juego(MyGdxGame game, XmlReader.Element xml) {
        this.instancia = this;
        this.game = game;
        skin = new Skin(Gdx.files.internal("skin/freezing-ui.json"));

        cargarSonidos(xml);

        for (XmlReader.Element e : xml.getChildrenByName("nivel")) niveles.add(new Nivel(game, e));

        menu = new Menu(this, game, xml.getChildByName("menu"));
        finPartida = new FinPartida(this, game, xml.getChildByName("menu"));


        if (xml.hasAttribute("sonidoRecord")) {
            levelUpSound = assetManager.get(xml.getAttribute("sonidoRecord"));
        }

        prefs = Gdx.app.getPreferences("Mis preferencias");
    }

    private void cargarSonidos(XmlReader.Element xml) {

        assetManager = new AssetManager();

        if (xml.hasAttribute("sonidoRecord")) {
            assetManager.load(xml.getAttribute("sonidoRecord"), Sound.class);
        }

        for (XmlReader.Element en : xml.getChildrenByName("nivel")) {
            if (en.hasAttribute("musica")) {
                assetManager.load(xml.getChildByName("musica").getAttribute("src"), Music.class);
            }

            for (String n : new String[] {"jugador", "malo", "balaamiga", "balaenemiga"}) {

                for (XmlReader.Element ex : en.getChildrenByName(n)) {
                    if (ex.hasAttribute("sonidoCreado")) {
                        assetManager.load(ex.getAttribute("sonidoCreado"), Sound.class);
                    }
                    if (ex.hasAttribute("sonidoDestruido")) {
                        assetManager.load(ex.getAttribute("sonidoDestruido"), Sound.class);
                    }
                }

            }


        }

        assetManager.finishLoading();


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

    public static Juego get() {
        return instancia;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public Sound getLevelUpSound() {
        return levelUpSound;
    }

    public void setLevelUpSound(Sound levelUpSound) {
        this.levelUpSound = levelUpSound;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
