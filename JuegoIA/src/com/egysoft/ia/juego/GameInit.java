package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameInit implements Screen 
{
	private Stage hud;
	
	public GameInit(JuegoIA juego) 
	{
		juego.assets.load("assets/uiskin.json", Skin.class);
		juego.assets.finishLoading();
		
		hud = new Stage();
		Gdx.input.setInputProcessor(hud);
		
		final Table table = new Table();
    	table.setFillParent(true);    	
    	hud.addActor(table);
    	final Skin skin = juego.assets.get("assets/uiskin.json");
    	
    	final TextButton iniciar = new TextButton("Iniciar",skin);
    	final TextButton salir = new TextButton("Salir",skin);
    	
        iniciar.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{
				juego.showGameloop();
			}
        });
                
        salir.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{
				Gdx.app.exit();
			}
        });
        
        table.row();
        table.add(iniciar).fillX();
        table.row();
        table.add(salir).fillX();
	}

	@Override
	public void dispose() 
	{
		hud.dispose();
	}

	@Override
	public void hide()
	{	
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() 
	{	

	}

	@Override
	public void render(float dt) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
	        
		hud.act(dt);
		hud.draw();

	}

	@Override
	public void resize(int w, int h) 
	{
		hud.getViewport().update(w, h);
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void show() 
	{
		Gdx.input.setInputProcessor(hud);
	}

}
