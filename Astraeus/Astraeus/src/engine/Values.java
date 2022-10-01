package engine;

import org.newdawn.slick.Color;

public interface Values 
{ 
	public static final String VERSION = "0.14.0";
	public static final String RELEASE_DATE = "3-8-22";

	// Image Scaling
	
	// Light -- > 24 x 24
	// Medium --> 36 x 36
	// Heavy --> 48 x 48
	// Assault --> 72 x 72
	// Base Ship --> 256 x 150  
	
	public final static int TEAM_ONE_ID = 0;		// left
	public final static int TEAM_TWO_ID = 1;		// right
	public final static int NEUTRAL_ID = 2;			// both sides can damage (asteroids)
	public final static int AMBIENT_ID = 3;			// background can't interact with
		
	public final static int LATENCY_SAMPLE_FREQUENCY = 240;
	public final static int LATENCY_GRACE_PERIOD = 480;
	
	public final static int TRANSITION_FADE_TIME = 20;
	
	public final static int SELECT_MIN_DIFFICULTY_RED = 40;
	public final static int SELECT_MAX_DIFFICULTY_GREEN = 300;
	public final static int SELECT_DIFFICULTY_RED_MAX = 180;
	public final static int SELECT_DIFFICULTY_GREEN_MAX = 130;
	
	// Public Constants

	
	final public static Color COLOR_SHIELD = new Color(100, 150, 255);
	final public static Color COLOR_PLATING = new Color(220, 200, 30);
	final public static Color COLOR_CAPACITY = new Color(196, 164, 132);
	final public static Color COLOR_SPEED = new Color(255, 255, 180);

	final public static Color COLOR_STRUCTURE = new Color(255, 80, 80);
	final public static Color COLOR_UTILITY = new Color(80, 255, 80);

	public final static int PLAYFIELD_WIDTH = 20000; 
	public final static int PLAYFIELD_HEIGHT = PLAYFIELD_WIDTH / 16 * 9; 

	public final static int FRAMES_PER_SECOND = 60;
	
	public final static int STARTING_MINERALS = 80;


	public final static float ACC = .001f; //.0004f before Josh's fix; 		
	public final static float SPEED = .05f; 		
	public final static float DODGE_FULL = 100;

	public static final float OUT_OF_BOUNDS_DAMAGE_PER_FRAME = .5f;





}
