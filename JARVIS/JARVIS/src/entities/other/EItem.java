package entities.other;

import java.util.ArrayList;

import core.Engine;
import core.Values;
import entities.core.Entity;
import entities.core.Entity.Type;

public class EItem extends Entity{
	protected int count;
	protected int itemID;
	
	public EItem(float x, float y) {
		super(x,y);	
		this.count = 1;
		
		this.entityType = Type.Item;
		
		// Add Entity
		game.addEntityDirect(Type.Item, this);
	}
	
	public int getID() { return itemID; }
	public int getCount() { return count; }
	
	@Override
	public void update() {
		if(timeAlive() > Values.Item_Despawn_Time) {
			this.remove = true;
			return;
		}
		super.update();
	}
	
	@Override // Overwritten entity collision method
	protected void entityCollisions() {
		// Check collisions with other EItems
		ArrayList<Entity> items = game.getEntities(Type.Item); 
		
		int index = items.indexOf(this);
		for(int i = 0; i < items.size(); i++) {
			if(i == index) continue;
			EItem e2 = (EItem) items.get(i);
			
			if(e2.isMarked()) continue;
			
			if(this.itemID == e2.itemID && entityCollision(e2)) { // If not the same ID, no stacking will occur
				this.count += e2.count;
				e2.markForRemoval();
			}
		}
	}
}