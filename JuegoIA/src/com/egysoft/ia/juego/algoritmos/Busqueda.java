package com.egysoft.ia.juego.algoritmos;

import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.tablero.Celda;

public interface Busqueda
{
	public Array<Estado> buscar(Celda origen, Celda destino);
}
