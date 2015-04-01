package com.egysoft.ia.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

/**
 *
 * @author Edgardo
 */
public class JuegoIA extends Game
{
    public static int Width = 800;
    public static int Height = 480;
    public AssetManager assets;
    
    
    @Override
    public void create() 
    {
        assets = new AssetManager();
        
        
        setScreen(new Gameloop(this));
    }    
}
