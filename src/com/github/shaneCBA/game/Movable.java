package com.github.shaneCBA.game;

import com.jogamp.opengl.GL2;

public class Movable extends Sprite {
	private final static float TERMINAL_VEL = -10;

	private float[] debug;
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
		//If velocity Y is positive, or the previous position of the sprite isn't
		//above the tile, then there was no ground collision
		if (velocityVector2f[1] > 0 || getOldBottom() < worldInstance.getTileTop(getBottom()))
		{
			return false;
		}
		//Check every half-tile below the sprite's hitbox
		for (float x = getLeft(); x <= getRight(); x += World.TILESIZE/2)
		{
			int type = worldInstance.getTile(x, getBottom()-1).getType();
			if (type == Tile.SOLID || type == Tile.HALFBLOCK)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkCeiling()
	{
		//If velocity Y is negative, or the previous position of the sprite isn't
		//below the tile, then there was no ceiling collision
		if (velocityVector2f[1] < 0 || getOldTop() > worldInstance.getTileTop(getTop()) - World.TILESIZE)
		{
			return false;
		}
		for (float x = getLeft(); x <= getRight(); x += World.TILESIZE/2)
		{
			if (worldInstance.getTile(x, getTop()).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkRight()
	{
		if (velocityVector2f[0] < 0 || getOldRight() > worldInstance.getTileRight(getRight()) - World.TILESIZE)
		{
			return false;
		}
		for (float y = getBottom(); y <= getTop(); y += World.TILESIZE/2)
		{
			if (worldInstance.getTile(getRight(), y).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkLeft()
	{
		if (velocityVector2f[0] > 0 || getOldLeft() < worldInstance.getTileLeft(getLeft()) - World.TILESIZE)
		{
			return false;
		}
		for (float y = getBottom(); y <= getTop(); y += World.TILESIZE/2)
		{
			if (worldInstance.getTile(getLeft(), y).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	//GL passed for dbug purposes, remove for final
	private void update(GL2 gl)
	{
		oldPositionVector2f = positionVector3f.clone();
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

		positionVector3f[0] += velocityVector2f[0];
		positionVector3f[1] += velocityVector2f[1];
		
		grounded = checkGrounded(gl);
		
		if (grounded && !wasGrounded)
		{
			//Set Y velocity to zero
			velocityVector2f[1] = 0;
			
			//Move the sprite at a reverse angle to simulate collision
			float topTile = worldInstance.getTileTop(getBottom()-1);
			float offPercent = (topTile-getBottom())/(getOldBottom() - getBottom());
			positionVector3f[1] = topTile +  hitboxVector2f[1];
			positionVector3f[0] += (getOldLeft() - getLeft())*offPercent;
		}
		if (checkCeiling())
		{
			positionVector3f[1] = worldInstance.getTileBottom(getTop()) - getHeight() - hitboxVector2f[1]-1;
			velocityVector2f[1] = 0;
		}
		if (checkRight())
		{
			positionVector3f[0] = worldInstance.getTileLeft(getRight()) - getWidth() - hitboxVector2f[0]-1;
			velocityVector2f[0] = 0;
		}
		else if (checkLeft())
		{
			positionVector3f[0] = worldInstance.getTileRight(getLeft()) - hitboxVector2f[0]+1;
			velocityVector2f[0] = 0;
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
	
	public float getOldLeft()
	{
		return oldPositionVector2f[0] + hitboxVector2f[0];
	}
	public float getOldRight()
	{
		return oldPositionVector2f[0] + hitboxVector2f[2];
	}

	public float getOldBottom()
	{
		return oldPositionVector2f[1] + hitboxVector2f[1];
	}
	public float getOldTop()
	{
		return oldPositionVector2f[1] + hitboxVector2f[3];
	}

	@Override
	public void render(GL2 gl)
	{
		update(gl);
		super.render(gl);
		if (debug != null)
		{
			DebugUtil.debugSquare(gl, debug[0], debug[1]);
		}
		DebugUtil.debugSquare(gl, getLeft(), getBottom());
		DebugUtil.debugSquare(gl, getRight(), getBottom());
		DebugUtil.debugSquare(gl, getLeft(), getTop());
		DebugUtil.debugSquare(gl, getRight(), getTop());
	}

}
