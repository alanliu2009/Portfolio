package entities.core;

public class Line {
	float xLeft, xRight;
	
	public Line(float xLeft, float xRight) {
		if(xLeft < xRight) {
			this.xLeft = xLeft;
			this.xRight = xRight;
		} else {
			this.xLeft = xRight;
			this.xRight = xLeft;
		}
	}
	
	public boolean isIn(float a1, float a2) {
		if(a1 < xLeft && xLeft < a2) {
			return true;
		} else if(a1 < xRight && xRight < a2) {
			return true;
		} else if(xLeft < a1 && xRight > a2) {
			return true;
		}
		else return false;
	}
}