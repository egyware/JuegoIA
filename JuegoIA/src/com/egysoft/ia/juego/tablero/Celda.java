package com.egysoft.ia.juego.tablero;

/**
 *
 * @author Edgardo
 */
public class Celda 
{
    public final ITablero t;    
    public final int i;
    public final int j;
    public final int x;
    public final int y;
    private IPieza pieza;
    
    /**
     *
     * @param _t
     * @param _i
     * @param _j
     */
    public Celda(ITablero _t, int _i, int _j)    
    {
        t = _t;
        i = _i;
        j = _j;
        x = i*t.boxWidth();
        y = j*t.boxHeight();
    }

    public IPieza getPiezaActual()
    {
        return pieza;
    }
    public void setPiezaActual(IPieza p)
    {
        pieza = p;
    }
    
    public boolean Disponible()
    {
    	return pieza == null;
    }
    
    public boolean Disponible(int k, int m) 
    {
        Celda siguiente = t.getCelda(i+k, j+m);        
        return siguiente != null && siguiente.Disponible();
    }
    
    public boolean Disponible(float x, float y) 
    {
    	int k = (int)(x/t.boxWidth())-i, m = (int)(y/t.boxHeight())-j;    	
    	if(k == 0 && m == 0) return true;
    	else return Disponible(k,m);        
    }

    /**
     * Pregunta si la celda siguiente k,m esta disponible.
     * @param c El tipo de la pieza que debe contener la celda para considerarla ocupada. 
     * @param k k-esima columna a partir esta celda.
     * @param m m-esima fila a partir de esta celda.
     * @return True si la celda está disponible. False si la celda está ocupada por un tipo de pieza c.
     */
	public boolean Disponible(Class<? extends IPieza> c, int k, int m)
	{
		Celda siguiente = t.getCelda(i+k, j+m);
        return siguiente != null && (siguiente.Disponible()  || (!siguiente.Disponible() && !c.isInstance(siguiente.getPiezaActual())));
	}	
}
