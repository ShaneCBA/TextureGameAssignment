package com.github.shaneCBA.game;

public class EnemyController {
	public static final int RUN = 0;
	public static final int IDLE = 1;
	public static final int JUMP = 2;
	
	Movable enemy;
	Level level;
	int maxPatrol;
	private boolean goingLeft;
	private int currentPatrolLength;
	
	public EnemyController(Movable enemy,  Level world, int maxPatrol)
	{
		this.enemy = enemy;
		this.level = world;
		this.maxPatrol = maxPatrol;
		currentPatrolLength = 1;
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
		if (this.goingLeft == true )
		{
			enemy.setXVel(-5f);
		}
		else 
		if (this.goingLeft == false)
		{
			enemy.setXVel(5f);
		}
		this.currentPatrolLength++;
		
		

		if (this.goingLeft == true && this.currentPatrolLength % this.maxPatrol == 0)
		{
			this.goingLeft = false;
		} 
		else
		if (this.goingLeft == false && this.currentPatrolLength % this.maxPatrol == 0)
		{
			this.goingLeft = true;
		}
		
	}
}
	
