package com.egysoft.ia.juego.actores;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.State;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Pieza;

public class Enemy extends Pieza
{
	private static int RIGHT = 0;
	private static int UP = 1;
	private static int DOWN = 2;
	private static int LEFT = 3;
	
	public static float velocity = 50;
	private TextureRegion texture[];
	private TextureRegion selected;
	
	private State currentState;	
	private float time = 0;	
	private final State states[];
	private Random random = new Random(System.currentTimeMillis());
	
	public Enemy(String assetName, TextureAtlas atlas)
	{
		Array<AtlasRegion> regions = atlas.findRegions(assetName);
		texture = new TextureRegion[4];
		int i=0;
		for(AtlasRegion r:regions)
		{
			texture[i++] = r;
		}
		
		states = new State[4];
				
		states[UP] = new UpState();		
		states[DOWN] = new DownState();
		states[LEFT] = new LeftState();
		states[RIGHT] = new RightState();
	    
	    setState(states[random.nextInt(4)]);
	}
	
	public void setState(State state)
    {
    	if(currentState != state)
    	{
    		state.enter();
    		currentState = state;
    	}
    }
		
	public void act(float delta)
	{
		time += delta;
		currentState.update(delta);
	}
	
	public void draw(Batch batch, float parentAlfa)
	{
		float x= getX()-selected.getRegionWidth()/2, y = getY() + MathUtils.cos(MathUtils.PI2*time)+5;
		batch.draw(selected, x,y);
	}
	
  public class LeftState implements State
    {
    	@Override
		public void enter()
		{
    		selected = texture[LEFT];    		
		}

		@Override
		public void update(float delta) 
		{		
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();
			if(!celda.Disponible(HumanPlayer.class, x-8, y))
			{
				HumanPlayer p = (HumanPlayer) celda.t.getCelda(x-8, y).getPiezaActual();
				p.hunted(Enemy.this);
				setX(x-velocity*delta);
			}
			else
			if(celda.Disponible(x-8,y))
			{
				setX(x-velocity*delta);
			}
			else
			{	
				int i = random.nextInt(4);
				State next = states[i];
				if(next == currentState)
				{
					next = states[(i+1)%4];
				}
				setState(next);
			}
		}    	
    }
    public class RightState implements State
    {
    	@Override
		public void enter()
		{
    		selected = texture[RIGHT];
		}

		@Override
		public void update(float delta) 
		{		
			final float x = getX(), y = getY();			
			final Celda celda = getCeldaActual();
			if(!celda.Disponible(HumanPlayer.class, x+8, y))
			{
				HumanPlayer p = (HumanPlayer) celda.t.getCelda(x+8, y).getPiezaActual();
				p.hunted(Enemy.this);
				setX(x+velocity*delta);
			}
			else
			if(celda.Disponible(x+8, y))
			{
				setX(x+velocity*delta);
			}
			else
			{
				int i = random.nextInt(4);
				State next = states[i];
				if(next == currentState)
				{
					next = states[(i+1)%4];
				}
				setState(next);
			}			
		}    	
    }
    public class DownState implements State
    {
    	@Override
		public void enter()
		{
    		selected = texture[DOWN];
		}

		@Override
		public void update(float delta) 
		{		
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();
			
			if(!celda.Disponible(HumanPlayer.class, x, y-4))
			{
				HumanPlayer p = (HumanPlayer) celda.t.getCelda(x, y-4).getPiezaActual();
				p.hunted(Enemy.this);
				setY(getY()-velocity*delta);
			}else
			if(celda.Disponible(x, y-4))
			{
				setY(getY()-velocity*delta);
			}
			else
			{			
				int i = random.nextInt(4);
				State next = states[i];
				if(next == currentState)
				{
					next = states[(i+1)%4];
				}
				setState(next);
			}
			
		}    	
    }
    public class UpState implements State
    {
    	@Override
		public void enter()
		{
    		selected = texture[UP];
		}

		@Override
		public void update(float delta) 
		{	
			final Celda celda = getCeldaActual();
			final float x = getX(), y = getY();
			//EL JUGADOR!!!
			if(!celda.Disponible(HumanPlayer.class, x, y+18))
			{
				HumanPlayer p = (HumanPlayer) celda.t.getCelda(x, y+18).getPiezaActual();
				p.hunted(Enemy.this);
				setY(getY()+velocity*delta);
			}else
			if(celda.Disponible(x, y+18))
			{
				setY(getY()+velocity*delta);
			}
			else
			{
				int i = random.nextInt(4);
				State next = states[i];
				if(next == currentState)
				{
					next = states[(i+1)%4];
				}
				setState(next);
			}
			
		}    	
    }
}
