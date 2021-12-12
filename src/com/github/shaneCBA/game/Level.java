package com.github.shaneCBA.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import java.util.ArrayList;

import com.drawing.GShape;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Level implements GShape {
	
	final static boolean DEBUG = false;
	
	public final static float GRAVITY = 0.5f;
	public final static int TILESIZE = 24;
	
	private Tile[][] tiles;
	private int width, height; //measured in tiles
	
	private ArrayList<Sprite> entities;

	public Level(Tile[][] tiles, int width, int height)
	{
		this.tiles = tiles;
		this.width = width;
		this.height = height;
		this.tiles = new Tile[height][width];
		entities = new ArrayList<>();
		if (DEBUG)
		{
			for (Tile[] row : this.tiles)
			{
				for (Tile tile : row)
				{
					System.out.print(tile.ordinal());
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public Level(int[][] tiles, int width, int height)
	{
		this.tiles = new Tile[height][width];
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				this.tiles[y][x] = Tile.values()[tiles[y][x]];
			}
		}
		this.width = width;
		this.height = height;
		entities = new ArrayList<>();
		if (DEBUG)
		{
			for (Tile[] row : this.tiles)
			{
				for (Tile tile : row)
				{
					System.out.print(tile.ordinal());
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	public void addEntity(Sprite entity)
	{		
		entities.add(entity);
		entity.setWorldInstance(this);
	}
	
	@Override
	public void render(GL2 gl) {
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
				gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
				drawTile(gl, x, y);
			}
		}
		for (Sprite ent : entities)
		{
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
			ent.render(gl);
		}
	}
	
	private void drawTile(GL2 gl, int x, int y)
	{
		Tile tile = tiles[y][x];


		if (tile.getDrawable())
		{
//			if (tile == Tile.SOLID)
//				DebugUtil.debugSquareIgnoreMatrix(gl, x*TILESIZE, y*TILESIZE);
			Texture texture = tile.getTexture();
			gl.glPushMatrix();
			gl.glPushAttrib(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
			
			float [] vertex2f = {1f, 1f, 1f};
			gl.glColor3fv(vertex2f, 0); // TEMP - set bg to white
			
			gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			
			gl.glTranslatef((float) x * TILESIZE, (float) y * TILESIZE, -1);
			
			
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glEnable(GL2.GL_BLEND);
	
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	
//			tile.loadTexture(gl);
			texture.bind(gl);
			texture.enable(gl);
	
			gl.glBegin(GL_TRIANGLE_STRIP);
	
			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(TILESIZE, 0f); // v0 bottom right
			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(TILESIZE, TILESIZE); // v1 top right
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(0f, 0f); // v2 bottom left
			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(0f, TILESIZE); // v3 top left
	
			gl.glEnd();
	
//			tile.unloadTexture(gl);
			texture.disable(gl);
			
			gl.glPopAttrib();
			gl.glPopMatrix();
		}
	}
	public boolean detectTileCollision(float x, float y)
	{
		int tileX = (int) (x/TILESIZE);
		int tileY = (int) (y/TILESIZE);
		if (tileX >= width || tileX < 0 || tileY >= height || tileY < 0)
		{
			return true;
		}
		return this.tiles[tileY][tileX].getType() == Tile.SOLID;
	}

	public float getTileBottom(float y)
	{
		int tileY = (int) (y/TILESIZE);
		return (tileY)*TILESIZE;
	}
	public float getTileTop(float y)
	{
		return getTileBottom(y) + TILESIZE;
	}
	
	public float getTileLeft(float x)
	{
		int tileX = (int) (x/TILESIZE);
		return (tileX)*TILESIZE;
	}
	
	public float getTileRight(float x)
	{
		return getTileLeft(x) + TILESIZE;
	}
	
	public Tile getTile(float x, float y)
	{
		int tileY = (int) (y/TILESIZE);
		int tileX = (int) (x/TILESIZE);
		if (tileX >= width || tileX < 0 || tileY >= height || tileY < 0)
		{
			return Tile.AIR;
		}
		return this.tiles[tileY][tileX];
	}
	
	public Tile getTile(float[] vect2f)
	{
		return getTile(vect2f[0], vect2f[1]);
	}
}
