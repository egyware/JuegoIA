package com.egysoft.ia.juego.tablero;

/**
 *
 * @author Edgardo
 */
public interface ITablero
{
    public Celda getCelda(int i,int j);
    public boolean Disponible(int i, int j);
    
}
