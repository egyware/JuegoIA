package com.egysoft.ia.juego.tablero;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Esta clase es la base de todas las piezas del tabler,
 * como si fuese un juego de ajedres o cualquier otro juego con tableros.
 * Sirve para almacenar la referencia en donde se encuentra la celda.
 * 
 * @author Edgardo
 *
 */
public abstract class Pieza extends Actor implements IPieza
{
	private Celda celda; //celda actual
	
	@Override
	public final Celda getCeldaActual() 
	{
		return celda;
	}

	@Override
	public final void setCeldaActual(Celda nueva) 
	{
		celda = nueva;
	}

}
