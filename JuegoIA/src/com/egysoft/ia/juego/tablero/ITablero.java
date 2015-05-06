package com.egysoft.ia.juego.tablero;

/**
 *
 * @author Edgardo
 */
public interface ITablero
{
	/**
	 * Devuelve la celda en la posición indicada
	 * @param i columna en donde está la celda
	 * @param j fila en donde está la celda
	 * @return La celda en la posicion (i,j). Si las coordenadas están fuera de los limites devolverá null.
	 */
    public Celda getCelda(int i,int j);
    /**
     * Devuelve si la celda en la posición indicada, esta disponible.
     * @param i
     * @param j
     * @return
     */
    public boolean Disponible(int i, int j);
    
}
