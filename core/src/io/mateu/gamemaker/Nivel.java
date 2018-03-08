package io.mateu.gamemaker;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nivel extends ScreenAdapter {

    private final Stage stage;
    private final Touchpad tp;
    private final Stage stageFondo;
    private final Label lscore;
    private final Label lvidas;
    private Plantilla balaAmiga;
    private Plantilla balaEnemiga;
    private Music musica;
    private final float ancho;
    private final float alto;
    private final OrthographicCamera camara;
    private final MyGdxGame game;
    private Peon jugador;
    List<Plantilla> tiposMalos = new ArrayList<>();

    private List<Peon> jugadores = new ArrayList<>();
    protected List<Peon> malos = new ArrayList<>();
    private List<Peon> balasAmigas = new ArrayList<>();
    protected List<Peon> balasEnemigas = new ArrayList<>();
    private Map<Plantilla, Long> ultimoDrop;


    private String[] imgsFondo = {
            "bg1.png"
            /*
      "parallax-space-backgound.png",
              "parallax-space-big-planet.png" ,
              "parallax-space-far-planets.png" ,
              "parallax-space-ring-planet.png" ,
              "parallax-space-stars.png"
              */
    };


    public Nivel(MyGdxGame game, XmlReader.Element xml) {

        this.game = game;

        ancho = Float.parseFloat(xml.getAttribute("ancho"));
        alto = Float.parseFloat(xml.getAttribute("alto"));

        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);




        jugador = new Peon(xml.getChildByName("jugador"));

        jugador.setX(ancho / 2);
        jugador.setY(alto / 4);

        balaAmiga = new Plantilla(xml.getChildByName("balaamiga"));
        balaEnemiga = new Plantilla(xml.getChildByName("balaenemiga"));

        for (XmlReader.Element e : xml.getChildrenByName("malo")) tiposMalos.add(new Plantilla(e));

        if (xml.hasAttribute("musica")) {
            musica = Gdx.audio.newMusic(Gdx.files.internal(xml.getChildByName("musica").getAttribute("src")));
            musica.setLooping(true);
        }

        ultimoDrop = new HashMap<>();
        for (Plantilla p : tiposMalos) ultimoDrop.put(p, TimeUtils.nanoTime());


        stage = new Stage();

        TextButton b;
        stage.addActor(b = new TextButton("||", Juego.get().skin));
        b.setWidth(100f);
        b.setHeight(30f);
        b.setPosition(20, Gdx.graphics.getHeight() - 70f);
        b.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("pausa");


                Juego.get().setNivelActual(null);

            }
        });

        stage.addActor(lscore = new Label("Score: " + Juego.get().puntos, Juego.get().skin));
        lscore.setWidth(100f);
        lscore.setHeight(20f);
        lscore.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 30f);

        stage.addActor(lvidas = new Label("Lifes: " + Juego.get().vidas, Juego.get().skin));
        lvidas.setWidth(100f);
        lvidas.setHeight(20f);
        lvidas.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 60f);

        
        stage.addActor(b = new TextButton("F", Juego.get().skin));
        b.setWidth(100f);
        b.setHeight(30f);
        b.setPosition(20, 75);
        b.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                disparar();

            }
        });



        tp = new Touchpad(0, getTouchPadStyle());
        tp.setBounds(Gdx.graphics.getWidth() - 160, 10, 150, 150);
        stage.addActor(tp);


        /*
        tp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                jugador.acelerarX(20 * tp.getKnobPercentX());
                jugador.acelerarY(20 * tp.getKnobPercentY());
                System.out.println("tp=" + tp.getKnobPercentX() + "," + tp.getKnobPercentY());
            }
        });
        */




        //background

        Array<Texture> textures = new Array<Texture>();
        for(int i = 0; i < imgsFondo.length; i++){
            textures.add(new Texture(Gdx.files.internal("space_shooter_art_pack_01/backgrounds/" + imgsFondo[i])));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1f);

        stageFondo = new Stage();

        stageFondo.addActor(parallaxBackground);

    }

    @Override
    public void dispose() {
        System.out.println("dispose de nivel");
        super.dispose();
        stage.dispose();
        musica.dispose();
        //todo: falta disposar los actores
    }

    public void start() {
        if (musica != null) musica.play();
    }

    public void stop() {
        if (musica != null) musica.stop();
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


        camara.update();
        game.batch.setProjectionMatrix(camara.combined);

        stageFondo.act(delta);

        stageFondo.draw();

        game.batch.begin();

        for (Peon malo : malos) malo.draw(game.batch);

        for (Peon bala : balasAmigas) bala.draw(game.batch);

        for (Peon bala : balasEnemigas) bala.draw(game.batch);

        jugador.draw(game.batch);

        game.batch.end();


        lscore.setText("Score: " + Juego.get().puntos);
        lvidas.setText("Lifes: " + Juego.get().vidas);
        //stage.act();
        stage.draw();



        jugador.act(delta);
        for (Peon malo : malos) malo.act(delta);

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) jugador.acelerarX(-10);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) jugador.acelerarX(10);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) jugador.acelerarY(-10);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) jugador.acelerarY(10);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) disparar();


        jugador.acelerarX(30 * tp.getKnobPercentX());
        jugador.acelerarY(30 * tp.getKnobPercentY());



        if (jugador.getX() < jugador.getAncho()) {
            jugador.setX(jugador.getAncho());
            jugador.pararX();
        }
        if (jugador.getX() > ancho - jugador.getAncho()) {
            jugador.setX(ancho - jugador.getAncho());
            jugador.pararX();
        }

        if (jugador.getY() < jugador.getAlto()) {
            jugador.setY(jugador.getAlto());
            jugador.pararY();
        }
        if (jugador.getY() > alto - jugador.getAlto()) {
            jugador.setY(alto - jugador.getAlto());
            jugador.pararY();
        }


        long ahora = TimeUtils.nanoTime();
        for (Plantilla p : tiposMalos) if (ahora - ultimoDrop.get(p) > p.getFrecuencia()) crearMalo(p);

        for (Peon m : malos) if (m.isActivo() && MathUtils.random(0, 300) == 0) disparar(m, balaEnemiga);

        List<Peon> borrar = new ArrayList<>();

        for (Peon bala : balasAmigas) {
            bala.act(delta);
            if (bala.getY() > alto) borrar.add(bala);
        }

        for (Peon bala : balasEnemigas) {
            bala.act(delta);
            if (bala.getY() < 0) borrar.add(bala);
        }

        for (Peon malo : malos) {
            malo.act(delta);
            if (malo.getY() < 0) borrar.add(malo);

            for (Peon bala : balasAmigas) if (!borrar.contains(bala) && bala.getRect().overlaps(malo.getRect())) {
                malo.destruido();

                Juego.get().puntos += 10;

                bala.destruido();
                borrar.add(bala);

                break;
            }


            if (jugador.isActivo() && malo.isActivo() && !borrar.contains(malo) && malo.getRect().overlaps(jugador.getRect())) {
                malo.destruido();
                System.out.println("hit");

                jugador.hitten(malo);

                Juego.get().vidas --;

                Gdx.input.vibrate(500);

            }


            if (Juego.get().vidas <= 0 && !game.getScreen().equals(Juego.get().finPartida) && jugador.isActivo()) {

                jugador.destruido();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(Juego.get().finPartida);
                    }
                }, 5);
            }

        }


        for (Peon malo : balasEnemigas) {
            if (jugador.isActivo() && !borrar.contains(malo) && malo.getRect().overlaps(jugador.getRect())) {
                malo.destruido();
                borrar.add(malo);
                System.out.println("hit");

                jugador.hitten(malo);

                Juego.get().vidas --;

                Gdx.input.vibrate(500);
            }
        }

        malos.removeAll(borrar);
        balasAmigas.removeAll(borrar);
        balasEnemigas.removeAll(borrar);
    }

    private void disparar(Peon malo, Plantilla balaEnemiga) {
        System.out.println("disparar");
        Peon bala;
        balasEnemigas.add(bala = new Peon(balaEnemiga));
        bala.setX(malo.getX());
        bala.setY(malo.getY());
        float v = 400;
        Vector2 aux = (malo.getPlantilla().isDisparoDirigido())?new Vector2(jugador.getX() - malo.getX(), jugador.getY() - malo.getY()).nor():new Vector2(0, -1);
        bala.setV(aux.x * v, aux.y * v);
        bala.setRotacion(aux.angle());
    }

    private void disparar() {
        System.out.println("disparar");
        Peon bala;
        balasAmigas.add(bala = new Peon(balaAmiga));
        bala.setX(jugador.getX());
        bala.setY(jugador.getY());
        bala.setV(0, 400);
    }

    public void crearMalo(Plantilla plantilla) {

        ultimoDrop.put(plantilla, TimeUtils.nanoTime());

        Peon malo;
        malos.add(malo = new Peon(plantilla));

        malo.setX(MathUtils.random(0, ancho - malo.getAncho()));
        malo.setY(alto);

        if (malo.getPlantilla().isZigzag()) malo.setV((-1 + MathUtils.random(1) * 2) * 50 , -50);
        else malo.setV(0, -100);

    }

    @Override
    public void resume() {
        super.resume();
        System.out.println("resume!");
    }

    @Override
    public void show() {
        super.show();
        System.out.println("show");
        Gdx.input.setInputProcessor(stage);
    }


    private static Touchpad.TouchpadStyle getTouchPadStyle(){
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
        return touchpadStyle;
    }


    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }
}
