package com.egysoft.ia.juego;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.egysoft.ia.juego.actores.Ficha;
import com.egysoft.ia.juego.actores.IAPlayerA;
import com.egysoft.ia.juego.actores.IAPlayerB;
import com.egysoft.ia.juego.actores.HumanPlayer;
import com.egysoft.ia.juego.tablero.Player;
import com.egysoft.ia.juego.tablero.Tablero;

/**
 *
 * @author Edgardo Moreno
 */
public class Gameloop implements Screen
{
	public static enum Modo
	{
		A,
		B
	}
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
    private Player player;
    private TextureAtlas atlas; 
    private Modo modo;
    
    public Gameloop(JuegoIA juego, Modo modo)
    {
        this.juego = juego;
        this.modo = modo;
        
        final String map;
        if(modo == Modo.B)
        	map	= "assets/maps/map_certamen.txt";
        else
        	map	= "assets/maps/map_01.txt";
        
        juego.assets.load("assets/game.atlas", TextureAtlas.class);
        juego.assets.load("assets/shaders/gray.shader", ShaderProgram.class);
        juego.assets.load("assets/shaders/basic.shader", ShaderProgram.class);
        juego.assets.load("assets/uiskin.json", Skin.class);
        juego.assets.load(map, Tablero.class);
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
        
        atlas = juego.assets.get("assets/game.atlas");
        setGUI();
        
        multiplexor = new InputMultiplexer(hud, game);
        
        //se crea a partir de un archivo cargado previamente en assets
        tablero = juego.assets.get(map);
        tablero.setGameloop(this);
        
        game.addActor(tablero);        
        
        player = new HumanPlayer("jasper", atlas, juego.assets.get("assets/ScreamAndDie.wav"));
        game.setKeyboardFocus(player);
        tablero.addActor(player);
        controller.follow(player);
        
//        player = new IAPlayer("jasper", atlas, juego.assets.get("assets/ScreamAndDie.wav"));
//        game.setKeyboardFocus(player);
//        tablero.addActor(player);
//        controller.follow(player);
        
		if(modo == Modo.B)
		{
			//añadir fichas
	        Random random = new Random(System.currentTimeMillis());
	        //Random random = new Random(100000L);
	        Sound sound = juego.assets.get("assets/cha-ching.wav");
	        Ficha[] fichas = new Ficha[]{
	        		new Ficha("ficha", Ficha.Tipo.Ficha_1, atlas, sound),
	        		new Ficha("ficha", Ficha.Tipo.Ficha_2, atlas, sound),
	                new Ficha("ficha", Ficha.Tipo.Ficha_3, atlas, sound),
	                new Ficha("ficha", Ficha.Tipo.Ficha_A, atlas, sound),
	                new Ficha("ficha", Ficha.Tipo.Ficha_B, atlas, sound),
	                new Ficha("ficha", Ficha.Tipo.Ficha_C, atlas, sound)
	        };
	        //fichas[0].emparejar(fichas[1]);fichas[1].emparejar(fichas[0]);
	        fichas[0].emparejar(fichas[3]);fichas[3].emparejar(fichas[0]);
	        fichas[1].emparejar(fichas[4]);fichas[4].emparejar(fichas[1]);
	        fichas[2].emparejar(fichas[5]);fichas[5].emparejar(fichas[2]);
	        
//	        fichas[0].setPosition(2*32+4,5*32+4);
//	        fichas[1].setPosition(8*32+4,5*32+4);
	        		
	        
	        for(Ficha ficha: fichas)
	        {
	        	int i,j;
	        	do
	        	{
	        		i = 1+random.nextInt(9);
	        		j = 1+random.nextInt(9);
	        	}while(!tablero.Disponible(i, j));
	        	ficha.setPosition(i*32+4, j*32+4);
	        	tablero.addActor(ficha);
	        }
	        tablero.setTotalRecompensas(6);
		}
           
               
        //musica
        if(Config.instance.getVolume() > 0)
        {
        	music.setLooping(true);
        	music.setVolume(Config.instance.getVolume());
        	music.play();
        }
        setDebug(Config.instance.isDebug());
    }

    private Label recompensas;
    private boolean showMenu = false;
    private void setGUI() 
    {
    	final Skin skin = juego.assets.get("assets/uiskin.json");
    	
    	final Table table = new Table();
    	table.setFillParent(true);    	
    	hud.addActor(table);
    	
    	recompensas = new Label("", skin);
    	
    	final Dialog menu = new Dialog("Menu", skin);
		TextButton continuar     = new TextButton("Continuar", skin);  
		TextButton jugadorHumano = new TextButton("Jugador Humano", skin);
		TextButton jugadorIA     = new TextButton("Jugador IA", skin);
		TextButton opciones      = new TextButton("Opciones", skin);
		TextButton salir         = new TextButton("Salir", skin);
		
		continuar.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{				
				showMenu = false;
				setPause(false);
				menu.hide();
			}
        });
		
		jugadorHumano.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{				
				tablero.removeActor(player);
		        
		        player = new HumanPlayer("jasper", atlas, juego.assets.get("assets/ScreamAndDie.wav"));
		        game.setKeyboardFocus(player);
		        tablero.addActor(player);
		        controller.follow(player);
			}
        });
		
		jugadorIA.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor source) 
			{				
				tablero.removeActor(player);
		        
				if(modo == Modo.B)
				{
					player = new IAPlayerB("jasper", atlas, juego.assets.get("assets/ScreamAndDie.wav"));
					game.setKeyboardFocus(player);
					tablero.addActor(player);
					controller.follow(player);		        
				}
				else
				{
					player = new IAPlayerA("jasper", atlas, juego.assets.get("assets/ScreamAndDie.wav"));
					game.setKeyboardFocus(player);
					tablero.addActor(player);
					controller.follow(player);
				}
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
		
		
		menu.row();				
		menu.add(continuar).fillX();
		menu.row();				
		menu.add(jugadorHumano).fillX();
		menu.row();				
		menu.add(jugadorIA).fillX();
		menu.row();				
		menu.add(opciones).fillX();
		menu.row();				
		menu.add(salir).fillX();
        
        hud.addListener(new InputListener()
        {
        	public boolean keyTyped(InputEvent event, char key)
        	{
        		if(key == (char)27)
        		{
        			if(showMenu)
        			{
        				showMenu = false;
        				setPause(false);              			
            			menu.hide();
        			}
        			else
        			{
        				showMenu = true;
        				setPause(true);              			
            			menu.show(hud);
        			}
        			return true;
        		}
        		return false;
        	}
        });
        
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
				TextButton ok     = new TextButton("Aceptar", skin);
				
				
				musicSlider.setValue(Config.instance.getVolume());
				musicBox.setChecked(Config.instance.getVolume()<=0);
				iaSlider.setValue(Config.instance.getEnemyIntelligence());				
				debugBox.setChecked(Config.instance.isDebug());				
				
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
						boolean b = debugBox.isChecked();
						setDebug(b);
						Config.instance.setDebug(b);
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
        table.add(recompensas).left().top();        
        
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
