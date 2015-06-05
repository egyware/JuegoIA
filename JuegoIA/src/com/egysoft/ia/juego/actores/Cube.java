package com.egysoft.ia.juego.actores;

import java.util.Random;

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
import com.egysoft.ia.juego.tablero.ITablero;
import com.egysoft.ia.juego.tablero.Pieza;
import com.egysoft.ia.juego.tablero.Pushable;

public class Cube extends Pieza implements Pushable
{
	private static Random random = new Random(System.currentTimeMillis());
	private TextureRegion texture;
	private Sound sound;
	private boolean isPushing = false;
	
	public Cube(String assetName, TextureAtlas atlas, Sound sound)
	{		
		texture = atlas.findRegion(assetName, random.nextInt(10)<4?1:2);
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
		if(!celda.Disponible(Lair.class,k,m))
		{
			//una guarida, en realidad tengo demasiado sueño para exaltarme x'D
			//bueno el cubo deberia desaparecer
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
			return true;			
		}else
		if(!celda.Disponible(Cube.class,k,m))
		{
			//otro cubo o.o!!!
			final Celda otra = celda.t.getCelda(celda.i+k, celda.j+m);
			final Cube otro = (Cube) otra.getPiezaActual();
			if(otro.push(this,k, m)) return true;			
			else 
			{
				if(pieza instanceof Player) //nop, no está isdisponible
				{
					final Player p = (Player)pieza;
					p.push(this, k, m);
					isPushing = true;			
					addAction(Actions.sequence
					(
						Actions.moveBy(-k*t.boxWidth(), -m*t.boxHeight(), 0.5f), 
						new PushFalse()
					));
				}
			}
		}else		
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
		else if(pieza instanceof Player) //nop, no está isdisponible
		{
			final Player p = (Player)pieza;
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
	
	
}
