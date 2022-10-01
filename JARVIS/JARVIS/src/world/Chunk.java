package world;

import structures.Block;

public class Chunk{	
	// All blocks in the chunk
	private Block[][] blocks;
	
	// Chunk x-value (multiply by chunk size x to find the leftmost block in the chunk)
	private int chunkX;
	
	// Chunks will either be created through the WorldGen class or through FileLoading
	// We will NEVER generate a chunk through the constructor.
	public Chunk(int x, Block[][] blocks) {
		this.chunkX = x;
		this.blocks = blocks;
	}
	
	// Returns the chunk x
	public int getX(){
		return chunkX;
	}
	
	public Block[][] getBlocks() {
		return blocks;
	}
	
	// Update a block in the chunk
	public void updateBlock(int x, int y, Block newBlock) {
		blocks[x][y] = newBlock;
		
		System.out.println("Block updated");
	}
	
}