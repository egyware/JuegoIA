package com.egysoft.ia.juego.algoritmos;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.egysoft.ia.juego.actores.Lair;
import com.egysoft.ia.juego.actores.Wall;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Estado;
import com.egysoft.ia.juego.tablero.Operacion;

/**
 * Algoritmo por anchura
 * @author Edgardo
 *
 */
public class Algoritmo1
{
	private static final Operacion[] operaciones = {Operacion.Arriba, Operacion.Izquierda, Operacion.Abajo,Operacion.Derecha};
	private static Comparator<Array<Estado>> comparador = new Comparator<Array<Estado>>()
	{
		@Override
		public int compare(Array<Estado> a, Array<Estado> b) 
		{
			return a.size - b.size;
		}		
	};
	
	public Algoritmo1()
	{		
	}
	
	public Array<Estado> buscar(Celda inicial)
	{
		final ObjectMap<Celda, Estado> agenda = new ObjectMap<Celda, Estado>(); //o historial
		final Array<Estado> cola = new Array<Estado>(); // cola
		final Array<Array<Estado>> resultados = new Array<Array<Estado>>();//todos los resultados
		
		//he revisado Array.java y add corresponde a push ya que lo añade al final del arreglo
		//https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/utils/Array.java
		cola.add(new Estado(inicial));
		
		while(cola.size > 0)
		{
			Estado actual = cola.pop();
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
				//return resultado;
				resultados.add(resultado);
				continue;
			}
			
			for(Operacion operacion: operaciones) //por cada operacion reviso si existen celdas adyacentes
			{
				Celda proxima = actual.celda.Obtener(operacion.k, operacion.m);
				if(proxima != null && !(proxima.getPiezaActual() instanceof Wall))
				{
					if(!agenda.containsKey(proxima))
					{
						cola.add(new Estado(proxima, operacion, actual));
					}
				}
			}			
		}	
		
		if(resultados.size > 0)
		{
			resultados.sort(comparador);
			for(Array<Estado> a:resultados)
			{
				System.out.println(a.size);
			}			
			return resultados.first();
		}		
		return null;
	}
	
}
