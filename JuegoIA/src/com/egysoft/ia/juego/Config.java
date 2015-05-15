package com.egysoft.ia.juego;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.egysoft.ia.juego.actores.Enemy;

public class Config 
{
	public static Config instance;
	private float volume = 1;
	private int enemyIntelligence = 0;
	private AssetManager assets; 
	
	public Config(AssetManager assets)
	{		
		if(instance ==  null)
		{
			instance = this;
		}
		this.assets = assets;
	}
	public void setVolume(float v)
	{
		volume = v;
		Array<Music> array = new Array<Music>();
		assets.getAll(Music.class, array);
		for(Music m: array)
		{
			if(volume >0)
	    	{
	    		m.setVolume(volume);
	    		if(!m.isPlaying()) m.play();
	    	}
	    	else
	    	{
	    		if(m.isPlaying()) m.stop();	    		
	    	}			
		}
	}	
	public float getVolume() 
	{
		return volume;
	}
	public int getEnemyIntelligence()
	{
		return enemyIntelligence;
	}
	public void setEnemyIntelligence(int i) 
	{
		this.enemyIntelligence = i;
		Enemy.velocity = 50 + 10*i;    	
	}
	
	

}
