package entities.core;

import managers.DisplayManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.Engine;

// The hitbox of the entity, basically
public class Rectangle {
	private Entity entity;

	private float width;
	private float height;

	public Rectangle(Entity entity) {
		this.entity = entity;

		this.width = entity.width;
		this.height = entity.height;
	}

	// Must adjust width and height in classes extending from entity
	public void setWidth(float width) { this.width = width; }
	public void setHeight(float height) { this.height = height; }

	// Rendering Methods
	public void drawHitBox(Graphics g) { // Draw the rectangle
		DisplayManager display = Entity.game.displayManager;

		Vector[] corners = getCorners(0f,0f);
		
		for(int i = 0; i < corners.length; i++) {
			int next = i + 1;
			if(next == corners.length) next = 0;
			
			g.drawLine(
					display.screenX(corners[i].x),
					display.screenY(corners[i].y),
					display.screenX(corners[next].x),
					display.screenY(corners[next].y)
			);
		}
	}
	
	// Get the corners of the hitbox
	public Vector[] getCorners(float adjustmentX, float adjustmentY) {
		Vector[] corners = new Vector[4];
		Vector[] axes = getAxes();
		
		// Find the transformed vectors
		corners[0] = Vector.add(axes[0].switchDirection(), axes[1]); // New top left corner
		corners[1] = Vector.add(axes[0], axes[1]);// New top right
		corners[2] = Vector.add(axes[0], axes[1].switchDirection());// New bottom right
		corners[3] = Vector.add(axes[0].switchDirection(), axes[1].switchDirection());// New bottom left
		
		// Find the vector relative to the xy origin
		for(int i = 0; i < corners.length; i++) {
			corners[i].add(new Vector(entity.position.getX() + adjustmentX, entity.position.getY() + adjustmentY));
		}
		
		return corners;
	}
	// Get the axes that the rectangle is aligned on
	private Vector[] getAxes() {
		Vector[] axes = new Vector[2];
	
		axes[0] = Vector.Standard_X.rotate(entity.angle); // X Basis Vector
		axes[1] = Vector.Standard_Y.rotate(entity.angle); // Y Basis Vector 
		
		// Expand both axes to encompass the width or height of the rectangle
		axes[0].multiply(width / 2);
		axes[1].multiply(height / 2);
		
		return axes;
	}
	
	// Main method that determines if this rectangle intersects with another one
	public boolean intersects(Rectangle rect2) {
		Vector[] axes1 = getAxes(); // Axes of this rectangle
		Vector[] axes2 = rect2.getAxes(); // Axes of the other rectangle
		
		Vector[] corners1 = getCorners(entity.xSpeed / Engine.FRAMES_PER_SECOND, entity.ySpeed / Engine.FRAMES_PER_SECOND);
		Vector[] corners2 = rect2.getCorners(rect2.entity.xSpeed / Engine.FRAMES_PER_SECOND, rect2.entity.ySpeed / Engine.FRAMES_PER_SECOND);
		
		float x1 = entity.position.x + entity.xSpeed / Engine.FRAMES_PER_SECOND;
		float y1 = entity.position.y + entity.ySpeed / Engine.FRAMES_PER_SECOND;
		
		float x2 = rect2.entity.position.x + rect2.entity.xSpeed / Engine.FRAMES_PER_SECOND;
		float y2 = rect2.entity.position.y + rect2.entity.ySpeed / Engine.FRAMES_PER_SECOND;
		
		// First projection
		Vector v1 = relToCenter(corners1[0], x2, y2);
		Vector v2 = relToCenter(corners1[2], x2, y2);
		
		boolean b1 = new Line(
				Vector.project(v1, axes2[1]),
				Vector.project(v2, axes2[1])
				) .isIn(-1f, 1f);
		if(!b1) return false; // Short circuiting
		
		// Second projection
		Vector v3 = relToCenter(corners1[1], x2, y2);
		Vector v4 = relToCenter(corners1[3], x2, y2);
		
		boolean b2 = new Line( 
				Vector.project(v4, axes2[0]),
				Vector.project(v3, axes2[0])
				) .isIn(-1, 1);
		if(!b2) return false; // Short circuiting
		
		// Third projection
		Vector v5 = relToCenter(corners2[0], x1, y1);
		Vector v6 = relToCenter(corners2[2], x1, y1);
		
		boolean b3 = new Line(
				Vector.project(v5, axes1[0]), 
				Vector.project(v6, axes1[0])
				) .isIn(-1, 1);
		if(!b3) return false; // Short circuiting
		
		// Fourth projection
		Vector v7 = relToCenter(corners2[1], x1, y1);
		Vector v8 = relToCenter(corners2[3], x1, y1);
	
		boolean b4 = new Line( 
				Vector.project(v8, axes1[1]),
				Vector.project(v7, axes1[1])
				) .isIn(-1, 1);
		if(!b4) return false; // Short circuiting
		
		return true; // If all booleans are true, the two rectangles will collide
	}
	
	// Helper method 
	private Vector relToCenter(Vector v1, float x, float y) {
		return new Vector(v1.x - x, v1.y - y);
	}
}