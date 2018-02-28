package io.mateu.gamemaker.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TestScreenAdapter extends ScreenAdapter {


    private final Stage escenario;

    public TestScreenAdapter() {
        escenario = new Stage();

        // para que los actores dentro del escenario reciban los eventos...
        Gdx.input.setInputProcessor(escenario);


        TestActor a;
        escenario.addActor(a = new TestActor());

        a.setX(escenario.getWidth() / 2);
        a.setY(escenario.getHeight() / 2);


        if (false) escenario.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("escenario.down");
                return false; // si devuelve false entonces no recibirá los eventos up ni dragged
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("up");
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                System.out.println("dragged");
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                System.out.println("moved");
                return true;
            }
        });

    }




    @Override
    public void resize(int width, int height) {
        System.out.println("resize(" + width + "," + height + ")");
        // See below for what true means.
        escenario.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {

        //System.out.println("render(" + delta + ")");

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        escenario.act(delta);
        escenario.draw();

    }

    @Override
    public void dispose() {
        escenario.dispose();
    }
}
