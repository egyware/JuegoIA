package com.egysoft.ia.juego.actores;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.tablero.Pieza;

public class Obstaculo extends Pieza
{
	//podria ser perfectamente una variable estatica, pero utilizaré esto a mi favor.
	private Array<AtlasRegion> tiles;
	private TextureRegion top;
	private TextureRegion wall;
	public Obstaculo(String assetName, TextureAtlas atlas)
	{
		tiles = atlas.findRegions(assetName);
		top = tiles.get(1);
		wall = tiles.get(7);
	}
	
	public void draw(Batch batch, float parentAlfa)
	{
		float x = getX(),y=getY();
		batch.draw(top,  x, y+23);
		batch.draw(wall, x, y-9);
	}	
	
}
