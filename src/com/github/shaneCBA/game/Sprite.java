package com.github.shaneCBA.game;

import static com.jogamp.opengl.GL.GL_CW;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import com.drawing.GShape;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

//Must be provided a flipbook or list of flipbooks
//TODO Width and height are represented as ints that fit tile sizes
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
	protected World worldInstance;
	
	//Used to change the way a sprite is facing
	protected boolean facingLeft;

	//Position vector
	protected float[] positionVector2f;

	//Size vector
	protected float[] sizeVector2f;
	
	//Hitbox
	protected float[] hitboxVector[];
	
	/**
	 * @param position Position of sprite
	 * @param size Dimensions of sprite
	 */
	private Sprite(float[] position, float[] size)
	{
		this.positionVector2f = position;
		this.sizeVector2f = size;
	}


	/**
	 * @param position Position of sprite
	 * @param size Dimensions of sprite
	 * @param animations Flipbook array holding all the animations
	 */
	public Sprite(float[] position, float[] size, Flipbook[] animations)
	{
		this(position, size);
		
		this.animations = animations;
	}

	/**
	 * @param position Position of sprite
	 * @param size Dimensions of sprite
	 * @param animation Individual Flipbook animation
	 */
	public Sprite(float[] position, float[] size, Flipbook animation)
	{
		this(position, size);
		
		this.animations[0] = animation;
	}
	
	public float[] getpVector2f() {
		return positionVector2f;
	}


	public void setpVector2f(float[] pVector2f) {
		this.positionVector2f = pVector2f;
	}

	@Override
	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_ENABLE_BIT);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);

		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		Texture currentTexture = animations[currAnimation].getCurrentFrame();

		currentTexture.enable(gl);
		currentTexture.bind(gl);
		
		gl.glPushMatrix();
		gl.glTranslatef(positionVector2f[0], positionVector2f[1], 0);
		//TODO scale to world tile size
		
		if (facingLeft)
		{
			gl.glTranslatef(sizeVector2f[0], 0, 0);
			gl.glRotatef(180.0f, 0, 1, 0);
			gl.glFrontFace(GL_CW);
		}


		gl.glBegin(GL_TRIANGLE_STRIP);

		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(sizeVector2f[0], 0f); // v0 bottom right
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(sizeVector2f[0], sizeVector2f[1]); // v1 top right
		gl.glTexCoord2f(0, 0);
		gl.glVertex2f(0f, 0f); // v2 bottom left
		gl.glTexCoord2f(0, 1);
		gl.glVertex2f(0f, sizeVector2f[1]); // v3 top left

		gl.glEnd();
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		currentTexture.disable(gl);
		
		gl.glPopMatrix();
		gl.glPopAttrib();
	}
	
	public World getWorldInstance() {
		return worldInstance;
	}


	public void setWorldInstance(World worldInstance) {
		this.worldInstance = worldInstance;
	}


	public int getCurrAnimation() {
		return currAnimation;
	}

	public float getRight()
	{
		return positionVector2f[0] + sizeVector2f[0];
	}

	public float getTop()
	{
		return positionVector2f[1] + sizeVector2f[1];
	}

	public void setCurrAnimation(int currAnimation) {
		this.currAnimation = currAnimation;
	}


}
