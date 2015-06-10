package com.egysoft.ia.juego.tablero;

/**
 * Indica si es empujable o no..
 * @author Edgardo
 *
 */
public interface Pushable 
{
	boolean push(IPieza pieza, int k, int m);
}
