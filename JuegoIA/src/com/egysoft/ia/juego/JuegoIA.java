package com.egysoft.ia.juego;

import com.badlogic.gdx.Game;

/**
 *
 * @author Alumno
 */
public class JuegoIA extends Game
{
    @Override
    public void create() 
    {
        setScreen(new Gameloop(this));
    }    
}
