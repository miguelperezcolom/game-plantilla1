package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;

public class SuperMenu extends ScreenAdapter {

    private final MyGdxGame game;
    private final float ancho;
    private final float alto;
    private final Stage stage;
    private final Skin skin;
    private final MenuSergi sergi;
    private final MenuKTT ktt;
    private OrthographicCamera camara;

    public SuperMenu(final MyGdxGame game) {

        this.game = game;

        skin = new Skin(Gdx.files.internal("skin/freezing-ui.json"));

        ancho = 800;
        alto = 450;
        
        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);

        stage = new Stage();

        Table t;
        stage.addActor(t = new Table(skin));

        t.setFillParent(true);
        TextButton pb1;
        t.add(pb1 = new TextButton("Defend Bellver", skin)).padBottom(10);

        t.row();

        TextButton pb2;
        t.add(pb2 = new TextButton("Kill the tourists", skin)).padBottom(10);

        t.row();


        TextButton eb;
        t.add(eb = new TextButton("Exit", skin));

        sergi = new MenuSergi(game);

        pb1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                /*
                juego.puntos = 0;
                juego.vidas = 3;
                juego.setNivelActual(juego.getNiveles().get(0));
                */
                game.setScreen(sergi);
            }
        });


        ktt = new MenuKTT(game);

        pb2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                /*
                juego.puntos = 0;
                juego.vidas = 3;
                juego.setNivelActual(juego.getNiveles().get(0));
                */
                game.setScreen(ktt);
            }
        });

        eb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
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
