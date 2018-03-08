package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;

public class FinPartida extends ScreenAdapter {

    private final MyGdxGame game;
    private final float ancho;
    private final float alto;
    private final Juego juego;
    private final Stage stage;
    private final Label lscore;
    private OrthographicCamera camara;

    public FinPartida(final Juego juego, MyGdxGame game, XmlReader.Element xml) {
        this.juego = juego;
        this.game = game;

        ancho = Float.parseFloat(xml.getAttribute("ancho"));
        alto = Float.parseFloat(xml.getAttribute("alto"));
        
        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);

        stage = new Stage();

        Table t;
        stage.addActor(t = new Table(juego.skin));

        t.setFillParent(true);

        t.add(new Label("You lost", juego.skin)).padBottom(10);
        t.row();
        t.add(lscore = new Label("Score: " + juego.puntos, juego.skin)).padBottom(10);
        t.row();

        TextButton pb;
        t.add(pb = new TextButton("Play again", juego.skin)).padBottom(10);

        t.row();

        TextButton eb;
        t.add(eb = new TextButton("Exit", juego.skin));
        //button.setFillParent(true);
        //t.setDebug(true);

        /*
        button.setWidth(200f);
        button.setHeight(20f);
        button.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);
        */

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
                Gdx.app.exit();
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
        //game.font.draw(game.batch, "Hola", 100, 150);
        //game.font.draw(game.batch, "Pulsa donde quieras para empezar", 100, 100);
        game.batch.end();

        /*
        if (Gdx.input.isTouched()) {
            juego.setNivelActual(juego.getNiveles().get(0));
            dispose();
        }
        */

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


        boolean record = false;
        int highScore = (juego.getPrefs().contains("highscore"))?juego.getPrefs().getInteger("highscore"):0;
        if (highScore < juego.puntos) {
            record = true;
            Juego.get().getPrefs().putInteger("highscore", juego.puntos);
            Juego.get().getPrefs().flush();
        }

        //Juego.get().getPrefs().putString("name", "Donald Duck");
        //String name = prefs.getString("name", "No name stored");

        //prefs.putBoolean("soundOn", true);


        if (record && juego.getLevelUpSound() != null) {
            juego.getLevelUpSound().play();
        }


        lscore.setText("Score: " + juego.puntos + "" + ((record)?"(New record!!!!)":"(Record: " + highScore + ")"));
        Gdx.input.setInputProcessor(stage);
    }
}
