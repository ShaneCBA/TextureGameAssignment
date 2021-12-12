package com.github.shaneCBA.game;

public class TileUtil {
	private TileUtil()
	{
	}
	
	static float getTileBottom(float y)
	{
		int tileY = (int) (y/Tile.TILESIZE);
		return (float) tileY*Tile.TILESIZE;
	}
	
	static float getTileTop(float y)
	{
		return getTileBottom(y) + Tile.TILESIZE;
	}
	
	static float getTileLeft(float x)
	{
		int tileX = (int) (x/Tile.TILESIZE);
		return (float) tileX*Tile.TILESIZE;
	}
	
	static float getTileRight(float x)
	{
		return getTileLeft(x) + Tile.TILESIZE;
	}
}
