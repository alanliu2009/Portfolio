package inventory;

import java.util.HashMap;

import entities.core.Coordinate;
import entities.living.Player;
import entities.other.EBlock;
import entities.other.EItem;

public class Inventory{
	public static final int Inventory_Size = 10;
	
	private Coordinate playerPos;
	private HashMap<Integer, Integer> idIndexMapping; // First entry: id, second entry: index
	
	private Item[] items;
	
	public Inventory(Player p) {
		this.playerPos = p.getPosition();
		this.items = new Item[Inventory_Size];
		this.idIndexMapping = new HashMap<Integer, Integer>();
		
		this.items[0] = Item.GetItem(-1, 0); // Gun 
		this.items[1] = Item.GetItem(-2, 0); // Pickaxe
	}
		
	public boolean hasItem(int id) { return idIndexMapping.containsKey(id); }
	
	public Item getItem(int index) { return items[index]; }
	
	public Item[] getItems() { return items; }
	public int getIndexMap(int id) { return idIndexMapping.get(id); }
	
	public void removeItem(int id) { items[idIndexMapping.get(id)].decreaseCount(1); }
	
	// Filter out items whose counts are 0
	public void filter() {
		for(int i = 0; i < Inventory.Inventory_Size; i++) {
			Item item = items[i];
			
			if(item == null) continue;
			if(item.getCount() == 0) {
				idIndexMapping.remove(item.getID());
				items[i] = null;
			}
		}
	}
	
	// Returns if the inventory has space for a given Integer ID
	public boolean hasSpace(Integer id) {
		if(idIndexMapping.containsKey(id)) return true;
		else if (idIndexMapping.size() < Inventory_Size + 1) return true;
		
		return false;
	}
	
	// Pick Up an EItem
	public void pickUp(EItem item) {
		int id = item.getID();
		int count = item.getCount();
		
		if(idIndexMapping.containsKey(id)) {
			int index = idIndexMapping.get(id);
			items[index].increaseCount(count);
		} else {
			for(int i = 0; i < Inventory_Size; i++) { // Find an empty spot
				if(items[i] == null) {
					idIndexMapping.put(id, i);
					items[i] = Item.GetItem(id, count);
					break;
				}
			}
		}
	}
	
	// Drop an Item by turning it into an EItem
	public void drop(int index) {
		// To drop, reduce count - if the count is 0, remove from hashmap and reset the array
		Item item = items[index];
		
		if(item == null || item.getID() < 0) return;
		else if(item.getCount() > 0) {
			item.decreaseCount(1);
			
			new EBlock(item.getID(), playerPos.getX(), playerPos.getY())
				.setSpeedY(7.5f);
		}	
	}
	
	public void swapElements(int index1, int index2) {
		Item item1 = items[index1];
		Item item2 = items[index2];
		if (item1 != null) {
			idIndexMapping.replace(item1.getID(), index2);
		}
		if (item2 != null) {
			idIndexMapping.replace(item2.getID(), index1);
		}
		
		items[index2] = item1;
		items[index1] = item2;
	}

}