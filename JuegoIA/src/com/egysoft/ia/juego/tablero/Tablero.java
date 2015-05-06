package com.egysoft.ia.juego.tablero;

import java.util.Comparator;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.egysoft.ia.juego.Gameloop;
import com.egysoft.ia.juego.actores.Player;
import com.egysoft.ia.juego.actores.Wall;

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
        	return (int)(b.getY()-a.getY());            
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
    private final TextureRegion grass;
    private final Array<TextureAtlas.AtlasRegion> digits;
	private float xi,yi; //posicion inicial
	
        
    public Tablero(AssetManager assets, final int c, final int r,final int bw, final int hw)
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
        TextureAtlas atlas = assets.get("assets/game.atlas");
        Skin skin = assets.get("assets/uiskin.json");
        font = skin.getFont("default-font");
        box = atlas.findRegion("box");
        digits = atlas.findRegions("digit");
        grass = atlas.findRegion("background");
    }
        
    @Override
    public void addActor(Actor actor)
    {
    	if(actor instanceof Player)
    	{
    		actor.setPosition(xi, yi);    		
    	}
		if(actor instanceof IPieza)
		{
		    final IPieza p = (IPieza)actor;
		    int ci = (int) (actor.getX()/boxWidth), cj = (int) (actor.getY()/boxHeight);
		    Celda nueva = grid[cj*columns+ci];
		    nueva.setPiezaActual(p);
		    p.setCeldaActual(nueva);
		}
		super.addActor(actor);
    }
    @Override
    public boolean removeActor(Actor actor)
    {
    	if(super.removeActor(actor))
    	{
    		if(actor instanceof IPieza)
    		{
    			final IPieza p = (IPieza)actor;
    			p.getCeldaActual().setPiezaActual(null);    			    
    		}
    		return true;
    	}
    	return false;
    }
   
    @Override
    public void act(float delta)
    {
    	if(!pausa)
    	{    		
    		super.act(delta);
			getChildren().sort(comparator);			
		    SnapshotArray<Actor> snapshotArray = getChildren();
		    Actor childrens[] = snapshotArray.begin();
		    for(Actor c:childrens) 
		    {
		        if(c instanceof IPieza)
		        {
		            final IPieza p = (IPieza)c;
		            int ci = (int) (c.getX()/boxWidth), cj = (int) (c.getY()/boxHeight);
		            Celda celdaActual = p.getCeldaActual();
		            celdaActual.setPiezaActual(null);
		            Celda nueva = grid[cj*columns+ci];
		            nueva.setPiezaActual(p);
		            p.setCeldaActual(nueva);		        
		        }		        
		    }        
		    snapshotArray.end();		   
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
    		for(int j=0;j<rows;j++)
    		{
    			for(int i=0;i<columns;i++)
    			{
    				batch.draw(grass, i*boxWidth, j*boxHeight);
    			}
    		}
    		super.draw(batch, parentAlfa);    		
    		drawDebugTablero(batch);    		
    		SnapshotArray<Actor> snapshotArray = getChildren();
		    Actor childrens[] = snapshotArray.begin();
    		for(Actor c:childrens) 
  		    {
  		        if(c instanceof IPieza && !(c instanceof Wall))
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
    		for(int j=0;j<rows;j++)
    		{
    			for(int i=0;i<columns;i++)
    			{
    				batch.draw(grass, i*boxWidth, j*boxHeight);
    			}
    		}
    		super.draw(batch, parentAlfa);
    	}
    }
    
    @Override
    public Celda getCelda(int i, int j)
    {
    	if(i < 0 || i >= columns) return null;
    	if(j < 0 || j >= rows) return null;
        return grid[j*columns+i];        
    }

	@Override
	public boolean Disponible(int i, int j) 
	{
		if(i < 0 || i >= columns) return false;
    	if(j < 0 || j >= rows) return false;
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

	@Override
	public int boxWidth() 
	{
		return boxWidth;
	}
	@Override
	public int boxHeight() 
	{
		return boxHeight;
	}

	@Override
	public int columns() 
	{
		return columns;
	}

	@Override
	public int rows() 
	{
		return rows;
	}

	@Override
	public Celda getCelda(float x, float y) 
	{
		return getCelda((int)(x/boxWidth), (int)(y/boxHeight));
	}

	private Gameloop gameloop;
	private int recompensas;
	private int totalRecompensas;
	public void setGameloop(Gameloop gameloop) 
	{	
		this.gameloop = gameloop;
	}
	public Gameloop getGameloop()
	{
		return gameloop;
	}

	@Override
	public void gameEnd(String t) 
	{
		pausa = true;
		gameloop.gameEnd(t);		
	}

	public void setInitialPosition(int xi, int yi) 
	{
		this.xi = xi;
		this.yi = yi;
	}

	public void setTotalRecompensas(int t)
	{
		totalRecompensas = t;
	}
	
	@Override
	public void addRecompensa(int i) 
	{
		recompensas += i;		
	}
	
	@Override
	public int getRecompensas()
	{
		return recompensas;
	}
	
	@Override
	public int getTotalRecompensas() 
	{
		return totalRecompensas;
	}
	
}
