package gamestates;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.HashMap;
import java.util.function.Predicate;

import core.Engine;
import core.Values;
import entities.core.Entity;
import entities.core.Entity.Type;
import entities.living.*;
import entities.projectiles.Projectile;
import inventory.Inventory;
import inventory.Item;
import managers.DisplayManager;
import managers.KeyManager;
import managers.SoundManager;
import managers.SpawningManager;
import world.World;

public class Game extends BasicGameState {
	// Slick2D Variables
	private GameContainer gc;
	private StateBasedGame sbg;
	
	private int id;
	
	// Managers
	public KeyManager keyManager; // Manages keyDown presses
	public DisplayManager displayManager; // Manages the display / graphics in the game
	private SpawningManager spawningManager; // Manages spawning	
	
	// The Player
	private Player player;
	
	// Entities
	private HashMap<Type, ArrayList<Entity>> entities;
	private HashMap<Type, ArrayList<Entity>> newEntities;
	
	// The World
	private World world;
	
	//temporary variables for item swap
	private int index1, index2;
	
	// Constructor
	public Game(int id) { this.id = id; } 
	
	// Accessor Methods
	public int getID() { return id; }
	public GameContainer getGC() { return gc; }
	
	public DisplayManager getDisplayManager() { return displayManager; }
	
	public ArrayList<Entity> getEntities(Type type) { return entities.get(type); }
	public HashMap<Type, ArrayList<Entity>> getAllEntities(){ return entities; }
	public Player getPlayer() { return player; }
	public World getWorld() { return world; }
	
	// Mutator Methods
	public void addEntity(Type type, Entity e) { newEntities.get(type).add(e); }
	public void addEntityDirect(Type type, Entity e) { entities.get(type).add(e); }
	
	// Helper Methods
	public void respawn() { player.respawn(); }
	
	@Override /* Initializing */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{	
		// Saving the StateBasedGame
		this.sbg = sbg;
		this.gc = gc;	

		// Initialize the Entities Lists
		this.entities = new HashMap<Type, ArrayList<Entity>>();
		this.newEntities = new HashMap<Type, ArrayList<Entity>>();
		
		for(Type entityType: Type.values()) { 
			entities.put(entityType, new ArrayList<Entity>()); 
			newEntities.put(entityType, new ArrayList<Entity>());
		}
		
		// Add Player
		this.player = new Player();
				
		// Swap Index settings
		this.index1 = -1;
		this.index2 = -1; 
				
		// Initializing World
		this.world = new World(this);
		
		// Initializing Destroying and Spawning Behaviors
		this.spawningManager = new SpawningManager(this);
		
		// Initializing the Managers
		this.displayManager = new DisplayManager(gc.getGraphics(), player.getPosition());
		this.keyManager = new KeyManager(this);
	}

	@Override /* Render Everything in Game */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException { displayManager.render(g); }

	@Override /* Update - Update different behaviors of the game */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{	 
		// Check Control Presses
		checkControls();
		
		// Update The World
		world.update();
		
		// Spawning and Despawning Mechanics
		spawningManager.update();
		
		// Update All Entities, Including the Player
		for(ArrayList<Entity> list: entities.values()) {
			for(Entity e: list) {
				if(e.isMarked()) continue;
				e.update();
			}
    	}
				
		// Remove Entities Marked for Removal
		Predicate<Entity> filter = (Entity e) -> (e.isMarked());
		for(ArrayList<Entity> list: entities.values()) { list.removeIf(filter); }
		
		// Add All New Entities
		for(Entity.Type type: newEntities.keySet()) {
			entities.get(type).addAll(newEntities.get(type));
			newEntities.get(type).clear();
		}
		
		// Sends to pause if player died
		if (!player.isAlive()) {
			player.update();
			Values.LastState = Engine.Game_ID;
			sbg.enterState(Engine.Pause_ID);
		}
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		SoundManager.playBackgroundMusic("Morning"); // Begin game background music
	}
	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) {}

	/* Controls */
	private void checkControls() {
		checkKeysDown();
		checkCursorDown();
	}
	
	// Key Mappings
	private void checkKeysDown() { KeyManager.keyList.stream().filter(keyManager).forEach(keyManager::keyDown); }
	@Override
	public void keyPressed(int key, char c)
	{
  		switch(key) {
  			case Input.KEY_ESCAPE: // Exit the game
  				gc.exit();
  				break;

  			case Input.KEY_SPACE: // Jump Key Mapping (Space & W)
  			case Input.KEY_W:{
  				player.jump(20f);
  				break;
  			}
  			
  			case Input.KEY_P: // Pause Key Binding
  				Values.LastState = Engine.Game_ID;
  				sbg.enterState(Engine.Pause_ID);
  				break;
  			case Input.KEY_BACKSLASH: // Debug Key Binding
  				Values.LastState = Engine.Game_ID;
  				sbg.enterState(Engine.Debug_ID);
  				break;
  			
  			case Input.KEY_G: // Drop Item
  				player.dropItem();
  				break;
  			
  			// All Inventory Key Bindings
  			case Input.KEY_1:
  			case Input.KEY_2:
  			case Input.KEY_3:
  			case Input.KEY_4:
  			case Input.KEY_5:
  			case Input.KEY_6:
  			case Input.KEY_7:
  			case Input.KEY_8:
  			case Input.KEY_9:
  			case Input.KEY_0:{
  				player.changeInventorySlot(key - 2);
  				break;
  			}
  			
  		}
  		
	}

	// Mouse Mappings
	public void mouseWheelMoved(int change) { player.adjustInventorySlot(-change); }
	private boolean inInventory(float x, float y) {
		final float BAR_X = (float) (0.050208333333 * getGC().getWidth());
		final float BAR_Y = (float) (0.03703703703 * getGC().getHeight());
		final float BAR_WIDTH = (float) ((Engine.game.getGC().getWidth()/2) - (0.15625 * Engine.game.getGC().getWidth()));
		final float BAR_HEIGHT = (float) ((60f / 1080f) * Engine.game.getGC().getHeight());
		
		return x > BAR_X && x < BAR_X + BAR_WIDTH && y > BAR_Y && y < BAR_Y + BAR_HEIGHT;
	}
	private void checkCursorDown() {
		Input input = gc.getInput();
		
		// Do not do anything if mouse is in inventory
		float x = input.getAbsoluteMouseX();
		float y = input.getAbsoluteMouseY();
		if(inInventory(x,y)) return;
		
		
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			player.useItem(
					displayManager.gameX(input.getAbsoluteMouseX()),
					displayManager.gameY(input.getAbsoluteMouseY()),
					true 
					);
		} else if(input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)){
			player.useItem(
					displayManager.gameX(input.getAbsoluteMouseX()),
					displayManager.gameY(input.getAbsoluteMouseY()),
					false
					);
			
		}
	}

	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			checkItemSwap(x, y);
		}
	}
	
	public float[] mousePosition()
	{
		Input input = gc.getInput();
		float[] temp = new float[2];
		
		temp[0] = input.getAbsoluteMouseX();
		temp[1] = input.getAbsoluteMouseY();
		
		return(temp);
	}
			
	public void checkItemSwap(float x, float y) {
		final float BAR_X = (float) (0.050208333333 * getGC().getWidth());
		final float BAR_Y = (float) (0.03703703703 * getGC().getHeight());
		final float BAR_WIDTH = (float) ((Engine.game.getGC().getWidth()/2) - (0.15625 * Engine.game.getGC().getWidth()));
		final float BAR_HEIGHT = (float) ((60f / 1080f) * Engine.game.getGC().getHeight());
		// Draw every item in the player's inventory
		final float boxSize = BAR_WIDTH / (float) Inventory.Inventory_Size;
		
		if (inInventory(x,y)) {
			if (index1 == -1) {
				//set index1 to what is in the bar
				index1 = (int) ((x - BAR_X) / boxSize);
			} else {
				//set index2 to what is in the bar
				index2 = (int) ((x - BAR_X) / boxSize);
				player.getInventory().swapElements(index1, index2);
				index1 = -1;
				index2 = -1;
			}
		} else {
			index1 = -1;
			index2 = -1;
		}
	}

}
