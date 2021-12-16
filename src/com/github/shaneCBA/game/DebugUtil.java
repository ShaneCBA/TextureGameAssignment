package com.github.shanecba.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_LINE_LOOP;
import static com.jogamp.opengl.GL2GL3.GL_LINE;

import com.jogamp.opengl.GL2;

public class DebugUtil {
	private DebugUtil()
	{
	}
	public static void debugSquare(GL2 gl, float x, float y)
	{
		gl.glPushAttrib(GL2.GL_CURRENT_BIT);
		float [] vertex2f = {255f, 0f, 0f};
		gl.glColor3fv(vertex2f, 0);
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		gl.glLineWidth(4f);
		gl.glBegin(GL_LINE_LOOP);

		gl.glVertex2f(x + 1f, y - 1f); // v0 bottom right
		gl.glVertex2f(x + 1f, y + 1f); // v1 top right
		gl.glVertex2f(x - 1f, y + 1f); // v3 top left
		gl.glVertex2f(x - 1f, y - 1f); // v2 bottom left
		
		gl.glEnd();
		gl.glPopAttrib();
	}
	public static void debugSquareIgnoreMatrix(GL2 gl, float x, float y)
	{
		gl.glPushMatrix();
		gl.glLoadIdentity();
		float [] vertex2f = {255f, 0f, 0f};
		gl.glColor3fv(vertex2f, 0); // outline color
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		gl.glLineWidth(4f);
		gl.glBegin(GL_LINE_LOOP);

		gl.glVertex2f(x + 1f, y - 1f); // v0 bottom right
		gl.glVertex2f(x + 1f, y + 1f); // v1 top right
		gl.glVertex2f(x - 1f, y + 1f); // v3 top left
		gl.glVertex2f(x - 1f, y - 1f); // v2 bottom left
		
		gl.glEnd();
		gl.glPopMatrix();
	}
}
