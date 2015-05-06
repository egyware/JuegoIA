package com.egysoft.ia.juego.tablero;

/**
 *
 * @author Edgardo
 */
public class Celda 
{
    public final ITablero l;    
    public final int i;
    public final int j;
    private IPieza pieza;
    
    /**
     *
     * @param _l
     * @param _i
     * @param _j
     */
    public Celda(ITablero _l, int _i, int _j)    
    {
        l = _l;
        i = _i;
        j = _j;
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
        Celda siguiente = l.getCelda(i+k, j+m);        
        return siguiente != null && siguiente.Disponible();
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
		Celda siguiente = l.getCelda(i+k, j+m);
        return siguiente != null && (siguiente.Disponible()  || (!siguiente.Disponible() && !c.isInstance(siguiente.getPiezaActual())));
	}
}
