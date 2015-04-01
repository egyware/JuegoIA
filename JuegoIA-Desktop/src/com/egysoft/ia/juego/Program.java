package com.egysoft.ia.juego;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 *
 * @author Alumno
 */
public class Program 
{
    public static void main(String ...args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title  = "JuegoIA";
        config.width  = JuegoIA.Width;
        config.height = JuegoIA.Height;
        
        new LwjglApplication(new JuegoIA(), config);
    }
    
}
