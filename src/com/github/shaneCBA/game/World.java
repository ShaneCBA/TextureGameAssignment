package com.github.shaneCBA.game;

import java.util.ArrayList;
import java.util.List;

import com.drawing.GShape;
import com.jogamp.opengl.GL2;

public class World implements GShape {
	static World worldInstance;
	
	private List<Level> levels;
	private int currentLevel = -1;
		
	
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
		this.playerController = new PlayerController(player, getCurrentLevel());
	}

	public void setEnemy(Movable enemy)
	{
		this.enemyController = new EnemyController(enemy, getCurrentLevel()); 
	}
	public void nextLevel()
	{
		++currentLevel;
		playerController.setLevel(getCurrentLevel());
	}
	
	public Level getCurrentLevel()
	{
		return levels.get(currentLevel);
	}
	
	//level transition
	@Override
	public void render(GL2 gl) {
		if (playerController != null)
			playerController.updateMovement();
		if (enemyController != null)
			enemyController.updateMovement();
		getCurrentLevel().render(gl);
		if (playerController.getTouchedTiles()[Tile.SIGN.ordinal()])
			nextLevel();
	}
}
