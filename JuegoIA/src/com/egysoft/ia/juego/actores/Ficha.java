package com.egysoft.ia.juego.actores;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.egysoft.ia.juego.Config;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.IPieza;
import com.egysoft.ia.juego.tablero.IPlayer;
import com.egysoft.ia.juego.tablero.IPushable;
import com.egysoft.ia.juego.tablero.ITablero;
import com.egysoft.ia.juego.tablero.Pieza;

/**
 * Crearé la ficha a partir de un cubo, además tiene gran parte de la funcionalidad lista
 * @author Edgardo
 *
 */
public class Ficha extends Pieza implements IPushable
{
	/**
	 * Tipo de ficha
	 */
	public static enum Tipo
	{
		Ficha_1(1),
		Ficha_2(2),
		Ficha_3(3),
		Ficha_A(4),
		Ficha_B(5),
		Ficha_C(6);
		
		private final int index;//indice de la imagen
		private Tipo(int i)
		{
			index = i;
		}
	}
	private TextureRegion texture;
	private Sound sound;
	private boolean isPushing = false;	
	private Ficha pareja;
	
	public Ficha(String assetName, Tipo tipo, TextureAtlas atlas, Sound sound)
	{		
		texture = atlas.findRegion(assetName, tipo.index);
		this.sound = sound;		
	}
	
	public void draw(Batch batch, float parentAlpha)
	{
		Color old = batch.getColor(), c = getColor();    	
    	batch.setColor(c.r,c.g,c.b,c.a*parentAlpha);    	
    	batch.draw(texture, getX(), getY());
    	batch.setColor(old);
		
	}

	public boolean push(IPieza pieza, int k, int m)
	{
		if(isPushing) return true;
		final Celda celda = getCeldaActual();
		final ITablero t = celda.t;
		//si choco con otra ficha
		if(!celda.Disponible(Ficha.class,k,m))
		{
			Ficha otra = (Ficha)celda.Obtener(k, m).getPiezaActual();
			if(pareja == otra) //si son pareja o no 
			{
				//lo hago desaparecer
				isPushing = true;
				sound.play(Config.instance.getVolume());
				addAction(Actions.sequence
				(
						Actions.parallel
						(
							Actions.fadeOut(0.5f),
							Actions.moveBy(k*t.boxWidth(), m*t.boxHeight(),0.5f)
						),
						new Action()
						{
							@Override
							public boolean act(float arg0) 
							{
								t.addRecompensa(1);	
								if(t.getRecompensas() >= t.getTotalRecompensas())
							    {
									t.gameEnd("Coleccionador: Ganaste");
							    }
								return true;
							}
							
						},
						Actions.removeActor()					
				));
				//la otra tambien deparece
				otra.addAction(Actions.sequence
				(
						Actions.parallel
						(
							Actions.fadeOut(0.5f),
							Actions.moveBy(-k*t.boxWidth(), -m*t.boxHeight(),0.5f)
						),
						new Action()
						{
							@Override
							public boolean act(float arg0) 
							{
								t.addRecompensa(1);	
								if(t.getRecompensas() >= t.getTotalRecompensas())
							    {
									t.gameEnd("Coleccionador: Ganaste");
							    }
								return true;
							}
							
						},
						Actions.removeActor()					
				));
				return true;
			} 
			else //no son pareja 
			{
				return false; // no puede ser empujado
			}
						
		}		
		if(celda.Disponible(k, m))
		{
			isPushing = true;			
			addAction(Actions.sequence
			(
				Actions.moveBy(k*t.boxWidth(), m*t.boxHeight(), 0.5f), 
				new PushFalse()
			));
			return true;
		}
		else if(pieza instanceof IPlayer) //nop, no está isdisponible
		{
			final IPlayer p = (IPlayer)pieza;
			p.push(this, k, m);
			isPushing = true;			
			addAction(Actions.sequence
			(
				Actions.moveBy(-k*t.boxWidth(), -m*t.boxHeight(),0.5f), 
				new PushFalse()
			));
			return true;
		}
		return false;
	}
	
	private class PushFalse extends Action
	{
		@Override
		public boolean act(float arg0)
		{
			isPushing = false;
			return true;
		}
	}
	
	public void emparejar(Ficha ficha) 
	{
		this.pareja = ficha;		
	}

	public Pieza getPareja() 
	{
		return pareja;
	}
	
}
