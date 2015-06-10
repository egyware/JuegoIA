package com.egysoft.ia.juego.algoritmos;

import java.util.LinkedList;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.egysoft.ia.juego.actores.Lair;
import com.egysoft.ia.juego.actores.Wall;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Operacion;

/**
 * Algoritmo por anchura
 * @author Edgardo
 *
 */
public class BusquedaPorAnchura
{
	private static final Operacion[] operaciones = {Operacion.Arriba, Operacion.Izquierda, Operacion.Abajo,Operacion.Derecha};

	public BusquedaPorAnchura()
	{		
	}
	
	final ObjectMap<Celda, Estado> agenda = new ObjectMap<Celda, Estado>(); //o historial
	public Array<Estado> buscar(Celda inicial)
	{
		agenda.clear();
		final LinkedList<Estado> cola = new LinkedList<Estado>(); // cola

		//he revisado Array.java y add corresponde a push ya que lo añade al final del arreglo
		//https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/utils/Array.java
		//15-05-2015 Me acabo de dar cuenta que utilizaba una pila y no una cola. Bueno estoy utilizando LinkedList de java.utils
		cola.push(new Estado(inicial));
		
		while(cola.size() > 0)
		{
			Estado actual = cola.poll();
			agenda.put(actual.celda, actual); //lo guardo en nuestra agenda o historial
			
			//este es nuestro estado final
			if(actual.celda.getPiezaActual() instanceof Lair)
			{
				//devuelvo lo primero que pille
				final Array<Estado> resultado = new Array<Estado>();
				Estado aux = actual;
				while(aux != null)
				{
					resultado.add(aux);
					aux = aux.predesor;
				}				
				return resultado;
			}
			
			for(Operacion operacion: operaciones) //por cada operacion reviso si existen celdas adyacentes
			{
				Celda proxima = actual.celda.Obtener(operacion.k, operacion.m);
				if(proxima != null && !(proxima.getPiezaActual() instanceof Wall))
				{
					if(!agenda.containsKey(proxima))
					{
						cola.push(new Estado(proxima, operacion, actual));
					}
				}
			}			
		}
				
		return null;
	}
	
}
