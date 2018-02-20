package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Nivel extends ScreenAdapter {

    private final Music musica;
    private final float ancho;
    private final float alto;
    private final OrthographicCamera camara;
    private final MyGdxGame game;
    private Peon jugador;
    List<Peon> tiposMalos = new ArrayList<>();

    private List<Peon> jugadores = new ArrayList<>();

    public Nivel(MyGdxGame game, Element xml) {

        this.game = game;

        ancho = Float.parseFloat(xml.getAttributeValue("ancho"));
        alto = Float.parseFloat(xml.getAttributeValue("alto"));

        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);

        jugador = new Peon(xml.getChild("jugador"));

        for (Element e : xml.getChildren("malo")) tiposMalos.add(new Peon(e));

        musica = Gdx.audio.newMusic(Gdx.files.internal(xml.getChild("musica").getAttributeValue("src")));
        musica.setLooping(true);


    }

    public void start() {
        musica.play();
    }

    public void stop() {
        musica.stop();
    }


    public Peon getJugador() {
        return jugador;
    }

    public void setJugador(Peon jugador) {
        this.jugador = jugador;
    }

    public List<Peon> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Peon> jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		/*
		batch.begin();
		batch.draw(imgJugador, 0, 0);
		batch.end();
		*/

        game.batch.setProjectionMatrix(camara.combined);
        game.batch.begin();
        jugador.getSprite().draw(game.batch);
        //game.batch.draw(jugador.getSprite(), jugador.getX(), jugador.getY());
        game.batch.end();


        if (Gdx.input.isTouched()) {

            Vector3 posicionToque = new Vector3();
            posicionToque.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(posicionToque);
            jugador.setX(posicionToque.x);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) jugador.moveX(-20);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) jugador.moveX(20);

        if (jugador.getX() < 0) jugador.setX(0);
        if (jugador.getX() > ancho - jugador.getAncho()) jugador.setX(ancho - jugador.getAncho());
    }


}
