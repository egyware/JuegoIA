package com.egysoft.ia.juego.algoritmos;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.egysoft.ia.juego.actores.Wall;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.Operacion;

public class BusquedaAStar implements Busqueda
{
	private static final Operacion[] operaciones = {Operacion.Arriba, Operacion.Izquierda, Operacion.Abajo,Operacion.Derecha};
	
	public static interface H
	{
		public int h(Celda actual);
	}
	public static interface G
	{
		public int g(int costo, Celda actual);
	}
	public static interface C extends Comparator<EstadoCosto>
	{		
	}

	private final G g;
	private final H h;
	private final C c;
	public BusquedaAStar(G g, H h, C c)
	{
		this.g = g;
		this.h = h;
		this.c = c;
		queue = new PriorityQueue<EstadoCosto>(c);
	}
	
	public BusquedaAStar(G g, H h)
	{
		this.g = g;
		this.h = h;
		this.c = new C()
		{
			@Override
			public int compare(EstadoCosto a, EstadoCosto b) 
			{
				return a.costo - b.costo;
			}			
		};
		queue = new PriorityQueue<EstadoCosto>(c);
	}
		
	final PriorityQueue<EstadoCosto> queue;
	final ObjectMap<Celda, Estado> agenda = new ObjectMap<Celda, Estado>(); //o historial	
	public Array<Estado> buscar(Celda inicial, Celda destino)
	{
		queue.clear();
		agenda.clear();
		queue.add(new EstadoCosto(inicial));
		
		while(queue.size() > 0)
		{
			//System.out.println(String.format("Cola: %s", queue.toString()));
			final EstadoCosto actual = queue.poll();
			agenda.put(actual.celda, actual); //lista cerrados			
			
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
			//System.out.println(String.format("Visitando: %s",actual.toString()));
			for(Operacion operacion:operaciones)
			{
				Celda proxima = actual.celda.Obtener(operacion.k, operacion.m);
				if(agenda.containsKey(proxima)) continue;
				if(proxima != null && !(proxima.getPiezaActual() instanceof Wall))
				{
					EstadoCosto ec = new EstadoCosto(proxima, operacion, actual, g(actual.costo, proxima)+h(proxima));
					//System.out.printf("Añadiendo: %15s = f(%d) + h(%d)\n", ec, g(actual.costo, proxima),h(proxima));
					queue.add(ec);
				}
			}
		}		
		
		return null;
	}
	
	public int g(int costo, Celda actual)
	{
		return g.g(costo, actual);
	}
	public int h(Celda actual)
	{
		return h.h(actual);
	}
}
