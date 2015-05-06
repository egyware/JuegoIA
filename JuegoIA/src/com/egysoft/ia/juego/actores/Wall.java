package com.egysoft.ia.juego.actores;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Pieza;

public class Wall extends Pieza
{
	private static int[] topTable = 
	{
		5,5,5,3,
		5,5,1,5,
		5,6,5,5,
		4,5,5,5
	};
	//esta tabla contiene 0 para indicar que no es necesario pintar una murralla ahi
	private static int[] wallTable = 
	{
		8,8,0,0,
		8,8,0,0,
		8,9,0,0,
		7,8,0,0
	};
	//podria ser perfectamente una variable estatica, pero utilizaré esto a mi favor.	
	private Array<AtlasRegion> tiles;
	private TextureRegion top;
	private TextureRegion wall;
	public Wall(String assetName, TextureAtlas atlas)
	{
		tiles = atlas.findRegions(assetName);
		top = tiles.get(1);
		wall = tiles.get(7);
	}
	
	public void act(float delta)
	{
		final Celda celda = getCeldaActual();
		int a = (celda.j+1 < celda.t.rows()    && !celda.Disponible(Wall.class, 0, 1))?8:0;
		int b = (celda.i+1 < celda.t.columns() && !celda.Disponible(Wall.class, 1, 0))?4:0;
		int c = (celda.j-1 >= 0                && !celda.Disponible(Wall.class, 0,-1))?2:0;
		int d = (celda.i-1 >= 0                && !celda.Disponible(Wall.class,-1, 0))?1:0;
		int index = a|b|c|d;
		int wallIndex = wallTable[index]-1; //los indices de la tabla están apuntando a las imagenes (indice basado en 1)
		top  = tiles.get(topTable[index]-1); 		
		wall = (wallIndex>=0)?tiles.get(wallIndex):null;		
	}
	
	public void draw(Batch batch, float parentAlfa)
	{
		float x = getX(),y=getY();
		batch.draw(top,  x, y+23);
		if(wall != null) batch.draw(wall, x, y-9);
	}	
	
}
