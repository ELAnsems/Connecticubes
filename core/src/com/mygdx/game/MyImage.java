package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by localadmin on 20-6-2017.
 */
public class MyImage extends Image {

    public boolean isDown=false;
    public boolean isDragging=false;
    public Vector2 lastTouch = new Vector2();


    public MyImage(Texture texture){
        super(texture);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public boolean addListener(EventListener listener) {
        return super.addListener(listener);
    }


}
