package teams.student.drive.utility.shapes;

public class HelperVector {
	public float x, y;
	
	public HelperVector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Return the magnitude of the vector
	public float magnitude() { return (float) Math.sqrt(x * x + y * y); }
	
	// Multiply the vector by some scalar value
	public void scalarMultiply(float scalar) { 
		x *= scalar;
		y *= scalar;
	}

	// Normalize the vector
	public void normalize() {
		float magnitude = magnitude();
		
		x /= magnitude;
		y /= magnitude;
	}
}