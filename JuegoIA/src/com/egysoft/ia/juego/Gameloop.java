package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.egysoft.ia.juego.actores.Player;
import com.egysoft.ia.juego.tablero.Tablero;

/**
 *
 * @author Edgardo Moreno
 */
public class Gameloop implements Screen
{
	private boolean debug;
    private final JuegoIA juego;
    private final Stage game;
    private final Stage hud;    
    private final InputMultiplexer multiplexor;
    private final Tablero tablero;
    
    private ShaderProgram grayShader;
    private ShaderProgram basicShader;
    
    public Gameloop(JuegoIA j)
    {
        juego = j;
        
        juego.assets.load("assets/game.atlas", TextureAtlas.class);
        juego.assets.load("assets/shaders/gray.shader", ShaderProgram.class);
        juego.assets.load("assets/shaders/basic.shader", ShaderProgram.class);
        juego.assets.load("assets/uiskin.json", Skin.class);
        
        juego.assets.finishLoading();
        
        grayShader  = juego.assets.get("assets/shaders/gray.shader");
        basicShader = juego.assets.get("assets/shaders/basic.shader");
        
        game = new Stage();        
        hud = new Stage();
        game.getBatch().setShader(basicShader);
        
        final Skin skin = juego.assets.get("assets/uiskin.json");
        setGUI(skin);
        
        multiplexor = new InputMultiplexer(hud, game);
        
        tablero = new Tablero(skin.getFont("default-font"),18,18,64,64); //se crea el laberinto y luego todo ahi se añade, aunque lo ideal sea solo en Stage..
        
        game.addActor(tablero);
        
        //jugadores
        Player player;
        player = new Player("mage", (TextureAtlas)juego.assets.get("assets/game.atlas"));
        player.setPosition(150,100);
        game.setKeyboardFocus(player);
        tablero.addActor(player);        
        
        player = new Player("mage", (TextureAtlas)juego.assets.get("assets/game.atlas"));
        player.setPosition(200,100);
        tablero.addActor(player);
    }

    private void setGUI(final Skin skin) 
    {
    	final Table table = new Table();
    	table.setFillParent(true);    	
    	hud.addActor(table);
    	
        final TextButton opciones = new TextButton("opciones",skin);
        opciones.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{				
				setPause(true);
				final Dialog dialog = new Dialog("Opciones", skin);
				CheckBox musicBox = new CheckBox("", skin);
				CheckBox debugBox = new CheckBox("", skin);
				Slider musicSlider = new Slider(0,10,1,false, skin);				
				Slider iaSlider = new Slider(0,10,1, false, skin);
				TextButton ok = new TextButton("Aceptar", skin);
				
				musicSlider.setValue(getMusicVolume());
				musicBox.setChecked(getMusicVolume()<=0);
				iaSlider.setValue(getIntelligence());
				debugBox.setChecked(getDebug());				
				
				musicBox.addListener(new ChangeListener()
				{
					private float lastValue = musicSlider.getValue();
					@Override
					public void changed(ChangeEvent event, Actor source) 
					{
						if(musicBox.isChecked())
						{
							lastValue = musicSlider.getValue();
							musicSlider.setValue(0);
							musicSlider.setDisabled(true);
						}
						else
						{
							musicSlider.setValue(lastValue);
							musicSlider.setDisabled(false);
						}
					}					
				});
				
				debugBox.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent event, Actor source) 
					{					
						setDebug(debugBox.isChecked());
					}					
				});
				
				musicSlider.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent arg0, Actor arg1) 
					{
						setMusicVolume(musicSlider.getValue());
					}					
				});
				iaSlider.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent arg0, Actor arg1) 
					{
						setIntelligence(iaSlider.getValue());						
					}					
				});
				
				ok.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent arg0, Actor arg1) 
					{
						dialog.hide();
						setPause(false);
					}					
				});
				
				dialog.row();
				dialog.add(new Label("Musica",skin)).left();
				dialog.add(musicBox);
				dialog.add(musicSlider).right();
				dialog.row();
				dialog.add(new Label("Inteligencia",skin)).left();
				dialog.add(iaSlider).colspan(2).left().fill();
				dialog.row();
				dialog.add(new Label("Debug",skin)).left();
				dialog.add(debugBox).colspan(2).left();
				dialog.row();
				dialog.add(ok).colspan(3).center();
				
				dialog.show(hud);
			}        	
        });
        
        table.row().expand();
        table.add(opciones).right().top();        
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
        
        if(debug)
        {
        	tablero.drawDebugTablero();
        }
        game.act();
        hud.act();
        game.draw();
        hud.draw();        
    }

    @Override
    public void resize(int w, int h) 
    {
    	game.getViewport().update(w, h);
    	hud.getViewport().update(w, h);    	
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
    	game.dispose();
    	hud.dispose();
    }
    
    
    public boolean getDebug()
    {
    	return debug;
    }
    public void setDebug(boolean b)
    {
    	debug = b;
    	tablero.setGameDebug(b);
    }
    
    public float getMusicVolume()
    {
    	return 10;
    }
    public void setMusicVolume(float value)
    {    	
    }
    
    public float getIntelligence()
    {
    	return 5;
    }
    public void setIntelligence(float f)
    {    	
    }
    
    public void setPause(boolean pause)
    {
    	final Batch batch = game.getBatch();
    	if(pause)
    	{
    		batch.setShader(grayShader);
    	}
    	else
    	{
    		batch.setShader(basicShader);	
    	}
    	tablero.setPausa(pause);
    	
    }
}
