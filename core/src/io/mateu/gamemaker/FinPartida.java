package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;

public class FinPartida extends Pantalla {

    private final MyGdxGame game;
    private final float ancho;
    private final float alto;
    private final Stage stage;
    private final Label lscore;
    private final Skin skin;
    private OrthographicCamera camara;
    private boolean play;
    private boolean record;
    private Juego juego;

    public FinPartida(final MyGdxGame game) {

        super();


        skin = new Skin(Gdx.files.internal("skin/freezing-ui.json"));
        this.game = game;

        ancho = 800;
        alto = 450;
        
        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);

        stage = new Stage();

        Table t;
        stage.addActor(t = new Table(skin));

        t.setFillParent(true);

        t.add(new Label("You lost", skin)).padBottom(10);
        t.row();
        t.add(lscore = new Label("Score: ", skin)).padBottom(10);
        t.row();

        TextButton pb;
        t.add(pb = new TextButton("Play again", skin)).padBottom(10);

        t.row();

        TextButton eb;
        t.add(eb = new TextButton("Back to Menu", skin));

        pb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //button.setText("You clicked the button");
                juego.puntos = 0;
                juego.vidas = 3;
                Nivel n = juego.getNiveles().get(0);
                n.malos.clear();
                n.balasEnemigas.clear();
                n.getJugador().setX(ancho / 2);
                n.getJugador().setY(alto / 4);
                n.getJugador().setV(0, 0);
                n.getJugador().setActivo(true);
                n.getJugador().setStateTimeExplosion(0);
                n.disparando = false;
                juego.setNivelActual(n);
            }
        });


        eb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.supermenu);
            }
        });

        //stage.addActor(button);


    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        game.batch.setProjectionMatrix(camara.combined);

        game.batch.begin();
        stage.draw();
        game.batch.end();

        if (play) {
            System.out.println("play levelup");
            if (getXml().hasAttribute("sonidoRecord")) {
                Sound lus = getAssetManager().get(getXml().getAttribute("sonidoRecord"));
                lus.play();

                if (record) {
                    lus.play();
                    lus.play();
                }
            }

            play = false;
        }

    }

    @Override
    public void dispose() {
        System.out.println("dispose de menu");
        super.dispose();
        stage.dispose();
    }

    @Override
    public void show() {
        super.show();


        record = false;
        int highScore = (juego.getPrefs().contains("highscore"))?juego.getPrefs().getInteger("highscore"):0;
        if (highScore < juego.puntos) {
            record = true;
            Juego.get().getPrefs().putInteger("highscore", juego.puntos);
            Juego.get().getPrefs().flush();
        }

        play = true;

        lscore.setText("Score: " + juego.puntos + "" + ((record)?"(New record!!!!)":"(Record: " + highScore + ")"));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void cargarAssets() {
        super.cargarAssets();
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }
}
