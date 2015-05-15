package com.egysoft.ia.juego.tablero;

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
}
