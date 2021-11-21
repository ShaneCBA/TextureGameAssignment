package com.github.shaneCBA.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoadingUtil {
	
	//.wd
	public static int [][] readOldWorld(String filename)
	{
		String texPath = System.getProperty("user.dir");
		int[][] world = null; 
	    try
	    {
			File myObj = new File(texPath+filename);
			Scanner myReader = new Scanner(myObj);
			String data;
			int width=0, height=0;
			int row = 0;
			if (myReader.hasNextLine())
			{
				data = myReader.nextLine();
				width = Integer.parseInt(data.substring(0, 2), 16);
				height = Integer.parseInt(data.substring(2, 4), 16);
			}
			world = new int[height][width];
			while (myReader.hasNextLine())
			{
				data = myReader.nextLine();
				for (int i = 0; i < width; i+=1)
				{
					char n = data.charAt(i);
					world[height - row - 1][i] = Character.getNumericValue(n);
				}
				row++;
			}
			myReader.close();
		}
	    catch (FileNotFoundException e)
	    {
			System.out.println("An error occurred while reading the world file.");
			e.printStackTrace();
		}
	    catch (Exception e)
	    {
	    	System.out.println("BRUH");
	    	e.printStackTrace();
	    }
		return world;
	}
	
	//.data
	public static Flipbook[] readSprite(String fileName, String spriteName)
	{
		String texPath = System.getProperty("user.dir");
		ArrayList<Flipbook> books = new ArrayList<Flipbook>();
	    try
	    {
			File myObj = new File(texPath+fileName);
			Scanner myReader = new Scanner(myObj);
			String data = null;
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
				    int fileCount = runDirFull.list().length;
				    ArrayList<String> fileNames = new ArrayList<String>();
				    for (int i = 1; i <= fileCount; i++)
				    {
				    	File test = new File(runDirFull + "\\Run" + i + ".png");
				    	if (test.exists())
				    	{
				    		fileNames.add(runDir + "\\Run" + i + ".png");
				    	}
				    }
				    String[] fileNamesString = new String[fileNames.size()];
				    fileNamesString = fileNames.toArray(fileNamesString);
				    Flipbook book = new Flipbook(fileNamesString);
					books.add(book);
					
					//idle animation
					File idleDir = new File(topDir.toString() + "/idle");
					File idleDirFull = new File(topDirFull.toString() + "/idle");
					fileCount = idleDirFull.list().length;
				    fileNames = new ArrayList<String>();
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
				
					//jump animation
					File jumpDir = new File(topDir.toString() + "/jump");
					File jumpDirFull = new File(topDirFull.toString() + "/jump");
					fileCount = jumpDirFull.list().length;
				    fileNames = new ArrayList<String>();
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
			myReader.close();
	    }
	    catch (FileNotFoundException e)
	    {
			System.out.println("An error occurred while reading the sprite file.");
			e.printStackTrace();
		}
	    catch (Exception e)
	    {
	    	System.out.println("BRUH");
	    	e.printStackTrace();
	    }
	    Flipbook[] flipbooks = new Flipbook[books.size()];
		flipbooks = books.toArray(flipbooks);
		return flipbooks;
	}
	
	
	
}
