package core;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockSettings {
	final public static Map<Integer, BlockSettings> BlockHashing = Stream.of(
			new BlockSettings(0, -1, true, 0, 0f), // ID 0 - Air,
			new BlockSettings(1, 1, false, 100, 0.2f), // ID 1 - Dirt
			new BlockSettings(2, 0, false, 100, 0.2f), // ID 2 - Grass
			new BlockSettings(3, 2, false, 200, 1f), // ID 3 - Stone
			new BlockSettings(4, 3, false, 250, 1.2f), // ID 4 - Coal
			new BlockSettings(5, 4, false, 300, 1.7f), // ID 5 - Gold
			new BlockSettings(6, 5, false, 400, 2.5f), // ID 6 - Diamond
			new BlockSettings(7, 6, false, 100, 0.2f), // ID 7 - Sand
			new BlockSettings(8, 7, false, 200, 1f), // ID 8 - Sandstone
			new BlockSettings(9, 8, false, 100, 0.2f), // ID 9 - Snow
			new BlockSettings(10, 9, false, 200, 1f) // ID 10 - Ice
		).collect(Collectors.toMap(BlockSettings::getID, BlockSettings::getHash));
	
	// Instance Variables for the BlockHash
	private int id; // ID of the Block
	
	private int spriteLocation; // Location of the Image in the SpriteSheet
	
	private boolean passable;
	
	private int baseDurability; // Base Durability of the Block
	private float strengthScaling; // Block Scaling (for the gun)
	
	// Constructor
	public BlockSettings(int id, int location, boolean passable, int baseDurability, float strengthScaling) {
		this.id = id;
		
		this.spriteLocation = location;
		
		this.passable = passable;
		
		this.baseDurability = baseDurability;
		this.strengthScaling = strengthScaling;
	}
	
	// Methods for Initializing the Map
	private BlockSettings getHash() { return this; }
	private int getID() { return id; }
	
	// Methods to Obtain the Values
	public static boolean hasBlock(int id) { return BlockHashing.containsKey(id); }
	
	public static int getLocation(int id) { return BlockHashing.get(id).spriteLocation; }
	
	public static boolean isPassable(int id) { return BlockHashing.get(id).passable; }

	public static int getBaseDurability(int id) { return BlockHashing.get(id).baseDurability; }
	public static float getStrengthScaling(int id) { return BlockHashing.get(id).strengthScaling; }	
}