package com.egysoft.ia.juego.tablero;

public class Rama 
{
	private final Celda actual;
	private Celda[] hojas;
	public Rama(final Celda celda) 
	{
		actual = celda;
	}
	
	public Celda[] obtenerHojas()
	{
		if(hojas == null)
		{
			hojas[0] = actual.Obtener(0,1);  //arriba			
			hojas[1] = actual.Obtener(-1,0); //izquierda
			hojas[2] = actual.Obtener(0,-1); //abajo
			hojas[3] = actual.Obtener(1,0);  //derecha
		}
		return hojas;
	}	
}
