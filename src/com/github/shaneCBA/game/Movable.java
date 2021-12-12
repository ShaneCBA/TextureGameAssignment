package com.github.shaneCBA.game;

import com.jogamp.opengl.GL2;

public class Movable extends Sprite {
	private static final float TERMINAL_VEL = -10;

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
	
	public void setXVel(float x)
	{
		velocityVector2f[0] = x;
	}

	public void setYVel(float y) {
		velocityVector2f[1] = y;
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

	public boolean[] getTilesTouched() {
		boolean[] touched = new boolean[Tile.values().length];
		int gapcount = (int) Math.max(getWidth()/(Tile.TILESIZE/2f), 2f);
		float gap = getWidth()/gapcount;
		for (float x = getLeft(); x <= getRight(); x += gap)
		{
			int tileId = levelInstance.getTile(x, getBottom()-1).ordinal();
			if (!touched[tileId])
				touched[tileId] = true;
			
		}
		return touched;
	}

	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}
	
	private boolean checkGrounded()
	{
		//If velocity Y is positive, or the previous position of the sprite isn't
		//above the tile, then there was no ground collision
		if (velocityVector2f[1] > 0 || getOldBottom() < TileUtil.getTileTop(getBottom()))
		{
			return false;
		}
		//Move to constructor or initialization
		//Used to allow non-tile sized sprites
		int gapcount = (int) Math.max(getWidth()/(Tile.TILESIZE/2f), 2f);
		float gap = getWidth()/gapcount;
		for (float x = getLeft(); x <= getRight(); x += gap)
		{
			int type = levelInstance.getTile(x, getBottom()-1).getType();
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
		if (velocityVector2f[1] < 0 || getOldTop() > TileUtil.getTileTop(getTop()) - Tile.TILESIZE)
		{
			return false;
		}
		//Move to constructor or initialization
		//Used to allow non-tile sized sprites
		int gapcount = (int) Math.max(getWidth()/(Tile.TILESIZE/2f), 2f);
		float gap = getWidth()/gapcount;
		for (float x = getLeft(); x <= getRight(); x += gap)
		{
			if (levelInstance.getTile(x, getTop()).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkRight()
	{
		if (velocityVector2f[0] < 0 || getOldRight() > TileUtil.getTileRight(getRight()) - Tile.TILESIZE)
		{
			return false;
		}
		int gapcount = (int) Math.max(getHeight()/(Tile.TILESIZE/2f), 2f);
		float gap = getHeight()/gapcount;
		for (float y = getBottom(); y <= getTop(); y += gap)
		{
			if (levelInstance.getTile(getRight(), y).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean checkLeft()
	{
		if (velocityVector2f[0] > 0 || getOldLeft() < TileUtil.getTileLeft(getLeft()) - Tile.TILESIZE)
		{
			return false;
		}
		int gapcount = (int) Math.max(getHeight()/(Tile.TILESIZE/2f), 2f);
		float gap = getHeight()/gapcount;
		for (float y = getBottom(); y <= getTop(); y += gap)
		{
			if (levelInstance.getTile(getLeft(), y).getType() == Tile.SOLID)
			{
				return true;
			}
		}
		return false;
	}
	
	private void update()
	{
		oldPositionVector2f = positionVector3f.clone();
		wasGrounded = grounded;
		
		if (!grounded)
		{
			velocityVector2f[1] = Math.max(velocityVector2f[1] - Level.GRAVITY, TERMINAL_VEL);
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
		
		grounded = checkGrounded();
		
		if (grounded && !wasGrounded)
		{
			//Set Y velocity to zero
			velocityVector2f[1] = 0;
			
			//Move the sprite at a reverse angle to simulate collision
			float topTile = TileUtil.getTileTop(getBottom()-1);
			float offPercent = (topTile-getBottom())/(getOldBottom() - getBottom());
			positionVector3f[1] = topTile - hitboxVector2f[1];
			positionVector3f[0] += (getOldLeft() - getLeft())*offPercent;
		}
		if (checkCeiling())
		{
			positionVector3f[1] = TileUtil.getTileBottom(getTop()) - getHeight() - hitboxVector2f[1]-1;
			velocityVector2f[1] = 0;
		}
		if (checkRight())
		{
			positionVector3f[0] = TileUtil.getTileLeft(getRight()) - getWidth() - hitboxVector2f[0]-1;
			velocityVector2f[0] = 0;
		}
		else if (checkLeft())
		{
			positionVector3f[0] = TileUtil.getTileRight(getLeft()) - hitboxVector2f[0]+1;
			velocityVector2f[0] = 0;
		}
	}
	
	public void reset()
	{
		this.velocityVector2f = new float[] {0,0};
		this.grounded = false;
		this.wasGrounded = false;
		this.oldPositionVector2f = null;
		this.currAnimation = 0;
	}

	@Override
	public void render(GL2 gl)
	{
		update();
		super.render(gl);
		DebugUtil.debugSquare(gl, getLeft(), getBottom());
		DebugUtil.debugSquare(gl, getRight(), getBottom());
		DebugUtil.debugSquare(gl, getLeft(), getTop());
		DebugUtil.debugSquare(gl, getRight(), getTop());
	}

}
