package io.mateu.gamemaker;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Scaling;
import org.jdom2.Element;

public class Actor {

    private final Plantilla plantilla;
    private Rectangle rect;

    public Actor(Element xml) {
        plantilla = new Plantilla(xml);
        rect = new Rectangle();
        rect.width = plantilla.getAncho();
        rect.height = plantilla.getAlto();
    }

    public Actor(Plantilla plantilla) {
        this.plantilla = plantilla;
        rect = new Rectangle();
        rect.width = plantilla.getAncho();
        rect.height = plantilla.getAlto();
    }


    public void setX(float x) {
        rect.x = x;
    }

    public void setY(float y) {
        rect.y = y;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void moveX(float d) {
        rect.x += d;
    }


    public void draw(SpriteBatch batch) {
        plantilla.getSprite().setX(rect.x);
        plantilla.getSprite().setY(rect.y);
        plantilla.getSprite().draw(batch);
    }

    public float getX() {
        return rect.x;
    }

    public float getAncho() {
        return rect.width;
    }

    public float getY() {
        return rect.y;
    }

    public void destruido() {
        if (plantilla.getSonidoDestruido() != null) plantilla.getSonidoDestruido().play();
    }
}
