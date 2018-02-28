package io.mateu.gamemaker.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class TestActor extends Actor {


    Texture texture = new Texture(Gdx.files.internal("q/q.png"));


    public TestActor() {

        setBounds(-1 * texture.getWidth() / 2, -1 * texture.getHeight() / 2, texture.getWidth(), texture.getHeight());

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("testactor.down");
                event.cancel();


                MoveByAction a = new MoveByAction();
                a.setAmount(20, 20);
                a.setDuration(2);
                addAction(a);

                return false; // no nos interesan los eventos touchup ni dragged
            }
        });
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX() -1 * texture.getWidth() / 2, getY() -1 * texture.getHeight() / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // esto pasa continuamente
        //System.out.println("hit");
        return super.hit(x, y, touchable);
    }

}
