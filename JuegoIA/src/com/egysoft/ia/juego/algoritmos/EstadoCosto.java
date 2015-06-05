package com.egysoft.ia.juego.algoritmos;

import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Operacion;

/**
 * Guarda el estado de la busqueda
 * @author Edgardo
 *
 */
public class EstadoCosto extends Estado
{
	public final int costo;
	
	public EstadoCosto(Celda inicial) 
	{
		this(inicial, Operacion.Inicial, null, 0);		
	}
	
	public EstadoCosto(Celda actual, Operacion operacion, Estado predesor, int costo)
	{
		super(actual, operacion, predesor);
		this.costo = costo;
	}
	
	@Override
	public String toString()
	{
		return String.format("{(%d, %d), %s, %d}", celda.i, celda.j, operacion.name(), costo);		
	}
}
