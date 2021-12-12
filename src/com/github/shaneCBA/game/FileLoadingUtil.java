package com.github.shaneCBA.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoadingUtil {
	private FileLoadingUtil()
	{
	}
	
	public static Level readLevel(String filename)
	{
		
		
		String texPath = System.getProperty("user.dir");
		int[][] tempLevel = null;
		File myObj = new File(texPath+filename);
	    try (Scanner myReader=new Scanner(myObj))
	    {
			String data;
			int width=0;
			int height=0;
			int row = 0;
			if (myReader.hasNextLine())
			{
				data = myReader.nextLine();
				width = Integer.parseInt(data.substring(0, 2), 16);
				height = Integer.parseInt(data.substring(2, 4), 16);
			}
			tempLevel = new int[height][width];
			while (myReader.hasNextLine())
			{
				data = myReader.nextLine();
				for (int i = 0; i < width; i+=1)
				{
					char n = data.charAt(i);
					tempLevel[height - row - 1][i] = Character.getNumericValue(n);
				}
				row++;
			}
		}
	    catch (FileNotFoundException e)
	    {
			System.err.println("An error occurred while reading the level file.");
			e.printStackTrace();
		}
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    Level level = new Level(tempLevel, tempLevel[0].length, tempLevel.length,1f*Tile.TILESIZE, 2f*Tile.TILESIZE);
		return level;
	}

	
	public static ArrayList<Level> readWorld()
	{
		ArrayList<Level> levelsArrayList = new ArrayList<>();
		
		String defaultPath = System.getProperty("user.dir");
		
		File levelsDir = new File(defaultPath+"/World/Levels");
		
		String levelFileNames[] = levelsDir.list();
		for (String levelFileName : levelFileNames)
		{
			Level levelTemp = readLevel("/World/Levels/" +levelFileName);
			levelsArrayList.add(levelTemp);
		}
	
		return levelsArrayList;
	}
	
	public static Flipbook[] readSprite(String fileName, String spriteName)
	{
		String texPath = System.getProperty("user.dir");
		ArrayList<Flipbook> books = new ArrayList<>();
		File myObj = new File(texPath+fileName);
	    try (Scanner myReader = new Scanner(myObj))
	    {
			String data = null;
			int fileCount = 0;
			ArrayList<String> fileNames;
			String[] fileNamesString;
			Flipbook book;
			while (myReader.hasNextLine()) 
			{
				data = myReader.nextLine();
				if (data.equals("[" + spriteName + "]")) 
				{
					File topDir = new File(myReader.nextLine());
					File topDirFull = new File(texPath + topDir);
				
					//run animation
					File runDir = new File(topDir.toString() + "/run");
					File runDirFull = new File(topDirFull.toString() + "/run");
					if (runDirFull.exists())
					{
						fileCount = runDirFull.list().length;
						fileNames = new ArrayList<>();
					    for (int i = 1; i <= fileCount; i++)
					    {
					    	File test = new File(runDirFull + "\\Run" + i + ".png");
					    	if (test.exists())
					    	{
					    		fileNames.add(runDir + "\\Run" + i + ".png");
					    	}
					    }
					    fileNamesString = new String[fileNames.size()];
					    fileNamesString = fileNames.toArray(fileNamesString);
					    book = new Flipbook(fileNamesString);
						books.add(book);
					}
					
					//idle animation
					File idleDir = new File(topDir.toString() + "/idle");
					File idleDirFull = new File(topDirFull.toString() + "/idle");
					if (idleDirFull.exists())
					{
						fileCount = idleDirFull.list().length;
					    fileNames = new ArrayList<>();
					    for (int i = 1; i <= fileCount; i++)
					    {
					    	File test = new File(idleDirFull + "\\Idle" + i + ".png");
					    	if (test.exists())
					    	{
					    		fileNames.add(idleDir + "\\Idle" + i + ".png");
					    	}
					    }
					    fileNamesString = new String[fileNames.size()];
					    fileNamesString = fileNames.toArray(fileNamesString);
					    book = new Flipbook(fileNamesString);
						books.add(book);
					}
				
					//jump animation
					File jumpDir = new File(topDir.toString() + "/jump");
					File jumpDirFull = new File(topDirFull.toString() + "/jump");
					if (jumpDirFull.exists())
					{
						fileCount = jumpDirFull.list().length;
					    fileNames = new ArrayList<>();
					    for (int i = 1; i <= fileCount; i++)
					    {
					    	File test = new File(jumpDirFull + "\\Jump" + i + ".png");
					    	if (test.exists())
					    	{
					    		fileNames.add(jumpDir + "\\Jump" + i + ".png");
					    	}
					    }
					    fileNamesString = new String[fileNames.size()];
					    fileNamesString = fileNames.toArray(fileNamesString);
					    book = new Flipbook(fileNamesString);
						books.add(book);
					}
				}
			}
	    }
	    catch (FileNotFoundException e)
	    {
			System.err.println("An error occurred while reading the sprite file.");
			e.printStackTrace();
		}
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    Flipbook[] flipbooks = new Flipbook[books.size()];
		flipbooks = books.toArray(flipbooks);
		return flipbooks;
	}
	
	
	
}
