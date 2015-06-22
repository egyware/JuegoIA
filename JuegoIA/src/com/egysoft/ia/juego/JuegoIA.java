package com.egysoft.ia.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.egysoft.ia.juego.loaders.ShaderLoader;
import com.egysoft.ia.juego.loaders.TableroLoader;
import com.egysoft.ia.juego.tablero.Tablero;


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
        assets.setLoader(Tablero.class, new TableroLoader(fileResolver));
        new Config(assets);
        showGameInit();
    }    
    
    @Override
    public void dispose()
    {
    	assets.dispose();
    	assets = null;
    	super.dispose();
    }

	public void showGameInit()
	{
		Screen screen = getScreen();
		if(screen != null)screen.dispose();
		assets.clear();
		setScreen(new GameInit(this));		
	}

	public void showGameloop(Gameloop.Modo modo) 
	{
		Screen screen = getScreen();
		if(screen != null)screen.dispose();
		assets.clear();
		setScreen(new Gameloop(this, modo));		
	}	
}
