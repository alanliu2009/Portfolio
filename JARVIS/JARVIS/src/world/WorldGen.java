package world;

import java.util.ArrayList;

import core.Engine;
import core.Values;
import gamestates.Game;
import managers.FileManager;
import structures.Block;
import structures.Tree;
import support.SimplexNoise;
import support.Utility;

public class WorldGen extends Thread
{
	// World-Specific Variables 
	private SimplexNoise noise; //noise for the rest
	private String worldName;
	private int desertStart;
	private int desertEnd;
	private int tundraStart;
	private int tundraEnd;
	private float mountainsStart;
	private float mountainsEnd;
	
	// Variables used in world generation
	
	public WorldGen(String worldName, int seed) 
	{
		this.worldName = worldName;
		
		desertStart = Engine.game.getWorld().getDesertStart();
		desertEnd = Engine.game.getWorld().getDesertEnd();
		tundraStart = Engine.game.getWorld().getTundraStart();
		tundraEnd = Engine.game.getWorld().getTundraEnd();
		
		mountainsStart = desertEnd;
		mountainsEnd = mountainsStart + 5;
		
		noise = new SimplexNoise(seed); // Later will be used with the noise parameter for custom seeds
	}
	
	public void run() 
	{
		generateWorld();
	}
	/*
	 * Method to generate an entire world from scratch
	 * 
	 * Use FileLoader.SaveChunk() to save chunks to file.
	 */
	public void generateWorld() {
		// Create the folders for the world
		FileManager.createWorldFolders(worldName);
		
		// For every chunk 1 - World.World_X_Size, generate it using the generate() function and save it to file.
		for(int chunkX = 0; chunkX < Values.World_X_Size; chunkX++) {
			Block[][] blocks = new Block[Values.Chunk_Size_X][Values.Chunk_Size_Y];
			
			blocks = generate(chunkX * Values.Chunk_Size_X, blocks);
			FileManager.SaveChunk(worldName, new Chunk(chunkX, blocks));
			
			// Increment the loading bar
			Engine.loading.finishedTask();
		}
		
		biomes();
		Engine.loading.finishedTask();
		
		Engine.game.getWorld().clearChunks();
	}
	
	private void biomes()
	{		
		for(int chunkX = desertStart; chunkX < desertEnd; chunkX++)
		{
			//System.out.println(chunkX);
			Block[][] blocks = new Block[Values.Chunk_Size_X][Values.Chunk_Size_Y];
			
			if(chunkX == desertStart)
			{
				blocks = BiomeGens.desertGen(FileManager.LoadChunk(worldName, chunkX).getBlocks(), -1);
			} 
			else if(chunkX == desertEnd - 1)
			{
				blocks = BiomeGens.desertGen(FileManager.LoadChunk(worldName, chunkX).getBlocks(), 1);
			} 
			else
			{
				blocks = BiomeGens.desertGen(FileManager.LoadChunk(worldName, chunkX).getBlocks(), 0);
			}
			
			FileManager.SaveChunk(worldName, new Chunk(chunkX, blocks));
		}
		
		for(int chunkX = tundraStart; chunkX < tundraEnd; chunkX++)
		{
			//System.out.println(chunkX);
			Block[][] blocks = new Block[Values.Chunk_Size_X][Values.Chunk_Size_Y];
			
			if(chunkX == tundraStart)
			{
				blocks = BiomeGens.tundraGen(FileManager.LoadChunk(worldName, chunkX).getBlocks(), -1);
			} 
			else if(chunkX == tundraEnd - 1)
			{
				blocks = BiomeGens.tundraGen(FileManager.LoadChunk(worldName, chunkX).getBlocks(), 1);
			} 
			else
			{
				blocks = BiomeGens.tundraGen(FileManager.LoadChunk(worldName, chunkX).getBlocks(), 0);
			}
			
			FileManager.SaveChunk(worldName, new Chunk(chunkX, blocks));
		}
	}
	
	
	
	// Returns a 2D array of blocks for a chunk.
	private Block[][] generate(int x, Block[][] blocks)
	{
		float smoothness = 1f / 32f;
		float terrainMultiplier = 32;
		float mountainsMultiplier = 2;
		
		double[] terrain = new double[Values.Chunk_Size_X];
		
		for(int i = 0; i < Values.Chunk_Size_X; i++) 
		{
			
			double temp = noise.eval((x + i) * smoothness, 0);
			
			terrain[i] = temp * terrainMultiplier + Values.Surface;
			
			
			if(x / Values.Chunk_Size_X > mountainsStart 
					&& x / Values.Chunk_Size_X < mountainsEnd) 	
			{
				terrain[i] += noise.eval((x + i) * smoothness * 1.7f, 0) * terrainMultiplier * mountainsMultiplier;
			} 
			else if(x / Values.Chunk_Size_X == mountainsStart)
			{
				terrain[i] += (noise.eval((x + i) * smoothness * 1.7f, 0) 
						* terrainMultiplier * mountainsMultiplier) * (float)i / (float)Values.Chunk_Size_X;
			}
			else if(x / Values.Chunk_Size_X == mountainsEnd)
			{
				terrain[i] += (noise.eval((x + i) * smoothness * 1.7f, 0) 
						* terrainMultiplier * mountainsMultiplier) * (1 - ((float)i / (float)Values.Chunk_Size_X));
			}
			
			
			for(int j = 0; j < Values.Chunk_Size_Y; j++)
			{
				if(j < terrain[i] && j - Values.Surface >= 60 && j
						> terrain[i] - Utility.random(1, 4))
				{	
					blocks[i][j] = new Block(9);
				}
				else if(j < terrain[i] - (Math.random() * 2 + 5) || (
						j - Values.Surface >= 35 + Utility.random(3) && j < terrain[i])) //mountains is the || part
				{
					blocks[i][j] = new Block(3); //stone
				}
				else if(j < terrain[i])
				{
					blocks[i][j] = new Block(1); //dirt;
				} 
				else
				{
					blocks[i][j] = new Block(0); //air
				}			
			}
		}
		
		
		populate(blocks, x);
		
		return(blocks);
	}
	
	private Block[][] populate(Block[][] blocks, int x) //basic grasslands generation
	{

		float[][] caves = new float[Values.Chunk_Size_X][Values.Chunk_Size_Y];
		
		float[][] ores = new float[Values.Chunk_Size_X][Values.Chunk_Size_Y];
		
		for(int i = 0; i < Values.Chunk_Size_X; i++)
		{
			for(int j = 0; j < Values.Chunk_Size_Y; j++)
			{
				float smoothGen = 0.1f; //adjust to smooth generation;
				
				caves[i][j] = (float) noise.eval((i + x) * smoothGen * .2, j * smoothGen * .5);
				caves[i][j] *= caves[i][j];
				caves[i][j] *= caves[i][j];
				
				ores[i][j] = (float) noise.eval((i + x) * smoothGen, j * smoothGen);
				
				if(blocks[i][j].getID() == 3)
				{
					/*
					 Note about the noise: make the threshold closer to 1 (array[i][j] >= x) for smaller veins
					 Make smoothGen larger along with adjusting the threshold to be higher to make the ores more rare
					 
					 trying to get it to work with just one noise pattern to save power
					 */
					if(ores[(i + 10) % Values.Chunk_Size_X][Math.abs(Values.Chunk_Size_Y - j - 1) ] >= 0.88 && j < 750) //diamonds?
					{
						//System.out.println(j * -1 % Values.Chunk_Size_Y + ", " + j % Values.Chunk_Size_Y);
						blocks[i][j].setID(6);
					} 
					else if(ores[i][j] >= .85 && j < 900) //gold for now
					{
						blocks[i][j].setID(5);
					} 
					else if(ores[(i + 5) % Values.Chunk_Size_X][(j + 50) % Values.Chunk_Size_Y] >= 0.75) //coal or some other ore
					{
						blocks[i][j].setID(4);
					} 
				}	
				
//				if(caves[i][j] > 0.2) //rudimentary caves
//				{
//					blocks[i][j].setID(0);
//				}
				
				if(blocks[i][j].getID() == 1) //populating dirt
				{					
					if(adjacentTo(i, j, 0, blocks) > 0) //grasssss
					{
						blocks[i][j].setID(2);
					}
				}
			}
		}
		
		return(blocks);
	}
	
	
	//utility methods below
	public static boolean adjacentTo(int x, int y, char direction, int id, Block[][] blocks)
	{
		if(direction == 'n')
		{
			if(inBounds(x, y + 1))
			{
				return(blocks[x][y + 1].getID() == id);
			}
		} 
		else if(direction == 's')
		{
			if(inBounds(x, y - 1))
			{
				return(blocks[x][y - 1].getID() == id);
			}
		}
		else if(direction == 'e')
		{
			if(inBounds(x + 1, y))
			{
				return(blocks[x + 1][y].getID() == id);
			}
		}
		else if(direction == 'w')
		{
			if(inBounds(x - 1, y))
			{
				return(blocks[x - 1][y].getID() == id);
			}
		}
		
		return(false);
	}
	
	public static int adjacentTo(int x, int y, int id, Block[][] blocks) //method for seeing if a certain kind of block is next to it
	//still need to fix
	{
		int count = 0;
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; j < 2; j++)
			{
				if(inBounds(x + i, y + j)) // hmmm
				{
					if(blocks[x + i][y + j].getID() == id) 
					{
						count++;					
					}
				}
			}
		}
		
		return(count);
	}
	
	public static boolean inBounds(int x, int y) //to avoid the out of bounds error
	{
		return(x > -1 && x < Values.Chunk_Size_X
		&& y > -1 && y < Values.Chunk_Size_Y);
	}
}