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

	ArrayList<GShape> drawingArtObjects;
	
	Movable player;
	Movable enemy;
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

		Flipbook[] playerFlipbooks = FileLoadingUtil.readSprite("/Options/sprite.options", "Player");
		//Players z-axis is zero, as everything in the world should be placed relative to the player
		player = new Movable(new float[] {1*Tile.TILESIZE, 2*Tile.TILESIZE, 0},
				new float[] {2*Tile.TILESIZE,2*Tile.TILESIZE},
				new float[] {10f, 3f,9f+1*Tile.TILESIZE,1.5f*Tile.TILESIZE},
				playerFlipbooks);
		
		
		Flipbook[] enemyFlipbooks = FileLoadingUtil.readSprite("/Options/sprite.options", "Enemy");
		enemy = new Movable(new float[] {1*Tile.TILESIZE, 2*Tile.TILESIZE, 0},
				new float[] {2*Tile.TILESIZE,2*Tile.TILESIZE},
				new float[] {10f, 3f,9f+1*Tile.TILESIZE,1.5f*Tile.TILESIZE},
				enemyFlipbooks);
		
		
		ArrayList<Level> levels = FileLoadingUtil.readWorld("/Options/world.options");
		levels.get(0).addEntity(player);
		levels.get(0).addEntity(enemy);
		
		world = World.getInstance();
		world.loadLevels(levels);
		world.setPlayer(player); 
		world.setEnemy(enemy);
		
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
		glu.gluOrtho2D(0, 2*GL_Width, 0, 2*GL_Height); // canvas

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW); // specify coordinates
		gl.glLoadIdentity(); // reset

	}

	public void setToOrthoView()
	{
		GLU glu = new GLU();
//		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
//		gl.glLoadIdentity(); // reset projection matrix
	}

	public void setToPerspectiveView(GL2 gl)
	{
		GLU glu = new GLU();
		gl.glViewport(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		float calc_aspect = DRAWING_WIDTH / DRAWING_HEIGHT;
		glu.gluPerspective(90f, calc_aspect, 1.0, 1000.0);


		gl.glMatrixMode(GL_MODELVIEW); // specify coordinates
		gl.glLoadIdentity(); // reset
	}

	void init3DProjectionMatrix(final GL2 gl) {
		

		float width = GLUTCanvas.CANVAS_WIDTH;
		float height = GLUTCanvas.CANVAS_HEIGHT;
		if (height < 1)
			height = 1;
		// this is the new aspect ratio based on the resize
		float calc_aspect = (float) width / (float) height;
		GLU glu = new GLU();

		// gl.glViewport(0, 0, WINDOW_W, WINDOW_H);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();


		glu.gluPerspective(90.0f, calc_aspect, 1.0, 40.0);

		// camera position, looking at location, axis
		// glu.gluLookAt(0, 0, 1.0f, 1.5f, 0, -12f, 0, 1, 0);

		// gl.glClearDepth(20.0f); // set clear depth value to farthest
		gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL2.GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
		gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // background color: white

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
		
		setToPerspectiveView(gl);

		gl.glClearColor(.90f, .90f, 1.0f, 1.0f); // color used to clean the canvas
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the canvas with color
		gl.glClearDepth(20.0f); // set clear depth value to farthest

		gl.glTranslatef(-world.getCurrentLevel().getWidth()/2, -world.getCurrentLevel().getHeight()/2, -175);
		for (GShape artObject : drawingArtObjects) {
			artObject.render(gl);
		}

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