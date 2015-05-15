package com.egysoft.ia.juego.actores;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.egysoft.ia.juego.tablero.Pieza;

public class Lair extends Pieza
{
	private TextureRegion top;
	private TextureRegion wall;
	public Lair(String assetName, TextureAtlas atlas)
	{
		top  = atlas.findRegion(assetName, 1);
		wall = atlas.findRegion(assetName, 2);
	}
	
	public void draw(Batch batch, float parentAlfa)
	{
		float x = getX(),y=getY();
		batch.draw(top,  x, y+23);
		batch.draw(wall, x, y-9);
	}	
	
}
