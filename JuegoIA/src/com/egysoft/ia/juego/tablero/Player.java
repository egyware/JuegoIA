package com.egysoft.ia.juego.tablero;

/**
 * Clase com�n entre jugador y jugador de la IA
 * @author edgardo.moreno
 *
 */
public abstract class Player extends Pieza
{
	public abstract void push(Pieza by, int k, int m);
}