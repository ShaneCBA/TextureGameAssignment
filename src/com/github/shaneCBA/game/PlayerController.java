package com.github.shaneCBA.game;

public class PlayerController {
	public static final int RUN = 0, IDLE = 1, JUMP = 2;
	
	Movable player;
	Level world;
	Keyboard keyboard = Keyboard.getInstance();
	
	public PlayerController(Movable player,  Level world)
	{
		this.player = player;
		this.world = world;
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
			float[] pos = player.getpositionVector2f();
			float y = player.getBottom()-1;
	
			int gapcount = (int) Math.max(player.getWidth()/(Level.TILESIZE/2), 2f);
			float gap = player.getWidth()/gapcount;
			//Check blocks below players feet to test if all of them are halfblocks
			for (float x = player.getLeft(); onHalfBlocks && x <= player.getRight(); x += gap)
			{
				onHalfBlocks = world.getTile(x, y).getType() == Tile.HALFBLOCK;
			}
			if (onHalfBlocks)
				pos[1]-=1;
		}
		if (player.getvVector2()[1] != 0)
		{
			player.setCurrAnimation(JUMP);
		}
	}
}
	
