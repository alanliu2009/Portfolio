package managers;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

import core.Values;
import structures.Block;
import world.Chunk;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileManager {
	
	// Creates all directories / subdirectories for our world
	public static boolean createWorldFolders(String name) {
		String path = Values.Save_Folder + name;
		if(new File(path).mkdir()) {
			new File(path + "/entities").mkdir(); // Entities folder
			new File(path + "/player").mkdir(); // Player folder
			new File(path + "/chunks").mkdir(); // Chunks folder
			
			return true;
		} 
		else return false;
	}
	
	// Return a list of all worlds
	public static String[] getWorldList() {
		File worldFolder = new File(Values.Save_Folder);
		
		return worldFolder.list();
	}
	
	
	// Saves a chunk for a given world name
	public static void SaveChunk(String worldName, Chunk c) {
		try {
			FileWriter writer = new FileWriter(new File(Values.Save_Folder + worldName + "/chunks/" + c.getX() + ".chunk"));
			
			Block[][] blocks = c.getBlocks();
			for(int y = Values.Chunk_Size_Y - 1; y >= 0 ; y--) {
				for(int x = 0; x < Values.Chunk_Size_X; x++) {
					String id = Integer.toString(blocks[x][y].getID());
					
					writer.write(id);
					writer.write(' ');
				}
				writer.write("\n");
			}
			
			writer.close();
		} catch(IOException e) {
			System.out.println("There was an error in saving chunk " + c.getX());
		}
		
	}
	
	// Load a chunk for a given world name
	public static Chunk LoadChunk(String worldName, int chunkX) {
		// Code for chunk retrieval
		Block[][] blocks = new Block[Values.Chunk_Size_X][Values.Chunk_Size_Y];
		
		File chunkFile = new File(Values.Save_Folder + worldName + "/chunks/" + chunkX + ".chunk");
		if(!chunkFile.exists()) return null; // Exception code in case the chunk doesn't exist
		
		try { // Space is ASCII 32, Newline is ASCII 10
			FileReader reader = new FileReader(chunkFile);
			
			int x = 0, y = Values.Chunk_Size_Y - 1;
			String id = "";
			
			int data = reader.read();
			while(data != -1) { // When data == -1, the file reading is complete
				if(data == 13) {} // Skip the ASCII character for 13 
				else if(data == 10) {
					x = 0;
					y -= 1;
					
					id = "";
				} else if(data == 32) {
					blocks[x][y] = new Block(Integer.parseInt(id));
					
					x++;
					id = "";
				} else {
					id += (char) data;
				}
				
				data = reader.read();
			}
			
			reader.close();
		} catch(IOException err) { System.out.println("There was an error in loading chunk " + chunkX); }
		return new Chunk(chunkX, blocks);
	}


	/*
	 * Res File Loading: Load all OGG and PNG files in the RES folder
	 */
	public static void LoadResFiles() {
		System.out.println(" --- Loading Images and Sounds --- ");

		LoadDirectory(new File(Values.Res_Folder), ImageManager.getImageHash(), SoundManager.getSoundHash());
		
		System.out.println(" --- " + SoundManager.getSize() + " Sound Files Loaded ---");
		System.out.println(" --- " + ImageManager.getSize() + " Images Loaded ---");
	}
	private static void LoadDirectory(File dir, HashMap<String, Image> images, HashMap<String, Sound> sounds) {
		for(final File f: dir.listFiles()) {
			if(f.isDirectory()) {
				System.out.println("New Directory Found: " + f.getName());
				LoadDirectory(f, images, sounds);
			} else {
				try {
					System.out.println("Attempting to Load File: " + f.getName());
					String[] split = f.getName().split("\\.");
					
					if(split[1].toLowerCase().equals("png")) {
						images.put(split[0], new Image(f.getPath()));
						System.out.println("Loaded File as Image");
					} else if(split[1].toLowerCase().equals("ogg")) {
						sounds.put(split[0], new Sound(f.getPath()));
						System.out.println("Loaded File as Sound");
					} else {
						System.out.println("File Loading Failed");
					}
				} catch(Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Failed to Load File");
				}
			}
		}
	}
}