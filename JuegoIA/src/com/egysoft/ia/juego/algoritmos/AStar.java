package com.egysoft.ia.juego.algoritmos;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.actores.Coin;
import com.egysoft.ia.juego.actores.Lair;
import com.egysoft.ia.juego.actores.Wall;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.IPieza;
import com.egysoft.ia.juego.tablero.Operacion;

public class AStar
{
	private static final Operacion[] operaciones = {Operacion.Arriba, Operacion.Izquierda, Operacion.Abajo,Operacion.Derecha};
	
	public static class MaximarEstado implements Comparator<EstadoCosto>
	{
		@Override
		public int compare(EstadoCosto a, EstadoCosto b) 
		{			
			return b.costo - a.costo;
		}		
	}
	
	public int h(Celda actual, Celda destino)
	{
		IPieza pieza = actual.getPiezaActual();
		if(pieza != null && pieza instanceof Coin)
		{
			return 5;
		}
		if(pieza != null && pieza instanceof Lair)
		{
			return 1;
		}
		return 1;
	}
	
	public int f(int f, Celda actual)
	{
		return f-1;
	}
		
	public Array<Estado> buscar(Celda inicial, Celda destino)
	{
		final PriorityQueue<EstadoCosto> queue = new PriorityQueue<EstadoCosto>(new MaximarEstado());
		queue.add(new EstadoCosto(inicial));
		
		while(queue.size() > 0)
		{
			System.out.println(queue);
			final EstadoCosto actual = queue.poll();
			
			if(actual.celda == destino)
			{
				final Array<Estado> resultado = new Array<Estado>();
				Estado aux = actual;
				while(aux != null)
				{
					resultado.add(aux);
					aux = aux.predesor;
				}				
				return resultado;	
			}
			System.out.println(actual);
			for(Operacion operacion:operaciones)
			{
				Celda proxima = actual.celda.Obtener(operacion.k, operacion.m);
				if(proxima != null && !(proxima.getPiezaActual() instanceof Wall))
				{
					EstadoCosto ec = new EstadoCosto(proxima, operacion, actual, f(actual.costo, proxima)+h(proxima, destino));
					System.out.printf("%s = f(%d) + h(%d)\n", ec, f(actual.costo, proxima), h(proxima, destino));
					queue.add(ec);
				}
			}
		}		
		
		return null;
	}
}
