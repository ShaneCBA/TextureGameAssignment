package com.github.shaneCBA.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import com.drawing.GShape;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

//Must be provided a flipbook or list of flipbooks
/**
 * @author shane
 *
 */
public class Sprite implements GShape
{

	//Array of animation sets
	private Flipbook[] animations;
	
	//Int holding the index of the animation from the animation set
	protected int currAnimation;
	
	//Allows access to the world instance for use in calcuation and condition
	//testing
	protected Level levelInstance;
	
	//Used to change the way a sprite is facing
	protected boolean facingLeft;

	//Position vector
	protected float[] positionVector3f;

	//Size vector
	protected float[] sizeVector2f;
	
	//Hitbox
	protected float[] hitboxVector2f;
	
	/**
	 * @param position Position of sprite
	 * @param size Dimensions of sprite
	 */
	private Sprite(float[] position, float[] size, float[] hitboxVector2f)
	{
		this.positionVector3f = position;
		this.sizeVector2f = size;
		this.hitboxVector2f = hitboxVector2f;
	}


	/**
	 * @param position Position of sprite
	 * @param size Dimensions of sprite
	 * @param animations Flipbook array holding all the animations
	 */
	public Sprite(float[] position, float[] size, float[] hitboxVector2f, Flipbook[] animations)
	{
		this(position, size, hitboxVector2f);
		
		this.animations = animations;
	}

	/**
	 * @param position Position of sprite
	 * @param size Dimensions of sprite
	 * @param animation Individual Flipbook animation
	 */
	public Sprite(float[] position, float[] size, float[] hitboxVector2f, Flipbook animation)
	{
		this(position, size, hitboxVector2f);
		this.animations = new Flipbook[]{animation};
	}
	
	public float[] getpositionVector3f() {
		return positionVector3f;
	}


	public void setpositionVector3f(float[] pVector2f) {
		this.positionVector3f = pVector2f;
	}

	public void setPos(float x, float y) {
		this.positionVector3f[0] = x;
		this.positionVector3f[1] = y;
	}
	
	public Level getWorldInstance() {
		return levelInstance;
	}


	public void setWorldInstance(Level worldInstance) {
		this.levelInstance = worldInstance;
	}


	public int getCurrAnimation() {
		return currAnimation;
	}

	public float getLeft()
	{
		return positionVector3f[0] + hitboxVector2f[0];
	}
	public float getRight()
	{
		return positionVector3f[0] + hitboxVector2f[2];
	}

	public float getBottom()
	{
		return positionVector3f[1] + hitboxVector2f[1];
	}
	public float getTop()
	{
		return positionVector3f[1] + hitboxVector2f[3];
	}

	public float getHeight()
	{
		return hitboxVector2f[3] - hitboxVector2f[1];
	}

	public float getWidth()
	{
		return hitboxVector2f[2] - hitboxVector2f[0];
	}

	public void setCurrAnimation(int currAnimation) {
		this.currAnimation = currAnimation;
	}

	@Override
	public void render(GL2 gl) {
		gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_BLEND);

		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		Texture currentTexture = animations[currAnimation].getCurrentFrame();

		currentTexture.enable(gl);
		currentTexture.bind(gl);
		
		gl.glPushMatrix();
		gl.glTranslatef(positionVector3f[0], positionVector3f[1], positionVector3f[2]);
		
		if (facingLeft)
		{
			gl.glTranslatef(sizeVector2f[0], 0, 0);
		}

		gl.glBegin(GL_TRIANGLE_STRIP);

		if (facingLeft)
		{
			float nX = sizeVector2f[0] - hitboxVector2f[2] +  hitboxVector2f[0] + getWidth();
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(sizeVector2f[0]-nX, 0f); // v0 bottom right
			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(sizeVector2f[0]-nX, sizeVector2f[1]); // v1 top right
			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(-nX, 0f); // v2 bottom left
			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(-nX, sizeVector2f[1]); // v3 top left
		}
		else
		{
			gl.glTexCoord2f(1, 0);
			gl.glVertex2f(sizeVector2f[0], 0f); // v0 bottom right
			gl.glTexCoord2f(1, 1);
			gl.glVertex2f(sizeVector2f[0], sizeVector2f[1]); // v1 top right
			gl.glTexCoord2f(0, 0);
			gl.glVertex2f(0f, 0f); // v2 bottom left
			gl.glTexCoord2f(0, 1);
			gl.glVertex2f(0f, sizeVector2f[1]); // v3 top left
		}

		gl.glEnd();
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		currentTexture.disable(gl);
		
		gl.glPopMatrix();
		gl.glPopAttrib();
	}
}
