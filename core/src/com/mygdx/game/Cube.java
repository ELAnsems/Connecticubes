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
public class Cube extends Image {

    public boolean isDown=false;
    public boolean isDragging=false;
    public Vector2 lastTouch = new Vector2();
    public Tile connectedTile = null;
    public Tile lastConnectedTile = null;
    public Tile correctTile = null;
    public String color = "";
    public int originalZIndex =0;
    public String correctChar = "";
    public int playerPickedUp=0;


    public Cube(Texture texture, String color){
        super(texture);
        this.color = color;
    }
    public Cube(Texture texture){
        super(texture);
    }

    public void setOriginalZIndex(int originalZIndex){

        this.originalZIndex =originalZIndex;
    }

    public void update(){

    }

    public void setCorrectTile(Tile correctTile){
        this.correctTile = correctTile;
    }

    public void setCorrectChar(String correctChar){
        this.correctChar = correctChar;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Texture t2 = new Texture(Gdx.files.internal("cube"+color+".png"));//new Texture("cube"+colors[i]+".png")
        batch.draw(t2, this.getX(),this.getY(),this.getWidth(),this.getHeight());
        super.draw(batch, parentAlpha);
        t2.dispose();
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
