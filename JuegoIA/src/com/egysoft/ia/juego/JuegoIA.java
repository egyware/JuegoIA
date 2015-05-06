package com.egysoft.ia.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

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
        final InternalFileHandleResolver fileResolver = new InternalFileHandleResolver();
        assets.setLoader(ShaderProgram.class, new ShaderLoader(fileResolver));
        
        setScreen(new Gameloop(this));
    }    
    
    @Override
    public void dispose()
    {
    	assets.dispose();
    	assets = null;
    	super.dispose();
    }
}
