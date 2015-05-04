package com.egysoft.ia.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

public class ShaderLoader extends SynchronousAssetLoader<ShaderProgram, ShaderLoader.ShaderParameter> {

	public ShaderLoader(FileHandleResolver resolver) 
	{
		super(resolver);	
	}

	public class ShaderParameter extends AssetLoaderParameters<ShaderProgram> 
	{
		public String fragmentShader;
		public String vertexShader;
	}

	@Override
	public ShaderProgram load(AssetManager assets, String fileName, FileHandle fileHandle,	ShaderParameter parameters) 
	{
		String shader = fileHandle.pathWithoutExtension();
		FileHandle   fragFile = Gdx.files.internal(String.format("%s.frag",shader));
		FileHandle vertexFile = Gdx.files.internal(String.format("%s.vertex",shader));
		ShaderProgram program = new ShaderProgram(vertexFile, fragFile);
		return program;
	}

	@Override
	@SuppressWarnings("rawtypes")	
	public Array<AssetDescriptor> getDependencies(String arg0, FileHandle arg1,	ShaderParameter arg2) 
	{	
		return null;
	}

}
