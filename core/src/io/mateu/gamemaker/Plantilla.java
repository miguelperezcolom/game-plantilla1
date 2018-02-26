package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader;

public class Plantilla {

    private Sound sonidoDestruido;
    private Texture img;
    private Sprite sprite;

    private float ancho;
    private float alto;

    private long frecuencia;
    

    public Plantilla(XmlReader.Element xml) {
        ancho = Float.parseFloat(xml.getAttribute("ancho"));
        alto = Float.parseFloat(xml.getAttribute("alto"));
        img = new Texture(xml.getAttribute("img"));
        sprite = new Sprite(img); // Creates a sprite from a Texture
        sprite.setScale(ancho / img.getWidth(), alto / img.getHeight());
        sprite.setOrigin(0, 0);
        if (xml.hasAttribute("frecuencia")) frecuencia = 1000l * Long.parseLong(xml.getAttribute("frecuencia"));
        if (xml.hasAttribute("sonidoDestruido")) sonidoDestruido = Gdx.audio.newSound(Gdx.files.internal(xml.getAttribute("sonidoDestruido")));
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
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

    public Sound getSonidoDestruido() {
        return sonidoDestruido;
    }

    public long getFrecuencia() {
        return frecuencia;
    }
}
