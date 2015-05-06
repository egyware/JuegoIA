package com.egysoft.ia.juego;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CameraController 
{
	private Actor actor;
	private Camera camera;
	
	public CameraController(Camera c)
	{
		camera = c;
	}
	
	public void update(float dt)
	{
		if(actor != null)
		{
			camera.position.x = actor.getX();
			camera.position.y = actor.getY();			
		}
	}
	
	public void follow(Actor a)
	{
		actor = a;
	}
}
