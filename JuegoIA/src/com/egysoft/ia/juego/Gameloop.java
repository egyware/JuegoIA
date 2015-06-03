package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
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
import com.egysoft.ia.juego.actores.Cube;
import com.egysoft.ia.juego.actores.Player;
import com.egysoft.ia.juego.actores.Wall;
import com.egysoft.ia.juego.tablero.Celda;
import com.egysoft.ia.juego.tablero.IPieza;
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
    private final CameraController controller;
    
    private ShaderProgram grayShader;
    private ShaderProgram basicShader;
    private Music music;
    
    public Gameloop(JuegoIA j)
    {
        juego = j;
        
        juego.assets.load("assets/game.atlas", TextureAtlas.class);
        juego.assets.load("assets/shaders/gray.shader", ShaderProgram.class);
        juego.assets.load("assets/shaders/basic.shader", ShaderProgram.class);
        juego.assets.load("assets/uiskin.json", Skin.class);
        juego.assets.load("assets/maps/map_01.txt", Tablero.class);
        juego.assets.load("assets/music.mp3", Music.class);
        juego.assets.load("assets/ScreamAndDie.wav", Sound.class);
        juego.assets.load("assets/cha-ching.wav", Sound.class);
        
        juego.assets.finishLoading();
        
        grayShader  = juego.assets.get("assets/shaders/gray.shader");
        basicShader = juego.assets.get("assets/shaders/basic.shader");
        
        music = juego.assets.get("assets/music.mp3");
        
        game = new Stage();        
        hud = new Stage();
        controller = new CameraController(game.getCamera());
        game.getBatch().setShader(basicShader);
        
        final TextureAtlas atlas = juego.assets.get("assets/game.atlas");
        setGUI();
        
        multiplexor = new InputMultiplexer(hud, game, new InputProcessor()
        {
			@Override
			public boolean keyDown(int arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyUp(int arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean mouseMoved(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			private Vector3 tmp = new Vector3();
			@Override
			public boolean touchDown(int x, int y, int arg2, int arg3)
			{
				tmp.set(x, y, 0);
				game.getCamera().unproject(tmp);
				final int w = tablero.boxWidth, h = tablero.boxHeight;
				final int i = (int) (tmp.x/w), j = (int) (tmp.y/h);				
				Celda celda = tablero.getCelda(i, j);
				if(celda != null)
				{
					IPieza pieza = celda.getPiezaActual();
					if(pieza != null && !(pieza instanceof Player) )
					{
						tablero.removeActor((Actor)pieza);
					}
					else
					{
						Wall wall = new Wall("sand_wall", atlas);
						wall.setPosition(i*w, j*h);
						tablero.addActor(wall);
					}
					return true;
				}
				return false;
			}

			@Override
			public boolean touchDragged(int arg0, int arg1, int arg2) 
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int arg0, int arg1, int arg2, int arg3) 
			{
				// TODO Auto-generated method stub
				return false;
			}
        
        });
        
        //se crea a partir de un archivo cargado previamente en assets
        tablero = juego.assets.get("assets/maps/map_01.txt");
        tablero.setGameloop(this);
        
        game.addActor(tablero);        
        
        Player player;
        player = new Player("jasper", atlas, juego.assets.get("assets/ScreamAndDie.wav"));
        game.setKeyboardFocus(player);
        tablero.addActor(player);
        controller.follow(player);
        
        if(Config.instance.getVolume() > 0)
        {
        	music.setLooping(true);
        	music.setVolume(Config.instance.getVolume());
        	music.play();
        }
    }

    private Label recompensas;
    private void setGUI() 
    {
    	final Skin skin = juego.assets.get("assets/uiskin.json");
    	
    	final Table table = new Table();
    	table.setFillParent(true);    	
    	hud.addActor(table);
    	
    	recompensas = new Label("", skin);
        final TextButton opciones = new TextButton("Opciones",skin);
        final TextButton salir = new TextButton("Salir",skin);
        
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
				
				musicSlider.setValue(Config.instance.getVolume());
				musicBox.setChecked(Config.instance.getVolume()<=0);
				iaSlider.setValue(Config.instance.getEnemyIntelligence());
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
						Config.instance.setVolume(musicSlider.getValue()*0.1f);
					}					
				});
				iaSlider.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent arg0, Actor arg1) 
					{
						Config.instance.setEnemyIntelligence((int) iaSlider.getValue());						
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
        
        salir.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{
				juego.showGameInit();
			}
		});
        
        table.row().expand();
        table.add(recompensas).left().top();
        table.add(opciones).right().top();        
        table.row().expand();
        table.add(salir).right().colspan(2).bottom();
        
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
        
        
        int r = tablero.getTotalRecompensas()-tablero.getRecompensas();
        recompensas.setText(String.format("Recompensas restantes: %d", r));
        controller.update(delta);
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
    	if(music.isPlaying()) music.stop();
    }

    @Override
    public void resume() 
    {    
    }

    @Override
    public void hide() 
    {    
        Gdx.input.setInputProcessor(null);
        if(music.isPlaying()) music.stop();
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
    
    public void gameEnd(String texto)
    {
    	final Skin skin = juego.assets.get("assets/uiskin.json");
    	final Dialog dialog = new Dialog(texto, skin);
    	TextButton ok = new TextButton("Aceptar", skin);
    	dialog.row();
		dialog.add(ok).colspan(3).center();
    	ok.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) 
			{
				dialog.hide();
				juego.showGameInit();
			}					
		});
    	dialog.show(hud);
    }
}
