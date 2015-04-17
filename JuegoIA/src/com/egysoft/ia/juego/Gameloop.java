package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 *
 * @author Edgardo Moreno
 */
public class Gameloop implements Screen
{
    private final JuegoIA game;
    private final Laberinth laberinth;
    public Gameloop(JuegoIA game)
    {
        this.game = game;
        this.laberinth = new Laberinth(18,18,64,64);
    }

    @Override
    public void show() 
    {    
    }

    @Override
    public void render(float delta) 
    {    
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
    }

    @Override
    public void resize(int w, int h) 
    {    
    }

    @Override
    public void pause() 
    {    
    }

    @Override
    public void resume() 
    {    
    }

    @Override
    public void hide() 
    {    
    }

    @Override
    public void dispose() 
    {    
    }
    
}
