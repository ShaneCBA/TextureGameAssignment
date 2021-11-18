package com.github.shaneCBA.game;

import java.util.HashMap;

public class Keyboard {
	private static Keyboard instance;

	private static HashMap<Integer, Boolean> keyHash;
	
	private Keyboard()
	{
		keyHash = new HashMap<>();
	}
	
	public static Keyboard getInstance()
	{
		if (instance == null)
			instance = new Keyboard();
		return instance;
	}

	public void setKeyDown(int key, boolean keyDown)
	{
		keyHash.put(key, keyDown);
	}
	public boolean getKeyDown(int key)
	{
		boolean exists = keyHash.containsKey(key);
		if (!exists)
		{
			keyHash.put(key, false);
		}
		return keyHash.get(key);
	}
	
	public boolean getKeyDown(char keyChar)
	{
		if (keyChar >= 97 && keyChar <= 122)
			return getKeyDown((int)keyChar - 32);
		return getKeyDown((int)keyChar);
	}
	public String toString()
	{
		return keyHash.toString();
	}
}
