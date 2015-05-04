package com.egysoft.ia.juego;

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
    
    boolean Disponible() 
    {
        return pieza != null;
    }
}
