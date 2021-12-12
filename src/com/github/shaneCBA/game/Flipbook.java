package com.github.shaneCBA.game;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drawing.GTextureUtil;
import com.jogamp.opengl.util.texture.Texture;

/**
 * 
 * @author shane
 * Instance utility class for retreiving, storing, and providing textures for
 * animations. Each animation (set of frames) is stored in one flipbook
 */
public class Flipbook {
	private Texture[] pages;
	private int currentFrame;
	private int frameCount;
	
	//lastFrameUpdateTime is the time in which the frame was last changed
	private long lastFrameUpdateTime;
	private long lastFrameUpdateCheck;//keep current just in case
	
	//duration of each frame in milliseconds
	private int frameDuration = 85;

	/**
	 * @param fileNames List of filenames to be loaded into the pages.
	 * <b>NOTE</b> - The filenames must include the full directory relative to
	 * the project
	 * @throws IOException 
	 */
	public Flipbook(String[] fileNames) throws IOException
	{
		currentFrame = 0;
		frameCount = fileNames.length;
		
		lastFrameUpdateTime = System.currentTimeMillis();
		lastFrameUpdateCheck = lastFrameUpdateTime;
		pages = new Texture[fileNames.length];
		
		for (int i = 0; i < frameCount; i++)
		{
			String file = fileNames[i];
			String ext = null;

			//REGEX to match the filetype at the end of the file name
			String regexFileType = ".*\\.([a-zA-Z0-9]+)$";
			Pattern p = Pattern.compile(regexFileType);
			
			//Retrieve the file extension of the textureFile
			Matcher m = p.matcher(file);
			try
			{
				if (m.find())
					ext = m.group(1);
			
				if (ext != null)
				{
					ext = ext.toLowerCase();
					pages[i] = GTextureUtil.loadTextureProjectDir(file, ext);	
				}
				else
				{
					throw new IOException("Could not find file extension",
							new Throwable(String.format("File String at Index %d - \"%s\"", i, file)));
				}
			}
			catch (Exception e)
			{
				System.err.println("Bad File " + file);
				System.err.println(m.group(1));
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	public Texture getCurrentFrame()
	{
		updateFrame();
		return pages[currentFrame];
	}

	private void updateFrame() {
		lastFrameUpdateCheck = System.currentTimeMillis();
		long timeDelta = lastFrameUpdateCheck - lastFrameUpdateTime;
		if (timeDelta > frameDuration)
		{
			//The number of frames that should have been rendered since the last update
			int frameChange = (int) (timeDelta/frameDuration);
			
			currentFrame = (currentFrame + frameChange) % frameCount;
			
			//Store for next update
			lastFrameUpdateTime = lastFrameUpdateCheck;
		}
	}
	
	//STRETCH GOAL
//	public Flipbook(String[] fileNames, float[] frameLengths)
//	{
//		
//	}
}
