package com.github.shaneCBA.game;

public class PlayerController {
	public static final int RUN = 0;
	public static final int IDLE = 1;
	public static final int JUMP = 2;
	
	Movable player;
	Level level;
	Keyboard keyboard = Keyboard.getInstance();
	
	public PlayerController(Movable player,  Level world)
	{
		this.player = player;
		this.level = world;
		respawn();
	}
	
	public void setPosition(float x, float y)
	{
		player.setPos(x, y);
	}

	public Movable getPlayer() {
		return player;
	}

	public void setPlayer(Movable player) {
		this.player = player;
		if (level != null)
			level.addEntity(player);
	}

	public void setLevel(Level level) {
		this.level = level;
		player.reset();
		level.addEntity(player);
		respawn();
	}
	
	public boolean[] getTouchedTiles()
	{
		return player.getTilesTouched();
	}
	
	public void updateMovement()
	{
		if (keyboard.getKeyDown('A') && !keyboard.getKeyDown('D'))
		{
			player.setXVel(-5f);
			player.setCurrAnimation(RUN);
		}
		else if (keyboard.getKeyDown('D') && !keyboard.getKeyDown('A'))
		{
			player.setXVel(5f);
			player.setCurrAnimation(RUN);
		}
		else
		{
			player.setCurrAnimation(IDLE);
		}
		if (keyboard.getKeyDown('W') && player.isGrounded())
		{
			player.setYVel(7f);
		}
		if (keyboard.getKeyDown('S') && player.isGrounded())
		{
			//move to player wrapper
			boolean onHalfBlocks = true;
			float[] pos = player.getpositionVector3f();
			float y = player.getBottom()-1;
	
			int gapcount = (int) Math.max(player.getWidth()/(Tile.TILESIZE/2f), 2f);
			float gap = player.getWidth()/gapcount;
			//Check blocks below players feet to test if all of them are halfblocks
			for (float x = player.getLeft(); onHalfBlocks && x <= player.getRight(); x += gap)
			{
				onHalfBlocks = level.getTile(x, y).getType() == Tile.HALFBLOCK || level.getTile(x, y).getType() == Tile.BREATHABLE;
			}
			if (onHalfBlocks)
				pos[1]-=1;
		}
		if (player.getvVector2()[1] != 0)
		{
			player.setCurrAnimation(JUMP);
		}
	}
	
	public void respawn()
	{
		float x = this.level.getSpawnX();
		float y = this.level.getSpawnY();
		this.player.setPos(x, y);
	}
}
	
