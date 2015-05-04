package com.egysoft.ia.juego.tablero;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

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
        
    public Tablero(final int c, final int r,final int bw, final int hw)
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
}
