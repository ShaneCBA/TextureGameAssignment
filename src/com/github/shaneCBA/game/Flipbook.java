package com.github.shaneCBA.game;

import java.io.IOException;
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
	private int currentFrame, frameCount;
	
	//lastFrameUpdateTime is the time in which the frame was last changed
	private long lastFrameUpdateTime, lastFrameUpdateCheck;//keep current just in case
	
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
		
		for (int i = 0; i < frameCount; i++)
		{
			String file = fileNames[i];

			//REGEX to match the filetype at the end of the file name
			String regexFileType = ".*\\.([a-zA-Z]+)$";
			Pattern p = Pattern.compile(regexFileType);
			
			//Retrieve the file extension of the textureFile
			String ext = p.matcher(file).group();
			
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
			int frameChange = (int) (timeDelta/frameDuration);
			currentFrame = (currentFrame + frameChange) % frameCount;
		}
	}
	
	//STRETCH GOAL
//	public Flipbook(String[] fileNames, float[] frameLengths)
//	{
//		
//	}
}
