package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;

public class Menu extends Pantalla {

    private final MyGdxGame game;
    private final float ancho;
    private final float alto;
    private final Juego juego;
    private final Stage stage;
    private OrthographicCamera camara;

    public Menu(final Juego juego, final MyGdxGame game, XmlReader.Element xml) {

        //super(xml);

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
        TextButton pb;
        t.add(pb = new TextButton("Play", juego.skin)).padBottom(10);

        t.row();

        TextButton eb;
        t.add(eb = new TextButton("Back to Menu", juego.skin));

        pb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
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
        Gdx.input.setInputProcessor(stage);
    }
}
