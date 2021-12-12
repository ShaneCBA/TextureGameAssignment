package com.github.shaneCBA.game;

import java.util.ArrayList;

import com.drawing.GShape;
import com.jogamp.opengl.GL2;

public class World implements GShape {
	static ArrayList<Level> levels;
	static int currentLevel = 0;
	
	static World worldInstance;
	
	public static World getInstance()
	{
		if (worldInstance == null)
			worldInstance = new World();
		return worldInstance;
	}
	
	public static void loadLevels(ArrayList<Level> levels)
	{
		World.levels = levels;
	}

	@Override
	public void render(GL2 gl) {
		// TODO Auto-generated method stub
		
	}
}
