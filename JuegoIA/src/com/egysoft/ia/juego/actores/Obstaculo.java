package com.egysoft.ia.juego.actores;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Obstaculo extends Pieza
{
	private TextureRegion texture;
	public Obstaculo(String assetName, TextureAtlas atlas)
	{
		texture = atlas.findRegion(assetName);
	}
	
	public void draw(Batch batch, float parentAlfa)
	{
		batch.draw(texture, getX(), getY());
	}	
	
}
