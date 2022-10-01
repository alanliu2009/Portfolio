package world;

import core.Values;
import structures.Block;

public class BiomeGens //just holds all the biome generation, for organization purposes
{
	public static Block[][] desertGen(Block[][] inputBlocks, int edge)
	{
		Block[][] blocks = inputBlocks;
		int elevation = 300;
		int block1 = 7;
		int block2 = 8;
		
		switch(edge) 
		{
			case(0): 
			{
				for(int i = 0; i < Values.Chunk_Size_X; i++)
				{
					for(int j = 0; j < Values.Chunk_Size_Y; j++)
					{
						setBlocks(blocks, i, j, block1, block2, elevation);
					}
				}	
			}
			case(1) :
			{
				for(int j = 0; j < Values.Chunk_Size_Y; j++)
				{
					for(int i = 0; i < Values.Chunk_Size_X * Math.random(); i++)
					{
						setBlocks(blocks, i, j, block1, block2, elevation);
					}
				}
					
			}
			case(-1) :
			{
				for(int j = 0; j < Values.Chunk_Size_Y; j++)
				{
					for(int i = 0; i < Values.Chunk_Size_X * Math.random(); i++)
					{
						setBlocks(blocks, (Values.Chunk_Size_X - 1) - i, j, block1, block2, elevation);
					}
				}
			}
		}
		
		return(blocks);
	}
	
	public static Block[][] tundraGen(Block[][] inputBlocks, int edge)
	{
		Block[][] blocks = inputBlocks;
		int elevation = 200;
		int block1 = 9;
		int block2 = 10;
		
		switch(edge)
		{
			case(0) : 
			{
				for(int i = 0; i < Values.Chunk_Size_X; i++)
				{
					for(int j = 0; j < Values.Chunk_Size_Y; j++)
					{
						setBlocks(blocks, i, j, block1, block2, elevation);
					}
				}	
			}
			case(1) :
			{
				for(int j = 0; j < Values.Chunk_Size_Y; j++)
				{
					for(int i = 0; i < Values.Chunk_Size_X * Math.random(); i++)
					{
						setBlocks(blocks, i, j, block1, block2, elevation);
					}
				}
					
			}
			case(-1) :
			{
				for(int j = 0; j < Values.Chunk_Size_Y; j++)
				{
					for(int i = 0; i < Values.Chunk_Size_X * Math.random(); i++)
					{
						setBlocks(blocks, (Values.Chunk_Size_X - 1) - i, j, block1, block2, elevation);
					}
				}
			}
		}
		
		return(blocks);
	}
	
	private Block[][] structures(Block[][] blocks) //oh boy
	{
		for(int i = 0; i < Values.Chunk_Size_X; i++)
		{
			for(int j = 0; j < Values.Chunk_Size_Y; i++)
			{
				if(blocks[i][j].getID() == 2 && WorldGen.adjacentTo(i, j, 'n', 0, blocks) 
						&& WorldGen.adjacentTo(i, j + 1, 'e', 0, blocks) && WorldGen.adjacentTo(i, j + 1, 'w', 0, blocks)) // tree generation bruhhhh
				{
					if(i % (4 + (int)Math.random() * 3) == 0)
					{
						blocks[i][j + 1].setID(3);
					}
					
				}	
			}		
		}
		return(blocks);
	}
	
	public static void setBlocks(Block[][] inputBlocks, int i, int j, int id1, int id2, int elevation)
	{
		Block[][] blocks = inputBlocks;
		
		if(j > elevation)
		{
			switch(blocks[i][j].getID())
			{
				case(1): blocks[i][j].setID(id1); break;
				case(2): blocks[i][j].setID(id1); break;
				case(3): blocks[i][j].setID(id2);
			}
		}
		
	}
}
