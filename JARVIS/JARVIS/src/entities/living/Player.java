package entities.living;

import java.util.ArrayList;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import core.Engine;
import core.Values;
import entities.core.Coordinate;
import entities.core.Entity;
import entities.other.EItem;
import inventory.Inventory;
import inventory.Item;
import managers.ImageManager;

public class Player extends Living{	
	private Inventory inventory;
	
	// Player constructor
	private int inventorySelected;
	
	private SpriteSheet animation;
	private int aniFrame;
	private int frameCounter;
	
	public Player() 
	{
		super(Values.SpawnX, Values.SpawnY); 
		
		this.team = Team.Ally;
    
		aniFrame = 0;
		frameCounter = 0;
		
		animation = ImageManager.getSpriteSheet("mushSprite", 110, 128);
		sprite = animation.getSubImage(0, 0);
		
		this.inventory = new Inventory(this);
		
		this.height = 1.85f;
		this.width = 0.90f; 
		curHealth = 100;
		maxHealth = 100;
		healthRegen = true;
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
	
	// Render the player's use of items
	protected void renderOther(Graphics g) {
		Item item = inventory.getItem(inventorySelected);
		if(item == null) return;
		
		Image im = item.getImage();
		
		float scale = width / im.getWidth() / 1.5f;
		im.draw(
				game.displayManager.screenX(position.getX() + width / 4 * pastDirection), 
				game.displayManager.screenY(position.getY()),
				im.getWidth() * scale * pastDirection * Values.Pixels_Per_Block,
				im.getHeight() * scale * Values.Pixels_Per_Block
				);
	}
	
	// Return the inventory selected
	public int inventorySelected() { return this.inventorySelected; }
	public Item selectedItem() { return inventory.getItems()[inventorySelected]; }
	public Inventory getInventory() { return inventory; }

	public void changeInventorySlot(int inventorySelected) { this.inventorySelected = inventorySelected; }
	
	public void adjustInventorySlot(int change) {
		inventorySelected += change / 120;
		if (inventorySelected < 0) {
			inventorySelected = Inventory.Inventory_Size - 1;
		} else if (inventorySelected > Inventory.Inventory_Size - 1) {
			inventorySelected = 0;
		}
	}
	
	public void useItem(float x, float y, boolean primary) {
		Item item = selectedItem();
		
		if(item == null) return;
		
		if(primary) item.use(x, y);
		else item.use2(x, y);
	}
	public void dropItem() {
		inventory.drop(inventorySelected);
	}
	
	public void updateSprite() {
		if(Math.abs(this.ySpeed) > 2)
		{
			sprite = animation.getSubImage(2, 0);
		} else if(Math.abs(this.xSpeed) > 1) {
			frameCounter++;
			if(frameCounter%5 == 0) {
				if(aniFrame == 4) {
					aniFrame = 0;
				}else {
					aniFrame++;
					sprite = animation.getSubImage(aniFrame, 0);
				}
			}else if(frameCounter > 60) {
				frameCounter = 0;
			}
		}else {
			sprite = animation.getSubImage(0, 0);
		}
		
	}
	
	@Override
	public void update() {
		super.update();
		if(selectedItem() != null) selectedItem().update();
		inventory.filter();
	}
	
	// Overwritten Collisions Method
	protected void entityCollisions() {
		ArrayList<Entity> items = Engine.game.getEntities(Type.Item);
		
		for(Entity e: items) {
			EItem item = (EItem) e;
			
			// Can only pick up after 0.5 seconds of existing
			if(e.timeAlive() < 0.25f) continue;
			
			// Pick up item
			if(inventory.hasSpace(item.getID()) && this.entityCollision(e)) {
				inventory.pickUp(item);
				e.markForRemoval();
			}
		}
	}
	
	public void respawn() {
		position.setXPos(Values.SpawnX);
		position.setYPos(Values.SpawnY);
		curHealth = maxHealth;
		alive = true;
	}
	
	protected void drawSprite() {
		updateSprite();
		super.drawSprite();
	}
}