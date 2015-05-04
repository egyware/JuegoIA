package com.egysoft.ia.juego;

/**
 *
 * @author Edgardo
 */
public interface ITablero
{
    public Celda getCelda(int i,int j);
    public boolean Disponible(int i, int j);
    
}
