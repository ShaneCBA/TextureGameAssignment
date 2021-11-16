package com.github.shaneCBA.game;

import com.jogamp.opengl.GL2;

public class Movable extends Sprite {
	private final float TERMINAL_VEL = -10;
	
	private float[] debugVector;

	//Prior position vector
	private float[] oldPVector2f;

	//Velocity vector
	private float[] vVector2f;
	//Prior velocity vector
	private float[] oldVVector2f;

	private boolean grounded;
	private boolean wasGrounded;

	public Movable(float[] position, float[] size, Flipbook[] animations) {
		super(position, size, animations);
		vVector2f = new float[] {0f,0f};
	}
	//Pass gl for debug purposes. Remove for final
	//TODO Change to getFloorTile
	private boolean checkGrounded(GL2 gl)
	{
		//TODO Modify to instead get the tile
		//Save for pass-through blocks
		if (vVector2f[1] > 0 || oldPVector2f[1] < worldInstance.getTileTopY(pVector2f[1]))
		{
			return false;
		}
		for (float x = pVector2f[0]; x <= getRight(); x += World.TILESIZE)
		{
			if (worldInstance.detectTileCollision(x, pVector2f[1]-1))
			{
				return true;
			}
		}
		return false;
	}
	//TODO
	public boolean checkCeiling()
	{
		for (float x = pVector2f[0]; x <= getRight(); x += World.TILESIZE)
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
		oldVVector2f = vVector2f.clone();
		oldPVector2f = pVector2f.clone();
		wasGrounded = grounded;
		
		if (!wasGrounded)
		{
			vVector2f[1] = Math.max(vVector2f[1] - World.GRAVITY, TERMINAL_VEL);
		}
		if (wasGrounded)
		{
			if (vVector2f[0] > 0)
				vVector2f[0] = Math.max(vVector2f[0] - 1, 0);
			else if (vVector2f[0] < 0)
				vVector2f[0] = Math.min(vVector2f[0] + 1, 0);
		}
		if (vVector2f[0] > 0)
		{
			facingLeft = false;
		}
		else if (vVector2f[0] < 0)
		{
			facingLeft = true;
		}

		pVector2f[0] += vVector2f[0];
		pVector2f[1] += vVector2f[1];
		
		grounded = checkGrounded(gl);
		
		if (grounded && !wasGrounded && vVector2f[1] < 0 && oldPVector2f[1] >= worldInstance.getTileTopY(pVector2f[1]-1))
		{
			//Set Y velocity to zero
			vVector2f[1] = 0;
			
			//Move the sprite at a reverse angle to simulate collision
			float topTile = worldInstance.getTileTopY(pVector2f[1]-1);
			float offPercent = (topTile-pVector2f[1])/(oldPVector2f[1] - pVector2f[1]);
			pVector2f[1] = topTile;
			pVector2f[0] += (oldPVector2f[0] - pVector2f[0])*offPercent;
		}
//		if (checkCeiling() && vVector2f[1] > 0)
//		{
//			pVector2f[1] = worldInstance.getTileTopY(getTop())-2*World.TILESIZE;
//			vVector2f[1] = 0;
//		}
	}
	
	public void setXVel(float x)
	{
		vVector2f[0] = x;
	}

	public void setYVel(float y) {
		vVector2f[1] = y;
	}
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public float[] getvVector2() {
		return vVector2f;
	}

	@Override
	public void render(GL2 gl)
	{
		update(gl);
		super.render(gl);
		if (debugVector != null)
		{
			DebugUtil.debugSquare(gl, debugVector[0], debugVector[1]);
		}
		DebugUtil.debugSquare(gl, pVector2f[0], pVector2f[1]);
		DebugUtil.debugSquare(gl, getRight(), pVector2f[1]);
	}

}
