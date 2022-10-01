package teams.student.drive.utility.shapes;

import objects.GameObject;

public class HelperPoint {
	public float x, y;
	
	public HelperPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public HelperPoint(GameObject o) {
		this.x = o.getCenterX();
		this.y = o.getCenterY();
	}
	
	public void setPoint(GameObject o) {
		this.x = o.getCenterX();
		this.y = o.getCenterY();
	}
	
	public void setPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPoint(GameObject o, float xOffset, float yOffset) {
		this.setPoint(o);
		
		this.x += xOffset;
		this.y += yOffset;
	}
}