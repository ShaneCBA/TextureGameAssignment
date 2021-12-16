package com.github.shanecba.game;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.drawing.DrawWindow;
import com.drawing.GLUTCanvas;
import com.drawing.GShape;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class World implements GShape {
	private static final int TRANSITION_LENGTH = 1000;//In milliseconds
	static World worldInstance;
	
	private List<Level> levels;
	private int currentLevel = -1;

	private boolean pause = false;
	
	private long gameOverTimeStart = 0;
	private long transitionStart = 0;
	
	private PlayerController playerController;
	private EnemyController enemyController;

	private TextOverlay gameOverScreen = new TextOverlay("GAME OVER", 50, new float[] {1f, 0f, 0f});
	private TextOverlay winScreen = new TextOverlay("YOU WIN", 50, new float[] {0f, 1f, 0f});
	private boolean won;
	
	
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
	
	public boolean isGameOver()
	{
		return gameOverTimeStart > 0;
	}
	
	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
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
		if(currentLevel + 1 == levels.size())
		{
			this.won = true;
			Dialog dialog = new Dialog(DrawWindow.getInstance(), "You Win!", true);
			dialog.setLayout(new FlowLayout());
			Button continueButton = new Button("Continue");
			continueButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
					setCurrentLevel(-1);
					nextLevel();
					dialog.setVisible(false);
				}
			});
			dialog.add(new Label("Click the button to restart the game!"));
			dialog.add(continueButton);

			dialog.setSize(300,75);
			dialog.setVisible(true);
			return;
		}
		if (++currentLevel != 0)
			transitionStart = System.currentTimeMillis();
		playerController.setLevel(getCurrentLevel());
	}
	
	public void setCurrentLevel(int newLevel)
	{
		currentLevel = newLevel;
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
		gl.glPushMatrix();

		gl.glRotatef(90f-90f * timeDifference/(float)TRANSITION_LENGTH, 0f, 1f, 0f);
		getCurrentLevel().render(gl);

		gl.glPopMatrix();
	}
	
	//level transition
	@Override
	public void render(GL2 gl) {

		if (playerController != null)
			playerController.updateMovement();
		if (enemyController != null)
			enemyController.updateMovement();
		
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
		else
		{
			getCurrentLevel().render(gl);
		}
		
		if (isGameOver())
		{
			long timeDifference = System.currentTimeMillis() - gameOverTimeStart;

			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			
			
			
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor4f(0f, 0f, 0f, timeDifference/(float)TRANSITION_LENGTH);
			gl.glVertex2f(0f, 0f);
			gl.glVertex2f(GLUTCanvas.CANVAS_WIDTH, 0f);
			gl.glVertex2f(GLUTCanvas.CANVAS_WIDTH, GLUTCanvas.CANVAS_HEIGHT);
			gl.glVertex2f(0f, GLUTCanvas.CANVAS_HEIGHT);
			gl.glEnd();
			gameOverScreen.render(gl);
			if (timeDifference > TRANSITION_LENGTH*2)
			{
				gameOverTimeStart = 0;
				setPause(false);
				playerController.respawn();
			}
		}

		if (playerController.getTouchedTiles()[Tile.SIGN.ordinal()])
			nextLevel();
		if (!isGameOver() && playerController.getTouchedTiles()[Tile.WATER.ordinal()])
		{
			gameOverTimeStart = System.currentTimeMillis();
			setPause(true);
		}
	}
}
