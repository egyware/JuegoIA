/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
    private final Stage game;
    private final Stage hud;
    private final InputMultiplexer multiplexor;
    
    public Gameloop(JuegoIA j)
    {
        juego = j;
        
        juego.assets.load("assets/game.atlas", TextureAtlas.class);
        
        juego.assets.finishLoading();
        
        game = new Stage();
        Player player;
        player = new Player("mage", (TextureAtlas)juego.assets.get("assets/game.atlas"));
        player.setPosition(150,100);
        game.addActor(player);
        game.setKeyboardFocus(player);
        
        hud = new Stage();
        
        multiplexor = new InputMultiplexer(hud, game);                
    }

    @Override
    public void show() 
    {    
        Gdx.input.setInputProcessor(multiplexor);
    }

    @Override
    public void render(float delta) 
    {    
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
        
        game.act();
        game.draw();
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() 
    {    
    }
    
}
