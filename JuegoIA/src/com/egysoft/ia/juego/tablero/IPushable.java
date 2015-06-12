package com.egysoft.ia.juego.tablero;

/**
 * Indica si es empujable o no..
 * @author Edgardo
 *
 */
public interface IPushable 
{
	/**
	 * Devuelve true si esta pieza fue empujada o no
	 * @param pieza quien la empuja
	 * @param k direccion en el eje x. Solo puede ser -1,0,1
	 * @param m direccion en el eje y. Solo puede ser -1,0,1
	 * @return True si la pieza fue empujada, false si no pudo ser empujada.
	 */
	boolean push(IPieza pieza, int k, int m);
}
