package com.github.shanecba.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import java.util.ArrayList;

import com.drawing.GShape;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Level implements GShape {
	
	public static final float GRAVITY = 0.5f;
	
	private Tile[][] tiles;
	private int width;
	private int height; //measured in tiles
	private float spawnX;
	private float spawnY;
	
	private ArrayList<Sprite> entities;

	public Level(Tile[][] tiles, int width, int height, float spawnX, float spawnY)
	{
		this.tiles = tiles;
		this.width = width;
		this.height = height;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		
		entities = new ArrayList<>();
	}
	
	public Level(int[][] tiles, int width, int height, float spawnX, float spawnY)
	{
		this.width = width;
		this.height = height;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		this.tiles = new Tile[height][width];
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				this.tiles[y][x] = Tile.values()[tiles[y][x]];
			}
		}
		entities = new ArrayList<>();
	}
	public void addEntity(Sprite entity)
	{		
		entities.add(entity);
		entity.setCurrAnimation(entity.IDLE);
		entity.setLevelInstance(this);
	}
	public void removeEntity(Sprite entity)
	{		
		entities.remove(entity);
	}
	
	private void drawTile(GL2 gl, int x, int y)
	{
		Tile tile = tiles[y][x];


		if (tile.getDrawable())
		{
//			if (tile == Tile.SOLID)
//				DebugUtil.debugSquareIgnoreMatrix(gl, x*Tile.TILESIZE, y*Tile.TILESIZE);
			Texture texture = tile.getTexture();
			gl.glPushMatrix();
			gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
			
			float [] vertex2f = {1f, 1f, 1f};
			gl.glColor3fv(vertex2f, 0); // TEMP - set bg to white
			
			gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			
			gl.glTranslatef((float) x * Tile.TILESIZE, (float) y * Tile.TILESIZE, -1);
			
			
			gl.glEnable(GL.GL_TEXTURE_2D);
			gl.glEnable(GL.GL_BLEND);
	
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	
//			tile.loadTexture(gl);
			texture.bind(gl);
			texture.enable(gl);
	
			gl.glBegin(GL_TRIANGLE_STRIP);
	
			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(Tile.TILESIZE, 0f); // v0 bottom right
			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(Tile.TILESIZE, Tile.TILESIZE); // v1 top right
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(0f, 0f); // v2 bottom left
			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(0f, Tile.TILESIZE); // v3 top left
	
			gl.glEnd();
	
//			tile.unloadTexture(gl);
			texture.disable(gl);
			
			gl.glPopAttrib();
			gl.glPopMatrix();
		}
	}
	public boolean detectTileCollision(float x, float y)
	{
		int tileX = (int) (x/Tile.TILESIZE);
		int tileY = (int) (y/Tile.TILESIZE);
		if (tileX >= width || tileX < 0 || tileY < 0)
		{
			return true;
		}
		return this.tiles[tileY][tileX].getType() == Tile.SOLID;
	}
	
	public Tile getTile(float x, float y)
	{
		int tileY = (int) Math.floor(y/Tile.TILESIZE);
		int tileX = (int) Math.floor(x/Tile.TILESIZE);
		if (tileX >= width || tileX < 0 || tileY < 0)
		{
			return Tile.DIRT;
		}
		return this.tiles[tileY][tileX];
	}
	
	public Tile getTile(float[] vect2f)
	{
		return getTile(vect2f[0], vect2f[1]);
	}
	
	public float getSpawnX() {
		return spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}
	
	public int getWidth() {
		return width * Tile.TILESIZE;
	}

	public int getHeight() {
		return height * Tile.TILESIZE;
	}

	@Override
	public void render(GL2 gl) {
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				drawTile(gl, x, y);
			}
		}
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		for (Sprite entity : entities)
		{
			entity.render(gl);
		}
	}
}
