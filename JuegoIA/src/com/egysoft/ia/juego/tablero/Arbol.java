package com.egysoft.ia.juego.tablero;

public class Arbol 
{
	private Rama raiz;
	public Arbol(ITablero tablero, int i, int j)
	{
		raiz = new Rama(tablero.getCelda(i, j));
	}
}
