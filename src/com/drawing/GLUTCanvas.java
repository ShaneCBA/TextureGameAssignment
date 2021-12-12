package com.drawing;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.github.shaneCBA.game.DebugUtil;
import com.github.shaneCBA.game.FileLoadingUtil;
import com.github.shaneCBA.game.Flipbook;
import com.github.shaneCBA.game.Keyboard;
import com.github.shaneCBA.game.Level;
import com.github.shaneCBA.game.Movable;
import com.github.shaneCBA.game.PlayerController;
import com.github.shaneCBA.game.Tile;
import com.github.shaneCBA.game.World;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

// import com.jogamp.opengl.util.gl2.GLUT;

/*
* JOGL 2.0 Program Template (GLCanvas) This is a "Component" which can be added
* into a top-level "Container". It also handles the OpenGL events to render
* graphics.
*/
@SuppressWarnings("serial")
class GLUTCanvas extends GLCanvas implements GLEventListener {

	public static int CANVAS_WIDTH = 1100; // width of the drawable
	public static int CANVAS_HEIGHT = 700; // height of the drawable

	public static final float DRAWING_WIDTH = 550f, DRAWING_HEIGHT = 350f;
	public static float GL_Width, GL_Height;
	// Setup OpenGL Graphics Renderer
//	GDrawOrigin myOrigin;
	GKeyBoard keyBoard;
	GMouse mouse;

	GQuad myQuad;

	// GTriangle learnTriangle;

	GPatch myPatch;
	GSpriteKey spriteCharacter;

	ArrayList<GShape> drawingArtObjects;
	ArrayList<GCRect> collisionRects;
	
	Movable player;
	World world;
	Keyboard keyboard;
	PlayerController playerController;

	/** Constructor to setup the GUI for this Component */
	public GLUTCanvas(GLCapabilities capabilities, GKeyBoard kb, GMouse mouse) {

		super(capabilities);

		keyboard = Keyboard.getInstance();

		// creating a canvas for drawing

		this.addGLEventListener(this);
		this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.keyBoard = kb;
		this.mouse = mouse;
	}

	// ------ Implement methods declared in GLEventListener ------

	/**
	 * 
	 * Called back immediately after the OpenGL context is initialized. Can be used
	 * to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context

		// ----- Your OpenGL initialization code here -----
		GLU glu = new GLU();

		GL_Width = DRAWING_WIDTH / 2.0f;
		GL_Height = DRAWING_HEIGHT / 2.0f;
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		
		glu.gluOrtho2D(0, 2*GL_Width, 0, 2*GL_Height); // canvas

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW); // specify coordinates
		gl.glLoadIdentity(); // reset

		gl.glClearColor(.90f, .90f, 1.0f, 1.0f); // color used to clean the canvas
		gl.glColor3f(1.0f, 1.0f, 1.0f); // drawing color
		// gl.glEnable(GL2.GL_DEPTH_TEST);
		// gl.glDepthFunc(GL2.GL_LESS);

		Flipbook[] flipbooks = FileLoadingUtil.readSprite("/World/sprite.data", "Player");
		//Players z-axis is zero, as everything in the world should be placed relative to the player
		player = new Movable(new float[] {1*Tile.TILESIZE, 2*Tile.TILESIZE, 0},
				new float[] {2*Tile.TILESIZE,2*Tile.TILESIZE},
				new float[] {10f, 3f,9f+1*Tile.TILESIZE,1.5f*Tile.TILESIZE},
				flipbooks);
		
		int[][] tileInts= FileLoadingUtil.readOldWorld("/World/demo.wd");
		
		Level level1 = new Level(tileInts, tileInts[0].length, tileInts.length,1f*Tile.TILESIZE, 2f*Tile.TILESIZE);
		
		tileInts= FileLoadingUtil.readOldWorld("/World/demo2.wd");
		Level level2 = new Level(tileInts, tileInts[0].length, tileInts.length,1*Tile.TILESIZE, 2*Tile.TILESIZE);
		
		level1.addEntity(player);

//		playerController = new PlayerController(player, level1);//move to
		
		world = World.getInstance();
		world.loadLevel(level1);
		world.loadLevel(level2);
		world.setPlayer(player); 
		
		// adding them all in the arrayList
		drawingArtObjects = new ArrayList<GShape>();
		drawingArtObjects.add(world);
	}


	/**
	 * Call-back handler for window re-size event. Also called when the drawable is
	 * first set to visible.
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context

		// Have to add this in order for the canvas to properly scale in the window
		// Found at https://forum.jogamp.org/canvas-not-filling-frame-td4040092.html
		double dpiScalingFactorX = ((Graphics2D) getGraphics()).getTransform().getScaleX();
		double dpiScalingFactorY = ((Graphics2D) getGraphics()).getTransform().getScaleY();
		width = (int) (width / dpiScalingFactorX);
		height = (int) (height / dpiScalingFactorY);

		if (DrawWindow.DEBUG_OUTPUT)
			System.out.println(width + ":" + height);

		GLUTCanvas.CANVAS_HEIGHT = height;
		GLUTCanvas.CANVAS_WIDTH = width;

		// we want this aspect ratio in our drawing
		float target_aspect = DRAWING_WIDTH / DRAWING_HEIGHT;

		if (height < 1)
			height = 1;
		// this is the new aspect ratio based on the resize
		float calc_aspect = (float) width / (float) height;

		float aspect = calc_aspect / target_aspect;

		if (calc_aspect >= target_aspect) {
			GL_Width = DRAWING_WIDTH / 2.0f * aspect;
			GL_Height = DRAWING_HEIGHT / 2.0f;
		} else {
			GL_Width = DRAWING_WIDTH / 2.0f;
			GL_Height = DRAWING_HEIGHT / 2.0f / aspect;
		}


		GLU glu = new GLU();
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		// gl.glOrtho(-GL_Width, GL_Width, -GL_Height, GL_Height, -2.0f, 2.0f); // 2D
//		glu.gluOrtho2D(-GL_Width, GL_Width, -GL_Height, GL_Height); // canvas
		glu.gluOrtho2D(0, 2*GL_Width, 0, 2*GL_Height); // canvas

//		 gl.glViewport(0, 0, (int) GL_Width * 2, -(int) GL_Height * 2);
//		gl.glViewport(-(int) GL_Width, (int) GL_Width, -(int) GL_Height, (int) GL_Height);

		// gl.glEnable(GL2.GL_DEPTH_TEST);
		// gl.glDepthFunc(GL2.GL_LESS);
		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW); // specify coordinates
		gl.glLoadIdentity(); // reset

	}

	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		gl.glClearColor(.90f, .90f, 1.0f, 1.0f); // color used to clean the canvas
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the canvas with color
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glLoadIdentity(); // reset the model-view matrix
		
		
		for (GShape artObject : drawingArtObjects) {
			artObject.render(gl);
		}
		DebugUtil.debugSquare(gl, 0, 0);

		gl.glFlush();
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as
	 * buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	/**
	 * 
	 */
	public void processMouseEvents()
	{
	}

}