package com.egysoft.ia.juego.algoritmos;

import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Operacion;

/**
 * Guarda el estado de la busqueda
 * @author Edgardo
 *
 */
public class Estado
{
	public final Celda celda;
	public Operacion operacion;
	public Estado predesor;
	
	public Estado(Celda inicial) 
	{
		celda = inicial;
		operacion = Operacion.Inicial;
		predesor = null;				
	}
	
	public Estado(Celda actual, Operacion operacion, Estado predesor)
	{
		this.celda    = actual;
		this.operacion = operacion;
		this.predesor  = predesor;
	}
	
	@Override
	public String toString()
	{
		return String.format("{(%d, %d), %s}", celda.i, celda.j, operacion.name());		
	}
}
