package com.egysoft.ia.juego.actores;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.ITablero;
import com.egysoft.ia.juego.tablero.Pieza;

public class Cube extends Pieza 
{
	private static Random random = new Random(System.currentTimeMillis());
	private TextureRegion texture;
	private boolean isPushing = false;
	
	public Cube(String assetName, TextureAtlas atlas)
	{		
		texture = atlas.findRegion(assetName, random.nextInt(10)<4?1:2);
	}
	
	public void draw(Batch batch, float parentFloat)
	{
		batch.draw(texture, getX(), getY());
	}

	public boolean push(Pieza pieza, int k, int m)
	{
		if(isPushing) return true;
		final Celda celda = getCeldaActual();
		final ITablero t = celda.t;
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
