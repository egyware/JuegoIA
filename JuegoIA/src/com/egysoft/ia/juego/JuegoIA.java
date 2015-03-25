package com.egysoft.ia.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

/**
 *
 * @author Edgardo
 */
public class JuegoIA extends Game
{
    public AssetManager assets;
    
    
    @Override
    public void create() 
    {
        assets = new AssetManager();
        
        
        setScreen(new Gameloop(this));
    }    
}
