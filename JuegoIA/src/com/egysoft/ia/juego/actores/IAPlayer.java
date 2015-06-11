package com.egysoft.ia.juego.actores;

import java.util.Comparator;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.Config;
import com.egysoft.ia.juego.State;
import com.egysoft.ia.juego.algoritmos.Busqueda;
import com.egysoft.ia.juego.algoritmos.BusquedaAStar;
import com.egysoft.ia.juego.algoritmos.BusquedaPorAnchura;
import com.egysoft.ia.juego.algoritmos.BusquedaPorProfundidad;
import com.egysoft.ia.juego.algoritmos.Estado;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.IPlayer;
import com.egysoft.ia.juego.tablero.IPushable;
import com.egysoft.ia.juego.tablero.ITablero;
import com.egysoft.ia.juego.tablero.Pieza;
import com.egysoft.ia.juego.tablero.Tablero;

/**
 *
 * @author Edgardo
 */
public class IAPlayer extends Pieza implements IPlayer
{
	private static final float velocity = 30.0f; // 50 pix/s
	private static final float movtime = 32/velocity; // 32 pix / 50 pix/s
	private static final Comparator<Array<Estado>> masCorto = new Comparator<Array<Estado>>()
	{
		@Override
		public int compare(Array<Estado> arg0, Array<Estado> arg1) 
		{
			return arg0.size - arg1.size;
		}
	};
	
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
    
    public IAPlayer(String assetName, TextureAtlas atlas, Sound sound)
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
        
        popEstado = new PopEstado();
        
        this.sound = sound;
        
        setState(idleState);             
    }
    
    //private Busqueda algoritmo = new BusquedaPorAnchura();
    private Busqueda algoritmo = new BusquedaAStar(
    new BusquedaAStar.G() 
    {		
		@Override
		public int g(int costo, Celda actual) 
		{
			return costo+1;
		}
	},
	new BusquedaAStar.H()
	{
		@Override
		public int h(Celda actual) 
		{
			int h1=0,h2=0;
			Array<Lair> lairs = getCeldaActual().t.get(Lair.class);
			for(Lair lair:lairs)
			{
				final Celda celda = lair.getCeldaActual();
				h1+= Math.abs(actual.i - celda.i) + Math.abs(actual.j-celda.j);
			}			
			Array<Cube> cube = getCeldaActual().t.get(Cube.class);
			for(Cube coin:cube)
			{
				final Celda celda = coin.getCeldaActual();
				h2+= Math.abs(actual.i - celda.i) + Math.abs(actual.j-celda.j);
			}		
			
			return (int)(h1*0.9f+h2*0.1f);
		}		
	});
    private final PopEstado popEstado;
    private Array<Estado> operaciones;
    private Estado estado = null;
    @Override
    public void	act(float delta)
    {
    	time += delta;
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
    	batch.draw(region, getX()-14, getY()-12);
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
			final Celda actual = getCeldaActual();
			
			//Array<Array<Estado>> caminos = new Array<Array<Estado>>();
			
			Array<Cube> cubes = actual.t.get(Cube.class);
			
			if(cubes.size > 0) operaciones = algoritmo.buscar(actual, cubes.first().getCeldaActual());			
    				
    		if(operaciones == null)
    		{
    			//HACER QUE NO SABES x'D
    		}	    		
    		
    		System.out.println(operaciones);			
	    	
	    	if(operaciones != null && operaciones.size > 0)
	    	{
	    		operaciones.pop();	    		
	    		clearActions();
	    		addAction(popEstado);
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
			final Celda celda = estado.celda;
			
			if(!celda.Disponible(IPushable.class))
			{
				//ohh!!! un cubo!!! que hacemos con el?				
				IPushable cubo = (IPushable)celda.getPiezaActual();
				if(cubo.push(IAPlayer.this, -1, 0)) return;				
			}
			
			if(celda.Disponible())
			{
				addAction(Actions.sequence(
						Actions.moveTo(estado.celda.x+Tablero.ColumnWidth/2, estado.celda.y+Tablero.ColumnHeight/2, movtime),
						popEstado));
				setState(nullState);
			}
			else
			{
				setState(idleState);
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
			final Celda celda = estado.celda;
			
			if(!celda.Disponible(IPushable.class))
			{
				//ohh!!! un cubo!!! que hacemos con el?				
				IPushable cubo = (IPushable)celda.getPiezaActual();
				if(cubo.push(IAPlayer.this, 1, 0)) return;
			}
			if(celda.Disponible())
			{
				addAction(Actions.sequence(
						Actions.moveTo(estado.celda.x+Tablero.ColumnWidth/2, estado.celda.y+Tablero.ColumnHeight/2, movtime),
						popEstado));
				setState(nullState);
			}
			else
			{
				setState(idleState);
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
			final Celda celda = estado.celda;
			
			if(!celda.Disponible(IPushable.class))
			{
				//ohh!!! un cubo!!! que hacemos con el?				
				IPushable cubo = (IPushable)celda.getPiezaActual();
				if(cubo.push(IAPlayer.this, 0, -1))return;
			}
			if(celda.Disponible())
			{
				addAction(Actions.sequence(
						Actions.moveTo(estado.celda.x+Tablero.ColumnWidth/2, estado.celda.y+Tablero.ColumnHeight/2, movtime),
						popEstado));
				setState(nullState);
			}
			else
			{
				setState(idleState);
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
			final Celda celda = estado.celda;
			
			if(!celda.Disponible(IPushable.class))
			{
				//ohh!!! un cubo!!! que hacemos con el?				
				IPushable cubo = (IPushable)celda.getPiezaActual();
				if(cubo.push(IAPlayer.this, 0, 1)) return;
			}
			if(celda.Disponible())
			{
				addAction(Actions.sequence(
						Actions.moveTo(estado.celda.x+Tablero.ColumnWidth/2, estado.celda.y+Tablero.ColumnHeight/2, movtime),
						popEstado));
				setState(nullState);
			}
			else
			{
				setState(idleState);
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
    public class PopEstado extends Action
    {
		@Override
		public boolean act(float arg0) 
		{
			if(operaciones != null && operaciones.size > 0)
			{
				estado = operaciones.pop();
				System.out.println(estado);
    			switch(estado.operacion)
    	    	{
    			case Abajo:		
    				setState(downState);
    				break;
    			case Arriba:			
    				setState(upState);			
    				break;
    			case Derecha:			
    				setState(rightState);	
    				break;		
    			case Izquierda:
    				setState(leftState);	
    				break;
    			case Inicial:	    				
    				break;
    			default:
    				break;    	
    	    	}
			}
			else
			{
				setState(idleState);
			}
			return true;
		}    	
    }
    
	public void push(Pieza by, int k, int m) 
	{
		final ITablero t = getCeldaActual().t;
		setState(nullState);
		addAction(Actions.sequence
		(
			Actions.moveBy(k*t.boxWidth(), m*t.boxHeight(), 0.5f),
			new Action() 
			{
				@Override
				public boolean act(float arg0) 
				{
					setState(idleState);
					return true;
				}						
			}
		));		
	}
	
	
	private boolean isHunted;
	public void hunted(Enemy enemy)
	{
		if(isHunted) return;
		isHunted = true;
		
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
