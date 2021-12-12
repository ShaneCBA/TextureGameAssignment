package com.github.shaneCBA.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drawing.GTextureUtil;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public enum Tile {
	AIR(null, false, Tile.BREATHABLE),
	DIRT("/world/dirt.png", true),
	PLATFORM("/world/platform.png", true, Tile.HALFBLOCK),
	WATER("/world/water.png", true, -1),//-1 for none of the above (for now)
	SIGN("/world/sign.png", true, Tile.HALFBLOCK);

	public static final int TILESIZE = 24;
	
	public static final int BREATHABLE = 0;
	public static final int SOLID = 1;
	public static final int HALFBLOCK = 2;
	
	private String textureFile;
	private Texture texture;
	
	private boolean drawable;
	private int type;
	
	Tile(String textureFile, boolean drawable, int type)
	{
		this.textureFile = textureFile;
		this.drawable = drawable;
		this.type = type;
		
		if (drawable)
			generateTexture();
	}

	Tile(String textureFile, boolean drawable)
	{
		this(textureFile, drawable, SOLID);
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
		String fileType = m.group();
		texture = GTextureUtil.loadTextureProjectDir(textureFile, fileType);
	}
	boolean getDrawable()
	{
		return drawable;
	}
	void loadTexture(GL2 gl)
	{
		if (drawable)
		{
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
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
	
	int getType()
	{
		return type;
	}
	
}
