package com.github.shaneCBA.game;

import com.jogamp.opengl.GL2;

public class Movable extends Sprite {
	private final static float TERMINAL_VEL = -10;

	//Prior position vector
	private float[] oldPositionVector2f;

	//Velocity vector
	private float[] velocityVector2f;

	private boolean grounded;
	private boolean wasGrounded;

	public Movable(float[] position, float[] size, float[] hitboxVector2f, Flipbook[] animations) {
		super(position, size, hitboxVector2f, animations);
		velocityVector2f = new float[] {0f,0f};
	}
	//Pass gl for debug purposes. Remove for final
	private boolean checkGrounded(GL2 gl)
	{
		//If velocity Y is poositive, or the previous position of the sprite isn't
		//above the tile, then there was no ground collision
		if (velocityVector2f[1] > 0 || oldPositionVector2f[1] < worldInstance.getTileTopY(positionVector2f[1]))
		{
			return false;
		}
		//Check every half-tile below the sprite's hitbox
		for (float x = positionVector2f[0]; x <= getRight(); x += World.TILESIZE/2)
		{
			if (worldInstance.getTile(x, positionVector2f[1]-1).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkCeiling()
	{
		for (float x = positionVector2f[0]; x <= getRight(); x += World.TILESIZE/2)
		{
			if (worldInstance.detectTileCollision(x, getTop()))
			{
				return true;
			}
		}
		return false;
	}
	
	//GL passed for dbug purposes, remove for final
	private void update(GL2 gl)
	{
		oldPositionVector2f = positionVector2f.clone();
		wasGrounded = grounded;
		
		if (!grounded)
		{
			velocityVector2f[1] = Math.max(velocityVector2f[1] - World.GRAVITY, TERMINAL_VEL);
		}
		if (grounded)
		{
			if (velocityVector2f[0] > 0)
				velocityVector2f[0] = Math.max(velocityVector2f[0] - 0.5f, 0);
			else if (velocityVector2f[0] < 0)
				velocityVector2f[0] = Math.min(velocityVector2f[0] + 0.5f, 0);
		}
		if (velocityVector2f[0] > 0)
		{
			facingLeft = false;
		}
		else if (velocityVector2f[0] < 0)
		{
			facingLeft = true;
		}

		positionVector2f[0] += velocityVector2f[0];
		positionVector2f[1] += velocityVector2f[1];
		
		grounded = checkGrounded(gl);
		
		if (grounded && !wasGrounded && velocityVector2f[1] < 0 && oldPositionVector2f[1] >= worldInstance.getTileTopY(positionVector2f[1]-1))
		{
			//Set Y velocity to zero
			velocityVector2f[1] = 0;
			
			//Move the sprite at a reverse angle to simulate collision
			float topTile = worldInstance.getTileTopY(positionVector2f[1]-1);
			float offPercent = (topTile-positionVector2f[1])/(oldPositionVector2f[1] - positionVector2f[1]);
			positionVector2f[1] = topTile;
			positionVector2f[0] += (oldPositionVector2f[0] - positionVector2f[0])*offPercent;
		}
		if (checkCeiling() && velocityVector2f[1] > 0)
		{
			positionVector2f[1] = worldInstance.getTileTopY(getTop())-2*World.TILESIZE;
			velocityVector2f[1] = 0;
		}
	}
	
	public void setXVel(float x)
	{
		velocityVector2f[0] = x;
	}

	public void setYVel(float y) {
		velocityVector2f[1] = y;
	}
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public float[] getvVector2() {
		return velocityVector2f;
	}

	@Override
	public void render(GL2 gl)
	{
		update(gl);
		super.render(gl);
		DebugUtil.debugSquare(gl, positionVector2f[0], positionVector2f[1]);
		DebugUtil.debugSquare(gl, getRight(), positionVector2f[1]);
	}

}
