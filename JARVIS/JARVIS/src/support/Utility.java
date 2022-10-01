package support;

import entities.core.Entity;

public class Utility //random functions and the such, feel free to add any annoying-to-write lines
{
	public static int random(int max) 
	{
		return (int) (Math.random() * max);
	}

	public static int random(int min, int max) 
	{
		return (int) (Math.random() * (max - min + 1)) + min;

	}

	public static float random(double min, double max) 
	{
		return (float) (min + (Math.random() * (max - min)));
	}
	
	public static float getDistance(Entity a, Entity b) {
		float d = 0f;
		
		d = (float) Math.sqrt((Math.pow(changeX(a, b), 2)) + (Math.pow(changeY(a,b), 2)) );		
		return d;
	}
	
	public static float getAngle(Entity a, Entity b) {
		float theta = 0f;
		theta = (float)Math.atan(changeY(a,b)/changeX(a,b));
	
		
		return theta;
	}
	
	public static boolean rectangleOverlap(float[] rec1, float[] rec2) {
		float x1 = rec1[0], y1 = rec1[1], x2 = rec1[2], y2 = rec1[3];
		float x3 = rec2[0], y3 = rec2[1], x4 = rec2[2], y4 = rec2[3];
		
		return (x1 < x4) && (x3 < x2) && (y1 < y4) && (y3 < y2);
	}
	
	public static float changeX(Entity a, Entity b) {
		return b.getPosition().getX() - a.getPosition().getX();
	}
	public static float changeY(Entity a, Entity b) {
		return b.getPosition().getY() - a.getPosition().getY();
	}
}
