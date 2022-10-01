package teams.student.drive.utility.shapes;

public class HelperLine {
	public float originX, originY;
	public HelperVector direction;
	
	public HelperLine(float x, float y, float x2, float y2) {
		originX = x;
		originY = y;
		
		direction = new HelperVector(y2 - y, x2 - x);
	}
	public HelperLine (float x, float y, HelperVector v) {
		originX = x;
		originY = y;
		
		direction = v;
	}
}