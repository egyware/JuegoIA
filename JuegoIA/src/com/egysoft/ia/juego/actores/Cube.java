package com.egysoft.ia.juego.actores;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.egysoft.ia.juego.tablero.Pieza;

public class Cube extends Pieza 
{
	private static Random random = new Random(System.currentTimeMillis());
	private TextureRegion texture;
	
	public Cube(String assetName, TextureAtlas atlas)
	{		
		texture = atlas.findRegion(assetName, random.nextInt(10)<4?1:2);
	}
	
	public void draw(Batch batch, float parentFloat)
	{
		batch.draw(texture,  getX()+texture.getRegionWidth()/2, getY()+16);
	}
	
	
}
