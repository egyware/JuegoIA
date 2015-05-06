package com.egysoft.ia.juego.actores;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.Config;
import com.egysoft.ia.juego.State;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.ITablero;
import com.egysoft.ia.juego.tablero.Pieza;

/**
 *
 * @author Edgardo
 */
public class Player extends Pieza
{
	private static final float velocity = 50.0f;
	
	private final Sound sound;
	private final Animation upAnimation;
	private final Animation downAnimation;
	private final Animation leftAnimation;
	private final Animation rightAnimation;
	private Animation selected;
	
    private final IdleState idleState;
    private final LeftState leftState;
    private final RightState rightState;
    private final DownState downState;
    private final UpState upState;
    private final NullState nullState;
    private State currentState;
    
	private float time;
    public boolean left, right,up, down;
    
    public Player(String assetName, TextureAtlas atlas, Sound sound)
    {
    	Array<AtlasRegion> regions;
        Array<TextureRegion> array = new Array<>(4);
        array.add(null); //relleno con nulls
        array.add(null); //para poder hacer set sin problemas
        array.add(null);
        array.add(null);
        
        regions = atlas.findRegions(String.format("%s_derecha", assetName));
        array.set(0, regions.get(0));
        array.set(1, regions.get(1));
        array.set(2, regions.get(0));
        array.set(3, regions.get(2));
        rightAnimation = new Animation(0.25f, array, Animation.PlayMode.LOOP);
        
        regions = atlas.findRegions(String.format("%s_izquierda", assetName));
        array.set(0, regions.get(0));
        array.set(1, regions.get(1));
        array.set(2, regions.get(0));
        array.set(3, regions.get(2));
        leftAnimation = new Animation(0.25f, array, Animation.PlayMode.LOOP);
        
        regions = atlas.findRegions(String.format("%s_espalda", assetName));
        array.set(0, regions.get(0));
        array.set(1, regions.get(1));
        array.set(2, regions.get(0));
        array.set(3, regions.get(2));
        upAnimation = new Animation(0.25f, array, Animation.PlayMode.LOOP);
        
        regions = atlas.findRegions(String.format("%s_frente", assetName));
        array.set(0, regions.get(0));
        array.set(1, regions.get(1));
        array.set(2, regions.get(0));
        array.set(3, regions.get(2));
        downAnimation = new Animation(0.25f, array, Animation.PlayMode.LOOP);
        
        downState = new DownState();
        upState = new UpState();
        leftState = new LeftState();
        rightState = new RightState();
        idleState = new IdleState();
        nullState = new NullState();
        selected = downAnimation; //deje este para el ultimo
        
        this.sound = sound;
        
        setState(idleState);
        
        addListener(new InputListener()
        {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                  if(Keys.LEFT == keycode) 
                  {
                	  left = true;
                      return true;
                  }
                  if(Keys.RIGHT == keycode) 
                  {
                	  right = true;
                      return true;
                  }
                  if(Keys.UP == keycode) 
                  {
                      up = true;
                      return true;
                  }
                  if(Keys.DOWN == keycode) 
                  {
                      down = true;
                      return true;
                  }
                  return false;
            }
            @Override
            public boolean keyUp(InputEvent event, int keycode)
            {
                  if(Keys.LEFT == keycode) 
                  {
                      left = false;
                      return true;
                  }
                  if(Keys.RIGHT == keycode) 
                  {
                	  right = false;
                      return true;
                  }
                  if(Keys.UP == keycode) 
                  {
                      up = false;
                      return true;
                  }
                  if(Keys.DOWN == keycode) 
                  {
                      down = false;
                      return true;
                  }
                  return false;
            }
        });
    }
    
    @Override
    public void	act(float delta)
    {
    	currentState.update(delta);
        super.act(delta);
    }
    
    public void setState(State state)
    {
    	if(currentState != state)
    	{
    		state.enter();
    		currentState = state;
    	}
    }
    
    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
    	Color old = batch.getColor(), c = getColor();    	
    	batch.setColor(c.r,c.g,c.b,c.a*parentAlpha);
    	TextureRegion region = selected.getKeyFrame(time);
    	batch.draw(region, getX()-14, getY()-4);
    	batch.setColor(old);
    	
    }
    
    public class IdleState implements State
    {
    	@Override
		public void enter()
		{
    		time = 0;    		
		}

		@Override
		public void update(float delta) 
		{			
			if(left)
			{
				setState(leftState);
			}
			else
			if(right)
			{
				setState(rightState);
			}
			else
			if(up)
			{
				setState(upState);								
			}			
			else
			if(down)
			{
				setState(downState);				
			}			
		}    	
    }
    public class LeftState implements State
    {
    	@Override
		public void enter()
		{
    		selected = leftAnimation;
    		time = selected.getFrameDuration()/4;
		}

		@Override
		public void update(float delta) 
		{		
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();			
			if(!left)
			{				
				if(right)
				{
					setState(rightState);
				}
				else
				if(up)
				{
					setState(upState);
				}
				else
				if(down)
				{
					setState(upState);
				}
				else
				{
					setState(idleState);				
				}
			}
			if(!celda.Disponible(Cube.class, x-14, y))
			{
				//ohh!!! un cubo!!! que hacemos con el?
				Celda celdaCubo = celda.t.getCelda(x-14, y);
				Cube cubo = (Cube)celdaCubo.getPiezaActual();
				cubo.push(Player.this, -1, 0);
			}
			if(celda.Disponible(x-14,y))
			{
				time += delta;
				setX(x-velocity*delta);
			}
			else
			{
				time = 0;
			}
		}    	
    }
    public class RightState implements State
    {
    	@Override
		public void enter()
		{
    		selected = rightAnimation;
    		time = selected.getAnimationDuration()/4;
		}

		@Override
		public void update(float delta) 
		{		
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();			
			if(!right)
			{				
				if(left)
				{
					setState(leftState);
				}
				else
				if(up)
				{
					setState(upState);
				}
				else
				if(down)
				{
					setState(upState);
				}
				else
				{
					setState(idleState);				
				}
			}
			
			if(!celda.Disponible(Cube.class, x+12, y))
			{
				//ohh!!! un cubo!!! que hacemos con el?
				Celda celdaCubo = celda.t.getCelda(x+12, y);
				Cube cubo = (Cube)celdaCubo.getPiezaActual();
				cubo.push(Player.this, 1, 0);
			}
			if(celda.Disponible(x+12, y))
			{
				time += delta;
				setX(x+velocity*delta);
			}
			else
			{
				time = 0;
			}			
		}    	
    }
    public class DownState implements State
    {
    	@Override
		public void enter()
		{
    		selected = downAnimation;
    		time = selected.getAnimationDuration()/4;
		}

		@Override
		public void update(float delta) 
		{		
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();
			if(!down)
			{				
				if(left)
				{
					setState(leftState);
				}
				else
				if(up)
				{
					setState(upState);
				}
				else
				if(right)
				{
					setState(rightState);
				}
				else
				{
					setState(idleState);				
				}
			}
			if(!celda.Disponible(Cube.class, x, y-4))
			{
				//ohh!!! un cubo!!! que hacemos con el?
				Celda celdaCubo = celda.t.getCelda(x, y-4);
				Cube cubo = (Cube)celdaCubo.getPiezaActual();
				cubo.push(Player.this, 0, -1);
			}
			if(celda.Disponible(x, y-4))
			{
				time += delta;
				setY(getY()-velocity*delta);
			}
			else
			{
				time = 0;
			}
			
		}    	
    }
    public class UpState implements State
    {
    	@Override
		public void enter()
		{
    		selected = upAnimation;
    		time = selected.getAnimationDuration()/4;
		}

		@Override
		public void update(float delta) 
		{	
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();
			if(!up)
			{				
				if(left)
				{
					setState(leftState);
				}
				else
				if(down)
				{
					setState(downState);
				}
				else
				if(right)
				{
					setState(rightState);
				}
				else
				{
					setState(idleState);				
				}
			}
			if(!celda.Disponible(Cube.class, x, y+18))
			{
				//ohh!!! un cubo!!! que hacemos con el?
				Celda celdaCubo = celda.t.getCelda(x, y+18);
				Cube cubo = (Cube)celdaCubo.getPiezaActual();
				cubo.push(Player.this, 0, 1);
			}
			if(celda.Disponible(x, y+18))
			{
				time += delta;
				setY(getY()+velocity*delta);
			}
			else
			{
				time = 0;
			}
			
		}    	
    }
    public class NullState implements State
    {
		@Override
		public void enter()
		{			
		}

		@Override
		public void update(float delta) 
		{			
		}
    	
    }
	public void push(Pieza by, int k, int m) 
	{
		final ITablero t = getCeldaActual().t;
		disableInput(true);
		addAction(Actions.sequence
		(
			Actions.moveBy(k*t.boxWidth(), m*t.boxHeight(), 0.5f),
			new Action() 
			{
				@Override
				public boolean act(float arg0) 
				{
					disableInput(false);
					return true;
				}						
			}
		));		
	}
	
	//aqui seria util una maquina de estados con pila, pero no es necesario
	private State lastState;
	private void disableInput(boolean b) 
	{
		if(b)
		{
			lastState = currentState;
			setState(nullState);
		}
		else
		{
			currentState = lastState;
		}		
	}

	private boolean isHunted;
	public void hunted(Enemy enemy)
	{
		if(isHunted) return;
		isHunted = true;
		disableInput(true);
		sound.play(Config.instance.getVolume());
		addAction(Actions.sequence
		(
			Actions.fadeOut(0.5f),
			new Action()
			{
				@Override
				public boolean act(float arg0) 
				{
					getCeldaActual().t.gameEnd("Cazado: Fin del juego");
					return true;
				}
			}			
		));
		
	}    
}
