/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 *
 * @author Alumno
 */
public class Gameloop implements Screen
{
    final JuegoIA game;
    public Gameloop(JuegoIA game)
    {
        this.game = game;
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
