package com.github.shaneCBA.game;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drawing.GShape;
import com.drawing.GTextureUtil;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public enum Tile {
	AIR(null, false, 0f),
	SOLID("/world/dirt.png", true, 0.5f),
	WATER("/world/water.png", true, 2f);
	
	private String textureFile;
	private boolean drawable;
	private Texture texture;
	private float friction;
	
	Tile(String textureFile, boolean drawable, float friction)
	{
		this.textureFile = textureFile;
		this.drawable = drawable;
		this.setFriction(friction);
		
		if (drawable)
			generateTexture();
//		if (textureFile != null)
//		{
//			//REGEX to match the filetype at the end of the file name
//			String regexFileType = ".*\\.([a-zA-Z]+)$";
//			Pattern p = Pattern.compile(regexFileType);
//			Matcher m = p.matcher(textureFile);
//			if (!m.find())
//				throw new IllegalArgumentException(
//						String.format("Tile - Could not find File Extension"
//								+ "in string \"%s\"", textureFile));
//			//Retrieve the file extension of the textureFile and format it to uppercase;
//			String pattern = m.group();
//			texture = GTextureUtil.loadTextureProjectDir(textureFile, pattern);
//		}
	}
	void generateTexture()
	{
		String regexFileType = ".*\\.([a-zA-Z]+)$";
		Pattern p = Pattern.compile(regexFileType);
		Matcher m = p.matcher(textureFile);
		if (!m.find())
			throw new IllegalArgumentException(
					String.format("Tile - Could not find File Extension"
							+ "in string \"%s\"", textureFile));
		//Retrieve the file extension of the textureFile and format it to uppercase;
		String type = m.group();
		texture = GTextureUtil.loadTextureProjectDir(textureFile, type);
	}
	boolean getDrawable()
	{
		return drawable;
	}
	void loadTexture(GL2 gl)
	{
		if (drawable)
		{
			texture.enable(gl);
			texture.bind(gl);
		}
	}
	void unloadTexture(GL2 gl)
	{
		if (drawable)
			texture.disable(gl);
	}
	Texture getTexture()
	{
		return texture;
	}
	public float getFriction() {
		return friction;
	}
	public void setFriction(float friction) {
		this.friction = friction;
	}
}
