package teams.student.drive.utility;

import org.newdawn.slick.geom.Point;

import engine.Utility;
import teams.student.drive.utility.shapes.*;

public class DriveHelper {
	// Trig Methods (Returns Floats)
	public static float sin(float angle) { return (float) Math.sin(angle); } // Sin
	public static float cos(float angle) { return (float) Math.cos(angle); } // Cos
	public static float atan(float y, float x) { return (float) Math.atan2(y, x); } // ArcTan
	
	// Dot product (v1 * v2)
	public static float dot(HelperVector v1, HelperVector v2) { return v1.x * v2.x + v1.y * v2.y; }
	// Cross product K (v1 X v2)
	public static float crossProductK(HelperVector v1, HelperVector v2) { return v1.x * v2.y - v1.y * v2.x; }
	
	// Projection of v1 onto vector v2
	public static HelperVector project(HelperVector v1, HelperVector v2) {
		float scalar = dot(v1, v2) / dot(v2, v2);
		return new HelperVector(v2.x * scalar, v2.y * scalar);
	}
	
	// Angle between two vectors
	public static float angleBetweenVectors(HelperVector v1, HelperVector v2) {
		float n = dot(v1, v2) / v1.magnitude() / v2.magnitude();
		return (float) Math.acos(n);
	}
	
	public static boolean inCircle(float x, float y, HelperCircle circle) {
		if( Utility.distance(new Point(x,y), new Point(circle.x, circle.y)) < circle.radius ) {
			return true;
		} else return false;
	}
	// Returns the closest tangent point to a circle from a line.
	public static HelperPoint closestTangent(HelperLine line, HelperCircle circle) {
		// Normalize the line's direction
		line.direction.normalize();
		
		// Project circle center onto line
		HelperVector projection = project(new HelperVector(circle.x - line.originX, circle.y - line.originY), line.direction);
		
		HelperVector distanceVector = new HelperVector(projection.x + line.originX - circle.x, projection.y + line.originY - circle.y);
		distanceVector.scalarMultiply(circle.radius / distanceVector.magnitude());
		
		// Find tangent point on circle
		return new HelperPoint(circle.x + distanceVector.x, circle.y + distanceVector.y);
	}
	
	// Returns if a given line intersects a circle in space
	public static boolean lineIntersectsCircle(HelperLine line, HelperCircle circle, float LeeWay) {
		// Normalize the Line's Direction
		line.direction.normalize();
 
		// Project circle center onto line
		HelperVector projection = project(new HelperVector(circle.x - line.originX, circle.y - line.originY), line.direction);
		float distance = new HelperVector(projection.x + line.originX - circle.x, projection.y + line.originY - circle.y).magnitude();
		
		if(distance < circle.radius * LeeWay) return true;
		else return false;
	}
}