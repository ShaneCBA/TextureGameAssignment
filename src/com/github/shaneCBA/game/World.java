package com.github.shaneCBA.game;

import java.util.ArrayList;
import java.util.List;

import com.drawing.GShape;
import com.jogamp.opengl.GL2;

public class World implements GShape {
	private static final int TRANSITION_LENGTH = 1000;//In milliseconds
	static World worldInstance;
	
	private List<Level> levels;
	private int currentLevel = -1;
	
	private long transitionStart = 0;
	
	private PlayerController playerController;
	private EnemyController enemyController;
	
	private World()
	{
		levels = new ArrayList<>();
	}
	
	public static World getInstance()
	{
		if (worldInstance == null)
			worldInstance = new World();
		return worldInstance;
	}
	
	public void loadLevel(Level level)
	{
		if (levels.isEmpty())
		{
			levels.add(level);
			//update the player
			if (playerController != null)
				worldInstance.nextLevel();
			else
				currentLevel++;
		}
		else
			levels.add(level);
	}
	
	public void loadLevels(List<Level> levels)
	{
		for (Level level : levels)
		{
			loadLevel(level);
		}
	}
	
	public void setPlayer(Movable player)
	{
		levels.get(currentLevel).addEntity(player);
		this.playerController = new PlayerController(player, getCurrentLevel());
	}

	public void setEnemy(Movable enemy)
	{
		levels.get(currentLevel).addEntity(enemy);
		this.enemyController = new EnemyController(enemy, getCurrentLevel()); 
	}
	public void nextLevel()
	{
		if (++currentLevel != 0)
			transitionStart = System.currentTimeMillis();
		playerController.setLevel(getCurrentLevel());
	}
	
	public Level getCurrentLevel()
	{
		return levels.get(currentLevel);
	}
	
	public Level getLastLevel()
	{
		return levels.get(currentLevel - 1);
	}
	
	private void renderFlip(GL2 gl, long timeDifference)
	{
		gl.glPushMatrix();
		
		gl.glRotatef(-90f * timeDifference/(float)TRANSITION_LENGTH, 0f, 1f, 0f);
		
		getLastLevel().render(gl);

		gl.glPopMatrix();
	}
	
	//level transition
	@Override
	public void render(GL2 gl) {

		if (playerController != null)
			playerController.updateMovement();
		if (enemyController != null)
			enemyController.updateMovement();
		getCurrentLevel().render(gl);
		if (transitionStart != 0)
		{
			long timeDifference = System.currentTimeMillis() - transitionStart;
			if (timeDifference <= TRANSITION_LENGTH)
			{
				renderFlip(gl, timeDifference);
			}
			else
			{
				transitionStart = 0;
			}
		}
		

		if (playerController.getTouchedTiles()[Tile.SIGN.ordinal()])
			nextLevel();
	}
}
