package com.egysoft.ia.juego.tablero;

/**
 * Guarda las operaciones para mover una Pieza
 * @author Edgardo
 *
 */
public enum Operacion 
{
	Inicial(0,0),
	Arriba(0,1),
	Derecha(1,0),
	Abajo(0,-1),	
	Izquierda(-1,0);
	
	public final int k;
	public final int m;
	
	private Operacion(int k,int m)
	{
		this.k = k;
		this.m = m;
	}	
}
