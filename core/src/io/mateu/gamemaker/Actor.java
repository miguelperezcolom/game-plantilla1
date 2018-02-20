package io.mateu.gamemaker;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Scaling;
import org.jdom2.Element;

public class Actor {

    private Rectangle rect;
    private Texture img;
    private Sprite sprite;

    private float ancho;
    private float alto;

    public Actor(Element xml) {
        ancho = Float.parseFloat(xml.getAttributeValue("ancho"));
        alto = Float.parseFloat(xml.getAttributeValue("alto"));
        rect = new Rectangle();
        rect.width = ancho;
        rect.height = alto;
        img = new Texture(xml.getAttributeValue("img"));
        sprite = new Sprite(img); // Creates a sprite from a Texture
        //sprite.setCenter(img.getWidth() / 2, img.getHeight() / 2);
        //sprite.setSize(rect.width, rect.height);
        //sprite.setOrigin(img.getWidth() / 2, img.getHeight() / 2);
        sprite.setScale(rect.width / img.getWidth(), rect.height / img.getHeight());
        sprite.setOrigin(0, 0);
    }


    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setX(float x) {
        System.out.println("x=" + x);
        rect.x = x;
        sprite.setX(x);
    }

    public void setY(float y) {
        rect.y = y;
        sprite.setY(y);
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void moveX(float d) {
        setX(sprite.getX() + d);
    }



}
