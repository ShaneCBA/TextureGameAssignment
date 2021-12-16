package com.github.shanecba.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import com.drawing.GShape;
import com.drawing.GLUTCanvas;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

public class TextOverlay implements GShape {

	private Font font;
	private TextRenderer renderer;
	private String text;
	private float[] colorVector3f;
	private int x;
	private int y;
	
	public TextOverlay(String text, int fontsize, float[] colorVector3f)
	{
		font = new Font("SansSerif", Font.BOLD, fontsize);
		renderer = new TextRenderer(font);
		Rectangle2D rect = renderer.getBounds(text);
		x = (int) (GLUTCanvas.CANVAS_WIDTH/2 - rect.getWidth()/2);
		y = (int) (GLUTCanvas.CANVAS_HEIGHT/2 - rect.getHeight()/2);
		this.text = text;
		this.colorVector3f = colorVector3f.clone();
	}
	
	@Override
	public void render(GL2 gl) {

		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	    renderer.beginRendering(GLUTCanvas.CANVAS_WIDTH, GLUTCanvas.CANVAS_HEIGHT);
	    renderer.setColor(colorVector3f[0], colorVector3f[1], colorVector3f[2], 1f);
	    renderer.draw(text, x, y);
	    renderer.end3DRendering();
	    
	    
	}

}
