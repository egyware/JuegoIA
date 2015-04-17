package com.egysoft.ia.juego;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Edgardo Moreno
 * @param <T>
 */
public class GridArray<T> extends Array<T>
{
    private final int columns;
    public GridArray(int c, int r, Class<T> classType)
    {
        super(true, c*r,classType);
        columns = c;
    }
    
    public T getGridElement(int i, int j)
    {
        return get(j*columns+i);
    }
}
