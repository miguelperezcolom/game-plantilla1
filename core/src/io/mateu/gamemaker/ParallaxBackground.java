package io.mateu.gamemaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private float scroll;
    private Array<Texture> layers;
    private final float LAYER_SPEED_DIFFERENCE = 2;

    float x,y,width,heigth,scaleX,scaleY;
    float originX, originY,rotation;
    int srcX,srcY;
    boolean flipX,flipY;

    private float speed;

    public ParallaxBackground(Array<Texture> textures){
        layers = textures;
        for(int i = 0; i <textures.size;i++){
            layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        scroll = 0;
        speed = 0;

        x = y = originX = originY = srcY = 0;
        rotation = 0;
        width =  Gdx.graphics.getWidth();
        heigth = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;
    }

    public void setSpeed(float newSpeed){
        this.speed = newSpeed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        scroll+=speed * delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        for(int i = 0;i<layers.size;i++) {
            int aux = new Float(scroll + i * this.LAYER_SPEED_DIFFERENCE).intValue();
            //if (aux - srcX > 1) System.out.println("dif=" + (aux - srcX));
            srcX = aux;
            batch.draw(layers.get(i), x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,srcX,srcY,layers.get(i).getWidth(),layers.get(i).getHeight(),flipX,flipY);
        }
    }
}