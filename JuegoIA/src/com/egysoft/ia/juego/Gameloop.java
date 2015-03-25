/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author Alumno
 */
public class Gameloop implements Screen
{
    private final JuegoIA juego;
    private Stage game;
    private Stage hud;
    
    public Gameloop(JuegoIA j)
    {
        juego = j;
        
        juego.assets.load("assets/main.atlas", TextureAtlas.class);
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
