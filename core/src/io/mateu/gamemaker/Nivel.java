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
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

public class Nivel extends Pantalla {

    private final Stage stage;
    //private final Touchpad tp;
    private final Stage stageFondo;
    private final Label lscore;
    private final Label lvidas;
    private final XmlReader.Element xmlNivel;
    private Image torre;
    Map<String, Plantilla> plantillas;
    private Music musica;
    private final float ancho;
    private final float alto;
    private final OrthographicCamera camara;
    private final MyGdxGame game;
    private final Juego juego;
    private Peon jugador;
    List<Plantilla> tiposMalos = new ArrayList<>();
    long lastShot = 0;
    long inicioNivel = 0;
    float dificultad = 0;
    Vector2 punteria = new Vector2();

    float oldX = 0;
    float oldY = 0;

    private boolean sergi;

    private List<Peon> jugadores = new ArrayList<>();
    protected List<Peon> malos = new ArrayList<>();
    private List<Peon> balasAmigas = new ArrayList<>();
    protected List<Peon> balasEnemigas = new ArrayList<>();
    private Map<Plantilla, Long> ultimoDrop;

    private boolean parallax;


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

    public boolean disparando;
    private boolean jugadorFijo;


    public Nivel(MyGdxGame game, final Juego juego, XmlReader.Element xml, XmlReader.Element xmlNivel) {

        super();

        this.juego = juego;

        setXml(xml);

        this.xmlNivel = xmlNivel;

        this.game = game;

        ancho = Float.parseFloat(xmlNivel.getAttribute("ancho"));
        alto = Float.parseFloat(xmlNivel.getAttribute("alto"));

        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);


        if (xmlNivel.hasAttribute("fondo")) {
            imgsFondo = new String[] { xmlNivel.getAttribute("fondo") };
        }



        ultimoDrop = new HashMap<>();
        for (Plantilla p : tiposMalos) ultimoDrop.put(p, TimeUtils.nanoTime());


        stage = new Stage() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 v = camara.unproject(new Vector3(screenX, screenY, 0));
                oldX = v.x;
                oldY = v.y;
                disparando = true;
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                Vector3 v = camara.unproject(new Vector3(screenX, screenY, 0));
                if (jugador.isActivo()) {
                    if (!jugadorFijo) {
                        jugador.setX(jugador.getX() + v.x - oldX);
                        jugador.setY(jugador.getY() + v.y - oldY);
                    } else {
                        Vector2 z = new Vector2(v.x - jugador.getX(), v.y - jugador.getY());
                        z = z.nor();
                        punteria.set(z.x, z.y);
                    }
                }
                oldX = v.x;
                oldY = v.y;
                return super.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                disparando = false;
                return super.touchUp(screenX, screenY, pointer, button);
            }
        };

        TextButton b;
        stage.addActor(b = new TextButton("||", Juego.get().skin));
        b.setWidth(100f);
        b.setHeight(30f);
        b.setPosition(20, Gdx.graphics.getHeight() - 70f);
        b.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("pausa");


                juego.setNivelActual(null);

            }
        });

        stage.addActor(lscore = new Label("Score: " + juego.puntos, juego.skin));
        lscore.setWidth(100f);
        lscore.setHeight(20f);
        lscore.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 30f);

        stage.addActor(lvidas = new Label("Lifes: " + juego.vidas, juego.skin));
        lvidas.setWidth(100f);
        lvidas.setHeight(20f);
        lvidas.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 60f);



        //background
        stageFondo = new Stage();

        sergi = xmlNivel.hasAttribute("sergi");

        if (isParallax()) {
            Array<Texture> textures = new Array<Texture>();
            for(int i = 0; i < imgsFondo.length; i++){
                textures.add(new Texture(Gdx.files.internal(imgsFondo[i])));
                textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
            }

            ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
            parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            parallaxBackground.setSpeed(1f);

            stageFondo.addActor(parallaxBackground);
        } else {
            Image i;
            stageFondo.addActor(i = new Image(new Texture(Gdx.files.internal(imgsFondo[0]))));
            i.setBounds(0, 0, ancho, alto);
            i.setFillParent(true);
            if (sergi) {
                stageFondo.addActor(torre = new Image(new Texture(Gdx.files.internal("sergi/torre.png"))));
            }
        }



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


        if (inicioNivel == 0) inicioNivel = TimeUtils.millis();

        dificultad = TimeUtils.millis() - inicioNivel;

        long segundos = new Float(dificultad / 1000).intValue();

        camara.update();
        game.batch.setProjectionMatrix(camara.combined);


        if (disparando && jugador.isActivo()) {
            long t = System.currentTimeMillis();
            if (lastShot == 0 || t - lastShot > 200) {
                lastShot = t;
                disparar();
            }
        }

        stageFondo.act(delta);

        stageFondo.draw();

        game.batch.begin();

        for (Peon malo : malos) malo.draw(game.batch);

        for (Peon bala : balasAmigas) {
            bala.act(delta);
            bala.draw(game.batch);
        }

        for (Peon bala : balasEnemigas) bala.draw(game.batch);

        jugador.draw(game.batch);

        game.batch.end();


        lscore.setText("Score: " + juego.puntos);
        lvidas.setText("Lifes: " + juego.vidas);
        //stage.act();
        stage.draw();



        jugador.act(delta);
        for (Peon malo : malos) malo.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) jugador.acelerarX(-10);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) jugador.acelerarX(10);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) jugador.acelerarY(-10);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) jugador.acelerarY(10);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) disparar();



        /*
        jugador.acelerarX(30 * tp.getKnobPercentX());
        jugador.acelerarY(30 * tp.getKnobPercentY());
        */



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
        if (segundos % 30 < 20) {
            for (XmlReader.Element e : xmlNivel.getChildrenByName("malo")) {
                Plantilla p = plantillas.get(e.getAttribute("id"));
                long f = p.getFrecuencia();
                f -= 500000 * dificultad / 1000;
                if (ahora - ultimoDrop.get(p) > f) crearMalo(p);
            }
        }

        for (Peon m : malos) if (m.isActivo() && m.getPlantilla().isDispara() && MathUtils.random(0, 50) == 0) disparar(m, plantillas.get("balaenemiga"));

        List<Peon> borrar = new ArrayList<>();

        for (Peon bala : balasAmigas) {
            if (bala.getY() > alto) borrar.add(bala);
        }

        for (Peon bala : balasEnemigas) {
            bala.act(delta);
            if (bala.getY() < 0) borrar.add(bala);
        }

        for (Peon malo : malos) {
            malo.act(delta);

            if (sergi && malo.getX() < 200 && malo.getY() <= torre.getY() + torre.getHeight()) {
                malo.setX(200);
                if (malo.isActivo()) {
                    torre.setY(torre.getY() - 1);
                    jugador.setY(jugador.getY() - 1);
                }
            }

            if (malo.getX() < 0) borrar.add(malo);

            if (malo.isActivo()) for (Peon bala : balasAmigas) if (!borrar.contains(bala) && bala.getRect().overlaps(malo.getRect())) {

                malo.setVida(malo.getVida() - 1);

                if (malo.getVida() >= 0) {
                    malo.destruido();
                }

                this.juego.puntos += 10;

                bala.destruido();
                borrar.add(bala);

                break;
            }


            if (jugador.isActivo() && malo.isActivo() && !borrar.contains(malo) && malo.getRect().overlaps(jugador.getRect())) {
                malo.destruido();
                System.out.println("hit");

                jugador.hitten(malo);

                juego.vidas --;

                Gdx.input.vibrate(500);

            }


            if (juego.vidas <= 0 && !game.getScreen().equals(Juego.get().finPartida) && jugador.isActivo()) {

                jugador.destruido();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        juego.finPartida.setJuego(juego);
                        juego.finPartida.setXml(xml);
                        game.setScreen(juego.finPartida);
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

                juego.vidas --;

                Gdx.input.vibrate(500);
            }
        }

        if (sergi && jugador.getY() <= 0) juego.vidas = 0;

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
        balasAmigas.add(bala = new Peon(plantillas.get("balaamiga")));
        bala.setX(jugador.getX());
        bala.setY(jugador.getY());
        bala.setRotacion(punteria.angle() + bala.getPlantilla().getRotacion());
        float v = 400;
        bala.setV(punteria.x * v, punteria.y * v);
    }

    public void crearMalo(Plantilla plantilla) {

        ultimoDrop.put(plantilla, TimeUtils.nanoTime());

        Peon malo;
        malos.add(malo = new Peon(plantilla));


        if (alto > ancho) {

            malo.setX(MathUtils.random(0, ancho - malo.getAncho()));
            malo.setY(alto);

            if (malo.getPlantilla().isZigzag()) malo.setV((-1 + MathUtils.random(1) * 2) * 50 , -50);
            else malo.setV(0, -100);

        } else {

            malo.setX(ancho);
            malo.setY(MathUtils.random(alto - malo.getAlto(), 0));

            if (malo.getPlantilla().isZigzag()) malo.setV(-50, (-1 + MathUtils.random(1) * 2) * 50);
            else malo.setV(-100, 0);

        }

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
        if (xmlNivel.hasChild("musica")) {
            musica = getAssetManager().get(xmlNivel.getChildByName("musica").getAttribute("src"));
            musica.setLooping(true);
        }

        Gdx.input.setInputProcessor(stage);
        disparando = Gdx.input.isTouched();
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

    @Override
    public void cargarAssets() {
        super.cargarAssets();

        if (xmlNivel.hasChild("musica")) {
            getAssetManager().load(xmlNivel.getChildByName("musica").getAttribute("src"), Music.class);
        }


        plantillas = new HashMap<>();

        plantillas.put("jugador", new Plantilla(xmlNivel.getChildByName("jugador"), getAssetManager()));
        plantillas.put("balaamiga", new Plantilla(xmlNivel.getChildByName("balaamiga"), getAssetManager()));
        plantillas.put("balaenemiga", new Plantilla(xmlNivel.getChildByName("balaenemiga"), getAssetManager()));

        if (alto < ancho) {
            plantillas.get("balaamiga").setRotacion(plantillas.get("balaamiga").getRotacion() + 90);
        }

        for (XmlReader.Element e : xmlNivel.getChildrenByName("malo")) plantillas.put(e.getAttribute("id"), new Plantilla(e, getAssetManager()));

        parallax = xmlNivel.hasAttribute("parallax") && "si".equalsIgnoreCase(xmlNivel.getAttribute("parallax"));

        jugador = new Peon(plantillas.get("jugador"));
        jugadorFijo = xmlNivel.getChildByName("jugador").hasAttribute("fijo") && "si".equalsIgnoreCase(xmlNivel.getChildByName("jugador").getAttribute("fijo"));

        if (alto > ancho) {
            jugador.setX(ancho / 2);
            jugador.setY(alto / 4);

            punteria.set(0, 1);

        } else {
            jugador.setX(jugador.getAncho() + 80);
            jugador.setY(alto * 3 / 4);

            punteria.set(1, 0);

        }

        if (sergi) {
            torre.setY(0);
        }


        for (XmlReader.Element e : xmlNivel.getChildrenByName("malo")) {
            plantillas.put(e.getAttribute("id"), new Plantilla(e, getAssetManager()));
            ultimoDrop.put(plantillas.get(e.getAttribute("id")), TimeUtils.millis());
            //malos.add(new Peon(plantillas.get(e.getAttribute("id"))));
        }

        getAssetManager().finishLoading();

    }


    public boolean isJugadorFijo() {
        return jugadorFijo;
    }

    public void setJugadorFijo(boolean jugadorFijo) {
        this.jugadorFijo = jugadorFijo;
    }

    public Vector2 getPunteria() {
        return punteria;
    }

    public void setPunteria(Vector2 punteria) {
        this.punteria = punteria;
    }

    public boolean isParallax() {
        return parallax;
    }

    public void setParallax(boolean parallax) {
        this.parallax = parallax;
    }

    public Image getTorre() {
        return torre;
    }

    public void setTorre(Image torre) {
        this.torre = torre;
    }
}
