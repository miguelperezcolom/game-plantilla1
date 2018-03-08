package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;

public class TestScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final float ancho;
    private final float alto;
    private final Juego juego;
    private final Stage stage;
    private OrthographicCamera camara;

    public TestScreen(final Juego juego, MyGdxGame game, XmlReader.Element xml) {
        this.juego = juego;
        this.game = game;

        ancho = 450;
        alto = 800;
        
        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);

        stage = new Stage();



        TextButton b;
        stage.addActor(b = new TextButton("||", Juego.get().skin));
        b.setWidth(30f);
        b.setHeight(20f);
        b.setPosition(10, Gdx.graphics.getHeight() - 40f);
        b.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("pausa");


                Juego.get().setNivelActual(null);

            }
        });

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                super.clicked(event, x, y);
            }
        });

        //stage.addActor(button);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();


    }

    @Override
    public void dispose() {
        System.out.println("dispose de menu");
        super.dispose();
        stage.dispose();

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
}
