package inventory.items;

import org.newdawn.slick.Image;

import core.Engine;
import core.Values;
import inventory.Item;
import structures.Block;

//Blocks that are items
public class IBlock extends Item{
	public IBlock(int id, int count) { 
		super(id, count);
		
		this.sprite = game.displayManager.getBlockSprite(id);
	}
	
	@Override
	public void use(float x, float y) {
		try {
			int blockX = (int) x;
			int blockY = (int) y;
			
			Block[][] blocks = game.getWorld().getChunk(blockX / Values.Chunk_Size_X).getBlocks();
			
			if(blocks[blockX % Values.Chunk_Size_X][blockY].getID() == 0) {
				blocks[blockX % Values.Chunk_Size_X][blockY] = new Block(id);
				this.count--;
			}
		} catch(Exception e){}
	}
}