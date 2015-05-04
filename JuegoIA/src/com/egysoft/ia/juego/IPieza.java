package com.egysoft.ia.juego;

/**
 * Interface que expone las funciones necesarias para comportarse como una pieza.
 *
 * @author Edgardo
 */
public interface IPieza
{
	/**
	 * Devuelve la celda actual en que se encuentra esta pieza.
	 * @return Celda actual.
	 */
    public Celda getCeldaActual();
    /**
     * Establece la nueva celda actual en donde se encuentra esta pieza.
     * @param nueva La nueva celda en donde se encontrará esta pieza.
     */
    public void setCeldaActual(Celda nueva);
}
