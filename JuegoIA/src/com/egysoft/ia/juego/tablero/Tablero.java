package com.egysoft.ia.juego.tablero;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.egysoft.ia.juego.Player;
import com.egysoft.ia.juego.actores.Obstaculo;

import java.util.Comparator;

/**
 * Esta clase esta basada en Libfdx/Group, sin embargo en vez de usar un SnapshotArray utilza un grid. 
 * @author Edgardo
 */
public class Tablero extends Group implements ITablero
{
    private boolean debug;
    private boolean pausa;
    public static class ActorComparator implements Comparator<Actor> 
    {
        @Override
        public int compare (Actor a, Actor b) 
        {
            int r = (int)(b.getY() - a.getY());
            if(r == 0) return (int)(a.getX() - b.getY());
            else return r;            
        }
    }
    public final int boxWidth;
    public final int boxHeight;
    public final int columns;
    public final int rows; 
    private final Celda grid[];
    private final ActorComparator comparator = new ActorComparator();
    private final BitmapFont font;
    private final TextureRegion box;
    private final Array<TextureAtlas.AtlasRegion> digits;
        
    public Tablero(TextureAtlas atlas, BitmapFont f, final int c, final int r,final int bw, final int hw)
    {
    	boxWidth = bw;
        boxHeight = hw;
        columns = c;
        rows = r;
        grid = new Celda[columns*rows];
        for(int j=0;j<rows;j++)        
        {
            for(int i=0;i<columns;i++)
            {
                grid[j*columns+i] = new Celda(this, i,j);
            }
        }	
        font = f;
        box = atlas.findRegion("box");
        digits = atlas.findRegions("digit");
    }
        
    @Override
    public void addActor(Actor actor)
    {
		if(actor instanceof IPieza)
		{
		    final IPieza p = (IPieza)actor;
		    int ci = (int) actor.getX()/boxWidth, cj = (int) actor.getY()/boxHeight;
		    Celda nueva = grid[cj*columns+ci];
		    nueva.setPiezaActual(p);
		    p.setCeldaActual(nueva);
		}
		super.addActor(actor);
    }
   
    @Override
    public void act(float delta)
    {
    	if(!pausa)
    	{    		
			getChildren().sort(comparator);
		    SnapshotArray<Actor> snapshotArray = getChildren();
		    Actor childrens[] = snapshotArray.begin();
		    for(Actor c:childrens) 
		    {
		        if(c instanceof IPieza)
		        {
		            final IPieza p = (IPieza)c;
		            int ci = (int) c.getX()/boxWidth, cj = (int) c.getY()/boxHeight;
		            Celda celdaActual = p.getCeldaActual();
		            celdaActual.setPiezaActual(null);
		            Celda nueva = grid[cj*columns+ci];
		            nueva.setPiezaActual(p);
		            p.setCeldaActual(nueva);
		        }
		    }        
		    snapshotArray.end();
		    super.act(delta);
    	}
    }
    
    private void drawDebugTablero(Batch batch)
    {
    	for(int j=0;j<rows;j++)
		{
			for(int i=0;i<columns;i++)
			{
				int x = i*boxWidth, y = j*boxHeight;
				batch.draw(box, x, y);
				String text = String.format("%d %d", i, j);
				x-=2;y+=2;
				for(char c:text.toCharArray())
				{
					x+=4;
					if(c==' ') continue;					
					batch.draw(digits.get(c-'0'),x,y);					
				}
			}			
		}		
    }
    
    public void draw(Batch batch, float parentAlfa)
    {    	
    	if(debug)
    	{
    		super.draw(batch, parentAlfa);
    		drawDebugTablero(batch);    		
    		SnapshotArray<Actor> snapshotArray = getChildren();
		    Actor childrens[] = snapshotArray.begin();
    		for(Actor c:childrens) 
  		    {
  		        if(c instanceof IPieza && !(c instanceof Obstaculo))
  		        {  		        	
	        		Celda celda = ((IPieza)c).getCeldaActual();
	        		float x = c.getX(), y = c.getY();  		        	
	        		font.draw(batch, String.format("%s(%.0f,%.0f)", c.toString(),x,y), x, y);  		        	
	        		font.draw(batch, String.format("Celda(%d,%d)",celda.i, celda.j), x, y-18);  		        	
  		        }
  		    }
    		snapshotArray.end();   	
    	}
    	else
    	{
    		super.draw(batch, parentAlfa);
    	}
    }
    
    @Override
    public Celda getCelda(int i, int j)
    {
        return grid[j*columns+i];        
    }

	@Override
	public boolean Disponible(int i, int j) 
	{
		return grid[j*columns+i].Disponible();
	}

	public void setPausa(boolean pause)
	{
		pausa = pause;		
	}

	public boolean getGameDebug()
	{		
		return debug;
	}

	public void setGameDebug(boolean b)
	{
		debug = b;	
	}
}
