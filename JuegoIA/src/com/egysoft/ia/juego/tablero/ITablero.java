package com.egysoft.ia.juego.tablero;

/**
 *
 * @author Edgardo
 */
public interface ITablero
{
	/**
	 * Devuelve la celda en la posici�n indicada
	 * @param i columna en donde est� la celda
	 * @param j fila en donde est� la celda
	 * @return La celda en la posicion (i,j). Si las coordenadas est�n fuera de los limites devolver� null.
	 */
    Celda getCelda(int i,int j);
    Celda getCelda(float x, float y);
    /**
     * Devuelve si la celda en la posici�n indicada, esta disponible.
     * @param i
     * @param j
     * @return
     */
    boolean Disponible(int i, int j);
	int boxWidth();
	int boxHeight();	
	int rows();
	int columns();
	void gameEnd();
	
    
}
