package com.github.shaneCBA.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import java.util.ArrayList;

import com.drawing.GShape;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class World implements GShape {
	
	public final static float GRAVITY = 9.8f;
	public final static int TILESIZE = 24;
	
	private Tile[][] tiles;
	private int width, height; //measured in tiles
	
	private ArrayList<Sprite> entities;
	
	public World(Tile[][] tiles, int width, int height)
	{
		this.tiles = tiles;
		this.width = width;
		this.height = height;
		entities = new ArrayList<>();
		for (Tile[] row : tiles)
		{
			for (Tile tile : row)
			{
				System.out.print(tile.ordinal());
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void addEntity(Sprite entity)
	{		
		entities.add(entity);
	}
	
	@Override
	public void render(GL2 gl) {
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				DebugUtil.debugSquareIgnoreMatrix(gl, x*TILESIZE, y*TILESIZE);
				drawTile(gl, x, y);
			}
		}
		for (Sprite ent : entities)
		{
			ent.render(gl);
		}
	}
	
	private void drawTile(GL2 gl, int x, int y)
	{
		Tile tile = tiles[y][x];


		if (tile.getDrawable())
		{
			Texture texture = tile.getTexture();
			gl.glPushMatrix();
			gl.glPushAttrib(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
			
			float [] vertex2f = {1f, 1f, 1f};
			gl.glColor3fv(vertex2f, 0); // TEMP - set bg to white
			
			gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			
			gl.glTranslatef((float) x * TILESIZE, (float) y * TILESIZE, 0);
			
			
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
}
