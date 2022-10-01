package core;

import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class Values // this will be quite useful for organizing stuff later on
{	
	// Defines our block system
	final public static int Pixels_Per_Block = 30; // Determines the size of everything in game
	
	// Rendering Variables
	final public static HashMap<Integer, Color> BlockHash = new HashMap<Integer, Color>(); // Stores our Block - Color Combinations
	final public static int Render_Distance = 2; // Render distance
	
	final public static float CenterX = Engine.RESOLUTION_X / 2; // Player Center X
	final public static float CenterY = Engine.RESOLUTION_Y / 2; // Player Center Y
	final public static float EBlock_Size = 0.75f;
	
	// Background variables
	final public static int nightLength = 3200; //3200
	final public static int dayLength = 3200; //3200
	final public static int transitionLength = 1400; //1400
	final public static int totalLength = nightLength + dayLength + transitionLength * 2;
	
	// Player Variables
	final public static float Player_Reach = 10f;
	
	// Word Generation Variables
	final public static int Surface = 1024; // Surface Mapping
	
	// Spawning Variables
	final public static float SpawnX = Values.World_X_Size * Values.Chunk_Size_X / 2; // Player X Spawn
	final public static float SpawnY = Surface + 96f; // Player Y Spawn
	
	final public static float Spawn_Rate = 2f; // Entity Spawn Rate
	
	final public static float Entity_Despawn_Distance = (float) Math.sqrt(Engine.RESOLUTION_X * Engine.RESOLUTION_X + Engine.RESOLUTION_Y * Engine.RESOLUTION_Y);
	final public static float Item_Despawn_Time = 10; // If an entity exists for >__ seconds, unload it
	
	// World Size Variables
	final public static int World_X_Size = 128; // World Size (in chunks)
	final public static int Chunk_Size_X = 16; // Chunk X Size (in blocks)
	final public static int Chunk_Size_Y = 2048; // Chunk Y Size (in blocks)
	
	// Physics Variables
	final public static float Drag = 0.005f;
	final public static float Friction = 0.5f;
	final public static float Acceleration_of_Gravity = 0.75f;
	
	// GameState Storage
	public static int LastState = 0; // Previous GameState
	
	// File Save Paths
	// final public static String Sound_Path = "res/Sound"; // Directory where sound is stored
	final public static String Res_Folder = "res/";
	final public static String Save_Folder = "saves/"; // Directory where all world information will be saved
	final public static String Hash_File_Path = "src/settings/BlockHashing.txt"; // File where all block hashing will be located
}
