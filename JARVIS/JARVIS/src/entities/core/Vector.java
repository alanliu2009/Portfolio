package entities.core;

public class Vector {
	public static Vector Standard_X = new Vector(1,0);
	public static Vector Standard_Y = new Vector(0,1);
	
	protected float x;
	protected float y;
	
	// Default Constructor for a Vector
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() { return x; }
	public float getY() { return y; }
	
	// Multiply the vector by some scalar quantity
	public void multiply(double c) {
		this.x *= c;
		this.y *= c;
	}
	
	// Add the vector to another vector
	public void add(Vector v) {
		this.x += v.x;
		this.y += v.y;
	}
	
	// Rotate the vector counterclockwise about the origin theta degrees (in radians)
	public Vector rotate(float theta) {
		float newX = (float) (x * Math.cos(theta) - y * Math.sin(theta));
		float newY = (float) (x * Math.sin(theta) + y * Math.cos(theta));
		
		return new Vector(newX, newY);
	}
	
	public Vector switchDirection() {
		return new Vector(-this.x, -this.y);
	}
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}
	// Find the projection of a vector onto an axis
	public static float project(Vector vector, Vector axis) {
		return Vector.dot(vector, axis) / Vector.dot(axis, axis);
	}
	public static float dot(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
}