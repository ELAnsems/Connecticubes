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
public class Tile extends Image {

    public Cube cube = null;
    public String hintColor = "";

    public Tile(Texture texture){
        super(texture);
    }


    public void removeCube(){
        this.cube = null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!hintColor.equals("")) {
            Texture t2 = new Texture(Gdx.files.internal(hintColor + "TileGlow.png"));
            batch.draw(t2, this.getX()-(30*(this.getWidth()/180)), this.getY()-(30*(this.getHeight()/180)), 240*(this.getWidth()/180), 240*(this.getHeight()/180));
            super.draw(batch, parentAlpha);
            t2.dispose();
        }
        else{
            super.draw(batch, parentAlpha);
        }
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
