package com.github.shaneCBA.game;

import java.io.File;
import java.io.FileNotFoundException;
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
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	    catch (Exception e)
	    {
	    	System.out.println("BRUH");
	    	e.printStackTrace();
	    }
		return world;
	}
}
