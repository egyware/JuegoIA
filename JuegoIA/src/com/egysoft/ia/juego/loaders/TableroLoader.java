package com.egysoft.ia.juego.loaders;

import java.util.Scanner;

import com.egysoft.ia.juego.actores.Coin;
import com.egysoft.ia.juego.actores.Cube;
import com.egysoft.ia.juego.actores.Enemy;
import com.egysoft.ia.juego.actores.Lair;
import com.egysoft.ia.juego.actores.Wall;
import com.egysoft.ia.juego.tablero.Tablero;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class TableroLoader extends SynchronousAssetLoader<Tablero, TableroLoader.TableroParameter> {

	public TableroLoader(FileHandleResolver resolver) 
	{
		super(resolver);	
	}

	public class TableroParameter extends AssetLoaderParameters<Tablero> 
	{		
	}

	@Override
	public Tablero load(AssetManager assets, String fileName, FileHandle fileHandle,TableroParameter parameters) 
	{
		final Tablero tablero;
		final TextureAtlas atlas = assets.get("assets/game.atlas");
		final Sound cha_ching = assets.get("assets/cha-ching.wav"); 
		final Scanner scan = new Scanner(fileHandle.read());		
		int columns = scan.nextInt();
		int rows = scan.nextInt();
		int recompensas = 0;
		scan.nextLine();
		
		tablero = new Tablero(assets, columns, rows, Tablero.ColumnWidth,Tablero.ColumnHeight);
		
		for(int j=rows-1;j>=0;j--)
		{
			final String line = scan.nextLine();
			final int len = line.length();
			for(int i=0;i < columns && i < len;i++)				
			{
				char c = line.charAt(i);
				switch(c)
				{
					case  '#':
					{
						Wall wall = new Wall("violet_wall", atlas);
						wall.setPosition(32*i, 32*j);
						tablero.addActor(wall);						
					}
					break;
					case 'x':
					{
						Enemy e = new Enemy("rafese", atlas);
				        e.setPosition(32*i, 32*j);
				        tablero.addActor(e);	
					}					
					break;
					case 'o':
					{
						Cube cube = new Cube("cube", atlas, cha_ching);
						cube.setPosition(32*(i+0.25f), 32*(j+0.25f));
						tablero.addActor(cube);
						recompensas += 1;
					}
					break;
					case 'c':
					{
						Coin coin = new Coin("cube", atlas, cha_ching);
						coin.setPosition(32*(i+0.25f), 32*(j+0.25f));
						tablero.addActor(coin);
						recompensas += 1;
					}
					break;
					case 'l':
					{
						Lair lair = new Lair("blue_lair", atlas);
						lair.setPosition(32*i, 32*j);
						tablero.addActor(lair);
					}
					break;
					case 'i':
					{
						tablero.setInitialPosition(32*i,32*j);
					}
					break;
				}
			}
		}
		
		scan.close();
		tablero.setTotalRecompensas(recompensas);
		
		return tablero;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Array<AssetDescriptor> getDependencies(String arg0, FileHandle arg1,	TableroParameter arg2) 
	{
		Array<AssetDescriptor> array = new Array<AssetDescriptor>();
		array.add(new AssetDescriptor("assets/uiskin.json",Skin.class));
		array.add(new AssetDescriptor("assets/game.atlas", TextureAtlas.class));
		array.add(new AssetDescriptor("assets/cha-ching.wav", Sound.class));
		return array;
	}

}
