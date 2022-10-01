package inventory;

import org.newdawn.slick.Image;

import core.Engine;
import gamestates.Game;
import inventory.items.Gun;
import inventory.items.IBlock;
import inventory.items.Tool;
import managers.ImageManager;

public class Item{
	// Variables for Easier Accessing
	protected static Game game = Engine.game;
	
	// Item Variables
	protected int id;
	protected int count;
	
	protected Image sprite;
		
	// Item Constructor
	public Item(int id, int count) {
		this.id = id;
		this.count = count;
		
		// Sprite will by default be a placeholder
		this.sprite = ImageManager.getPlaceholder();
	}

	// Accessor Methods
	public int getID() { return id; }
	public int getCount() { return count; }
	public Image getImage() { return sprite; }
	
	// Mutator Methods
	public void setID(int id) { this.id = id; }
	public void setCount(int count) { this.count = count; }
	public void increaseCount(int i) { count += i; }
	public void decreaseCount(int i) { count -= i; }
	
	/*
	 * Main Method All Items Will Use
	 * Parameters: Absolute Coordinates of the Mouse in Game
	 */
	public void use(float x, float y) {} // Left click main functionality
	public void use2(float x, float y) {} // Right click secondary functionality
	public void update() {} //for tools
	
	// Returns some item based on a provided ID and Count
	public static Item GetItem(int id, int count) {
		// Positive Numbers = IBlock (Item Block)
		if(id > 0) { return new IBlock(id, count); } 
		else {
			switch(id) {
				case -1: // Id: -1 = Gun
					return new Gun();
				case -2: // Id: -2 = Tool
					return new Tool();
			}
		} 
		return null;
	}
}