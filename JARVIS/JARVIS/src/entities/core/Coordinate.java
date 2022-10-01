package entities.core;

import core.Engine;
import core.Values;

public class Coordinate{
	protected float x, y;
	
	public Coordinate(float InitX, float InitY) {
		this.x = InitX;
		this.y = InitY;
	}
	
	// Update coordinate position
	public void update(float xSpeed, float ySpeed) {
		x += xSpeed / (float) Engine.FRAMES_PER_SECOND;
		y += ySpeed / (float) Engine.FRAMES_PER_SECOND;
	}
	
	// Accessor Methods
	public float getX() { return x; } // Get x position
	public float getY() { return y; } // Get y position 
	
	public float getChunk() { return x / (float) Values.Chunk_Size_X; } // Find the chunk the coordinate is located in
		
	// Mutator Methods
	public void setXPos(float x){ this.x = x; } // Set x position
	public void setYPos(float y) { this.y = y; } // Set y position
	
	// Determine magnitude of displacement from some coordinate
	public float magDisplacement(Coordinate c) {
		float[] displacement = new float[2];
		
		displacement[0] = c.getX() - x;
		displacement[1] = c.getY() - y;
		
		return (float) Math.sqrt(Math.pow(displacement[0], 2) + Math.pow(displacement[1], 2));
	}
	
}