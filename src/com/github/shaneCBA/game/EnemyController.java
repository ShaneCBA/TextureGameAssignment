package com.github.shanecba.game;

public class EnemyController {
	public static final int RUN = 0;
	public static final int IDLE = 1;
	public static final int JUMP = 2;
	
	Movable enemy;
	Level level;
	private boolean goingLeft;
	
	public EnemyController(Movable enemy,  Level world)
	{
		this.enemy = enemy;
		this.level = world;
	}
	
	public void setPosition(float x, float y)
	{
		enemy.setPos(x, y);
	}

	public Movable getEnemy() {
		return enemy;
	}

	public void setEnemy(Movable enemy) {
		this.enemy = enemy;
		if (level != null)
			level.addEntity(enemy);
	}

	public void setLevel(Level level) {
		this.level = level;
		enemy.reset();
		level.addEntity(enemy);
	}
	
	public boolean[] getTouchedTiles()
	{
		return enemy.getTilesTouched();
	}
	
	public void updateMovement()
	{
		enemy.setCurrAnimation(RUN);
		
		
		if (this.goingLeft == true)
		{
			enemy.setXVel(-5f);
		}
		else 
		if (this.goingLeft == false)
		{
			enemy.setXVel(5f);
		}
		if (enemy.getOldCheckRight())
		{
			goingLeft = true;
		}
		if (enemy.getOldCheckLeft())
		{
			goingLeft = false;
		}
	}
}
