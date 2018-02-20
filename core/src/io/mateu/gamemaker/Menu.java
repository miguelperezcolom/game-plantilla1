package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.jdom2.Element;

public class Menu extends ScreenAdapter {

    private final MyGdxGame game;
    private final float ancho;
    private final float alto;
    private final Juego juego;
    private OrthographicCamera camara;

    public Menu(Juego juego, MyGdxGame game, Element xml) {
        this.juego = juego;
        this.game = game;

        ancho = Float.parseFloat(xml.getAttributeValue("ancho"));
        alto = Float.parseFloat(xml.getAttributeValue("alto"));
        
        camara = new OrthographicCamera();
        camara.setToOrtho(false, ancho, alto);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        game.batch.setProjectionMatrix(camara.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Hola", 100, 150);
        game.font.draw(game.batch, "Pulsa donde quieras para empezar", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            juego.setNivelActual(juego.getNiveles().get(0));
            dispose();
        }

    }
}
