package com.egysoft.ia.juego.tablero;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Pieza
{
	private static int LEFT = 0;
	private static int RIGHT = 0;
	private static int TOP = 0;
	private static int DOWN = 0;
	private TextureRegion texture[];
	
	private int estado = LEFT;
	private float time = 0;
	
	public Enemy(String assetName, TextureAtlas atlas)
	{
		Array<AtlasRegion> regions = atlas.findRegions(assetName);
		texture = new TextureRegion[4];
		int i=0;
		for(AtlasRegion r:regions)
		{
			texture[i++] = r;
		}
	}
	
	public void act(float delta)
	{
		time += delta;
	}
	
	public void draw(Batch batch, float parentAlfa)
	{
		float x= getX(), y = getY() + MathUtils.cos(MathUtils.PI2*time)+5;
		batch.draw(texture[estado], x,y);
	}
	
}
