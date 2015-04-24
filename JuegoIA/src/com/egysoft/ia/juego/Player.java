package com.egysoft.ia.juego;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Alumno
 */
public class Player extends Actor implements Pieza
{
    private Celda celda;
    private Animation up;
    private Animation down;
    private Animation left;
    private Animation right;
    private Animation selected;
    
    private float time;
    
    public Player(String assetName, TextureAtlas atlas)
    {
        Array<TextureRegion> array = new Array<>(4);
        array.add(null); //relleno con nulls
        array.add(null); //para poder hacer set sin problemas
        array.add(null);
        array.add(null);
        
        array.set(0, atlas.findRegion(assetName,0));
        array.set(1, atlas.findRegion(assetName,1));
        array.set(2, atlas.findRegion(assetName,2));
        array.set(3, atlas.findRegion(assetName,1));
        up = new Animation(0.5f, array, Animation.PlayMode.LOOP);
        
        array.set(0, atlas.findRegion(assetName,3));
        array.set(1, atlas.findRegion(assetName,4));
        array.set(2, atlas.findRegion(assetName,5));
        array.set(3, atlas.findRegion(assetName,4));
        right = new Animation(0.5f, array, Animation.PlayMode.LOOP);
        
        array.set(0, atlas.findRegion(assetName,6));
        array.set(1, atlas.findRegion(assetName,7));
        array.set(2, atlas.findRegion(assetName,8));        
        array.set(3, atlas.findRegion(assetName,7));
        down = new Animation(0.5f, array, Animation.PlayMode.LOOP);
        
        array.set(0, atlas.findRegion(assetName,9));
        array.set(1, atlas.findRegion(assetName,10));
        array.set(2, atlas.findRegion(assetName,11));
        array.set(3, atlas.findRegion(assetName,10));
        left = new Animation(0.5f, array, Animation.PlayMode.LOOP);
        
        selected = down;
        
        addListener(new InputListener()
        {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                  if(Keys.LEFT == keycode) 
                  {
                      setX(getX()-10);
                      return true;
                  }
                  if(Keys.RIGHT == keycode) 
                  {
                      setX(getX()+10);
                      return true;
                  }
                  if(Keys.UP == keycode) 
                  {
                      setY(getY()+10);
                      return true;
                  }
                  if(Keys.DOWN == keycode) 
                  {
                      setY(getY()-10);
                      return true;
                  }
                  return false;
            }
        });
    }
    
    @Override
    public void	act(float delta)
    {
        time += delta;        
        super.act(delta);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
       TextureRegion region = selected.getKeyFrame(time);
       batch.draw(region, getX(), getY());       
    }   

    @Override
    public Celda getCeldaActual()
    {
        return celda;
    }

    @Override
    public void setCeldaActual(Celda nueva) 
    {
        celda = nueva;
    }

    
    
    
}
