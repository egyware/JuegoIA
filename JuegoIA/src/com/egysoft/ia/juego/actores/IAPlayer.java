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
import com.egysoft.ia.juego.algoritmos.Estado;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.IPlayer;
import com.egysoft.ia.juego.tablero.IPushable;
import com.egysoft.ia.juego.tablero.ITablero;
import com.egysoft.ia.juego.tablero.Operacion;
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
			int h=0;
			Array<Ficha> fichas = getCeldaActual().t.get(Ficha.class);
			for(Ficha ficha:fichas)
			{
				final Celda celda = ficha.getCeldaActual();
				h+= Math.abs(actual.i - celda.i) + Math.abs(actual.j-celda.j);
			}			
			return h;
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
			
			Array<Array<Estado>> rutas = new Array<Array<Estado>>();
			Array<Ficha> fichas = actual.t.get(Ficha.class);	
			for(int i=0;i<fichas.size;i++) //a la antigüita no má
			{
				final Ficha ficha = fichas.get(i);
				Array<Estado> ruta1 = algoritmo.buscar(ficha.getCeldaActual(), ficha.getPareja().getCeldaActual());
				Estado ultimo = ruta1.first();
				Operacion ultima = ultimo.operacion;
				Celda inicio_ruta2 = ultimo.celda.Disponible(ultima.k,ultima.m)?ultimo.celda.Obtener(ultima.k, ultima.m):ultimo.celda.Obtener(-ultima.k, -ultima.m);
				Array<Estado> ruta2 = algoritmo.buscar(inicio_ruta2, actual);				

				Array<Estado> ruta = combinar(ruta1, ruta2);
				//System.out.println(ruta);
				
				if(ruta != null) rutas.add(ruta);				
			}
			rutas.sort(masCorto);
			
			if(rutas.size > 0) operaciones = rutas.first();
			
    		if(operaciones == null)
    		{
    			//HACER QUE NO SABES x'D
    			return;
    		}	    		
    		
    		System.out.println(String.format("Ruta: %s",operaciones.toString()));			
	    	
	    	if(operaciones != null && operaciones.size > 0)
	    	{
	    		clearActions();
	    		addAction(popEstado);
	    	}	    	
		}		
    }
    /**
     * Combina dos rutas
     * @param ruta1 ruta desde una ficha X a la ficha Y
     * @param ruta2 ruta desde la celda siguiente de la ficha Y hasta el jugador
     * @return ruta desde el jugador hasta la ficha X
     */
    private static Array<Estado> combinar(Array<Estado> ruta1, Array<Estado> ruta2) 
	{
		if(ruta1 == null || ruta2 == null) return null;
		Array<Estado> ruta = new Array<Estado>();
		//voy para atras
		
		Estado inicial = ruta2.first();
		Estado anterior = new Estado(inicial.celda);
		ruta.add(anterior);
		
		System.out.println(ruta2);
		for(int i = 1;i<ruta2.size;i++)
		{
			Estado actual = ruta2.get(i);
			int k = anterior.celda.i - actual.celda.i;
			int m = anterior.celda.j - actual.celda.j;
			if(k==-1 && m == 0)
				anterior = new Estado(actual.celda, Operacion.Derecha ,anterior);
			if(k==1 && m == 0)
				anterior = new Estado(actual.celda, Operacion.Izquierda ,anterior);
			if(k==0 && m == -1)
				anterior = new Estado(actual.celda, Operacion.Arriba ,anterior);
			if(k==0 && m == 1)
				anterior = new Estado(actual.celda, Operacion.Abajo ,anterior);
			ruta.add(anterior);
		}
		
		for(int i = 0;i<ruta1.size;i++)
		{
			Estado actual = ruta1.get(i);
			int k = anterior.celda.i - actual.celda.i;
			int m = anterior.celda.j - actual.celda.j;
			if(k==-1 && m == 0)
				anterior = new Estado(actual.celda, Operacion.Derecha ,anterior);
			if(k==1 && m == 0)
				anterior = new Estado(actual.celda, Operacion.Izquierda ,anterior);
			if(k==0 && m == -1)
				anterior = new Estado(actual.celda, Operacion.Arriba ,anterior);
			if(k==0 && m == 1)
				anterior = new Estado(actual.celda, Operacion.Abajo ,anterior);
			ruta.add(anterior);
		}
		
		//invierte
		Array<Estado> rutaDefinitiva = new Array<Estado>(ruta.size);
		while(ruta.size > 0)
		{
			rutaDefinitiva.add(ruta.pop());
		}
		
		
		return rutaDefinitiva;
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
				do
				{
					estado = operaciones.pop();
				}
				while(Operacion.Inicial == estado.operacion && operaciones.size > 0); //solo doy una vueltecita más si esta operacion es Inicial
				System.out.println(estado);
    			switch(estado.operacion) //voy al revez
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
