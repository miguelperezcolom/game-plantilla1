package io.mateu.gamemaker;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;

public class Actor {

    private boolean activo = true;
    private boolean borrar = false;
    private final Plantilla plantilla;
    private Circle rect;
    private Vector2 v = new Vector2();
    public float stateTime = 0f;
    public float stateTimeExplosion = 0f;
    public float stateTimeExplosionHit = 0f;
    float rotacion;
    private Vector2 posHit = null;
    private float deltaAgulo = 0;
    private int vida = 1;


    public Actor(Plantilla plantilla) {
        this.plantilla = plantilla;
        rotacion = plantilla.getRotacion();
        float r = plantilla.getRadio();
        rect = new Circle(0, 0, r);
        if (plantilla.getSonidoCreado() != null) plantilla.getSonidoCreado().play();
        vida = plantilla.getVida();
    }


    public void setX(float x) {
        rect.x = x;
    }

    public void setY(float y) {
        rect.y = y;
    }

    public Circle getRect() {
        return rect;
    }

    public void moveX(float d) {
        rect.x += d;
    }


    public void draw(SpriteBatch batch) {
        //System.out.println("x=" + rect.x + ", y=" + rect.y + ", r=" + rect.radius );
        if (activo) {

            if (Juego.get().isDebug()) {
                batch.end();
                ShapeRenderer shapeRenderer = new ShapeRenderer();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(1, 1, 0, 1);
                //shapeRenderer.line(x, y, x2, y2);
                //shapeRenderer.rect(x, y, width, height);
                shapeRenderer.circle(rect.x, rect.y, rect.radius);
                shapeRenderer.end();
                batch.begin();
            }


            if (plantilla.getSprite() != null) {

                batch.draw(
                        plantilla.getSprite(),
                        rect.x - plantilla.getAncho() / 2, rect.y - plantilla.getAlto() / 2,
                        plantilla.getAncho() / 2, plantilla.getAlto() / 2,

                        plantilla.getAncho(), plantilla.getAlto()
                        , 1f, 1f, rotacion + deltaAgulo);
            } else if (plantilla.getAnimacion() != null) {
                TextureRegion currentFrame = plantilla.getAnimacion().getKeyFrame(stateTime);
                //System.out.println("w=" + currentFrame.getRegionWidth() + ", h=" + currentFrame.getRegionHeight());
                batch.draw(
                        currentFrame,
                        rect.x - rect.radius, rect.y - rect.radius,
                        rect.radius, rect.radius,

                        getAncho() * 2, getAlto() * 2
                        , 1f, 1f, rotacion);
            }
        } else {
            if (!borrar && plantilla.getExplosion() != null) {
                TextureRegion currentFrame = plantilla.getExplosion().getKeyFrame(stateTimeExplosion);
                batch.draw(
                        currentFrame,
                        rect.x - rect.radius, rect.y - rect.radius,
                        0, -0,

                        getAncho() * 2, getAlto() * 2
                        , 1f, 1f, 0f);
            }
        }
        if (posHit != null && plantilla.getExplosion() != null) {
            System.out.println("stateTimeExplosionHit=" + stateTimeExplosionHit);
            TextureRegion currentFrame = plantilla.getExplosion().getKeyFrame(stateTimeExplosionHit);
            batch.draw(
                    currentFrame,
                    rect.x + posHit.x - rect.radius, rect.y + posHit.y - rect.radius,
                    0, -0,

                    getAncho() * 2, getAlto() * 2
                    , 1f, 1f, 0f);
            if (stateTimeExplosionHit >= plantilla.getExplosion().getAnimationDuration()) posHit = null;
        }
    }

    public float getX() {
        return rect.x;
    }

    public float getAncho() {
        return rect.radius;
    }

    public float getY() {
        return rect.y;
    }

    public void destruido() {
        System.out.println("destruido");
        activo = false;
        if (plantilla.getSonidoDestruido() != null) plantilla.getSonidoDestruido().play();
        if (plantilla.getExplosion() ==null) {
            borrar = true;
        }
    }

    public void moveY(float d) {
        rect.y += d;
    }

    public float getAlto() {
        return rect.radius;
    }

    public void setV(float vx, float vy) {
        v.x = vx;
        v.y = vy;
    }

    public void act(float delta) {
        rect.x = rect.x + delta * v.x;
        rect.y = rect.y + delta * v.y;

        if (plantilla.isZigzag()) {
            if (Juego.get().getAlto() > Juego.get().getAncho()) {
                if (getX() < getAncho() || getX() > Juego.get().getNiveles().get(0).getAncho() - getAncho()) v.x *= -1;
            } else {
                if (getY() < getAlto() || getY() > Juego.get().getNiveles().get(0).getAlto() - getAlto()) v.y *= -1;
            }
        }

        stateTime += delta;
        if (!activo && plantilla.getExplosion() != null) {
            stateTimeExplosion += delta;
            if (plantilla.getExplosion().getAnimationDuration() < stateTimeExplosion) borrar = true;
        }
        if (posHit != null  && plantilla.getExplosion() != null) {
            stateTimeExplosionHit += delta;
        }

        if (plantilla.isGira()) {
            deltaAgulo += 400 * delta;
        }
    }

    float VMAX = 200;

    public void acelerarX(float a) {
        v.x += a;
        if (Math.abs(v.x) > VMAX) v.x = VMAX * ((v.x < 0)?-1:1);
    }

    public void acelerarY(float a) {
        v.y += a;
        if (Math.abs(v.y) > VMAX) v.y = VMAX * ((v.y < 0)?-1:1);
    }

    public void pararX() {
        v.x = 0;
    }

    public void pararY() {
        v.y = 0;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isBorrar() {
        return borrar;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public float getRotacion() {
        return rotacion;
    }

    public void setRotacion(float rotacion) {
        this.rotacion = rotacion;
    }

    public void hitten(Peon elOtro) {
        if (plantilla.getSonidoDestruido() != null) plantilla.getSonidoDestruido().play();
        if (plantilla.getExplosion() != null) {
            stateTimeExplosionHit = 0;
            float x = (getX() + elOtro.getX()) / 2 - getX();
            float y = (getY() + elOtro.getY()) / 2 - getY();
            posHit = new Vector2(x, y);
        }
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setStateTimeExplosion(int stateTimeExplosion) {
        this.stateTimeExplosion = stateTimeExplosion;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
}
