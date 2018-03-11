package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class Plantilla {

    private boolean zigzag = false;
    private Sound sonidoDestruido;
    private Sound sonidoCreado;
    private Texture img;
    private Texture pack;
    private Animation<TextureRegion> animacion;
    private Animation<TextureRegion> explosion;
    private Sprite sprite;
    private float escala = 1f;
    private float rotacion = 0f;
    private float escalaExplosion = 1f;
    private boolean disparoDirigido = false;
    private boolean dispara = false;
    private boolean gira = false;
    private int vida = 1;

    private float ancho;
    private float alto;
    private float radio;

    private long frecuencia;
    

    public Plantilla(XmlReader.Element xml, AssetManager assetManager) {
        if (xml.hasAttribute("gira") && "si".equalsIgnoreCase(xml.getAttribute("gira"))) gira = true;
        if (xml.hasAttribute("escala")) escala = Float.parseFloat(xml.getAttribute("escala"));
        if (xml.hasAttribute("escalaExplosion")) escalaExplosion = Float.parseFloat(xml.getAttribute("escalaExplosion"));
        if (xml.hasAttribute("rotacion")) rotacion = Float.parseFloat(xml.getAttribute("rotacion"));
        if (xml.hasAttribute("img")) {
            String urlimg = xml.getAttribute("img");
            if (urlimg.startsWith("/")) urlimg = urlimg.substring(1);
            img = new Texture(urlimg);
            sprite = new Sprite(img); // Creates a sprite from a Texture
            sprite.setOrigin(0, 0);
            ancho = img.getWidth() * escala;
            alto = img.getHeight() * escala;

            sprite.setScale(escala);
            sprite.setRotation(rotacion);
        } else if (xml.hasAttribute("pack")) {
            String urlimg = xml.getAttribute("pack");
            if (urlimg.startsWith("/")) urlimg = urlimg.substring(1);
            pack = new Texture(urlimg);
            TextureRegion[][] tmp = TextureRegion.split(pack, pack.getWidth() / Integer.parseInt(xml.getAttribute("imgs")), pack.getHeight());
            Array<TextureRegion> a = new Array<>(tmp[0]);
            animacion = new Animation<TextureRegion>(0.025f, a, Animation.PlayMode.LOOP);
            ancho = a.get(0).getRegionWidth() * escala;
            alto = a.get(0).getRegionHeight() * escala;
        }
        radio = Math.max(ancho, alto) / 2;
        if (xml.hasAttribute("radio")) radio = Long.parseLong(xml.getAttribute("radio"));

        if (xml.hasAttribute("frecuencia")) frecuencia = 1000l * Long.parseLong(xml.getAttribute("frecuencia"));
        if (xml.hasAttribute("sonidoDestruido")) sonidoDestruido = assetManager.get(xml.getAttribute("sonidoDestruido"));
        if (xml.hasAttribute("sonidoCreado")) sonidoCreado = assetManager.get(xml.getAttribute("sonidoCreado"));
        if (xml.hasAttribute("explosion")) {
            String urlimg = xml.getAttribute("explosion");
            if (urlimg.startsWith("/")) urlimg = urlimg.substring(1);
            explosion = new Animation(0.25f, new TextureAtlas(Gdx.files.internal(urlimg)).getRegions());
        }
        if (xml.hasAttribute("apunta") && "si".equalsIgnoreCase(xml.getAttribute("apunta"))) disparoDirigido = true;
        if (xml.hasAttribute("zigzag") && "si".equalsIgnoreCase(xml.getAttribute("zigzag"))) zigzag = true;
        if (xml.hasAttribute("dispara") && "si".equalsIgnoreCase(xml.getAttribute("dispara"))) dispara = true;
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

    public Sound getSonidoCreado() {
        return sonidoCreado;
    }

    public Texture getPack() {
        return pack;
    }

    public Animation<TextureRegion> getAnimacion() {
        return animacion;
    }

    public float getEscala() {
        return escala;
    }

    public float getRotacion() {
        return rotacion;
    }

    public Animation<TextureRegion> getExplosion() {
        return explosion;
    }

    public float getEscalaExplosion() {
        return escalaExplosion;
    }

    public boolean isDisparoDirigido() {
        return disparoDirigido;
    }

    public boolean isZigzag() {
        return zigzag;
    }

    public void setRotacion(float rotacion) {
        this.rotacion = rotacion;
    }

    public boolean isDispara() {
        return dispara;
    }

    public void setDispara(boolean dispara) {
        this.dispara = dispara;
    }

    public float getRadio() {
        return radio;
    }

    public boolean isGira() {
        return gira;
    }

    public void setGira(boolean gira) {
        this.gira = gira;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
}
