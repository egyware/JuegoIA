package com.egysoft.ia.juego;

/**
 *
 * @author Edgardo
 */
public class Celda 
{
    public final Laberinto l;    
    public final int i;
    public final int j;
    private Pieza pieza;
    
    /**
     *
     * @param _l
     * @param _i
     * @param _j
     */
    public Celda(Laberinto _l, int _i, int _j)    
    {
        l = _l;
        i = _i;
        j = _j;
    }

    public Pieza getPiezaActual()
    {
        return pieza;
    }
    public void setPiezaActual(Pieza p)
    {
        pieza = p;
    }
    
    boolean Disponible() 
    {
        return pieza != null;
    }
}
