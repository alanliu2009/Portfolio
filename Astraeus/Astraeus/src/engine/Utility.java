package engine;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import objects.GameObject;
import objects.entity.unit.Unit;

public class Utility 
{
	public static int random(int max) 
	{
		return (int) (Math.random() * max);
	}
	
	public static float random(float max) 
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

	public static float distance(GameObject a, GameObject b) 
	{
		if (a == null || b == null)
			return -1;
		return distance(a.getCenterX(), a.getCenterY(), b.getCenterX(), b.getCenterY());
	}
	
	public static float distance(Point a, Point b) 
	{
		if (a == null || b == null)
			return -1;
		return distance(a.getCenterX(), a.getCenterY(), b.getCenterX(), b.getCenterY());
	}

	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	public static boolean isNear(float x1, float y1, float x2, float y2)
	{
		return x1 >= x2 - 1 && 
			   x1 <= x2 + 1 && 
			   y1 >= y2 - 1 && 
			   y2 <= y2 + 1;
	}
	
	public static boolean isNear(Unit u, float x, float y)
	{
		return isNear(u.getCenterX(), u.getCenterY(), x, y);
	}
	
	public static boolean isNear(Unit u, Unit v)
	{
		return isNear(u.getCenterX(), u.getCenterY(), v.getCenterX(), v.getCenterY());
	}

	public static boolean hasInstance(ArrayList<Unit> units, Class<? extends Unit> clazz) 
	{
		for (Unit u : units) {
			if (clazz.isAssignableFrom(u.getClass())) {
				return true;
			}
		}
		return false;
	}
	
	public static void drawStringRightTop(Graphics g, Font f, String message, float x, float y)
	{
		g.setFont(f);
		g.drawString(message, x - f.getWidth(message), y);
	}
	
	public static void drawStringCenterTop(Graphics g, Font f, String message, float x, float y)
	{
		g.setFont(f);
		g.drawString(message, x - f.getWidth(message)/2, y);
	}
	
	public static void drawStringLeftTop(Graphics g, Font f, String message, float x, float y)
	{
		g.setFont(f);
		g.drawString(message, x, y);
	}
	
	public static void drawStringCenterCenter(Graphics g, Font f, String message, float x, float y)
	{
		g.setFont(f);
		g.drawString(message, x - f.getWidth(message)/2, y - f.getHeight(message)/2);
	}
	
	public static Color modifyAlpha(Color c, int alpha)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	public static Color blend(Color c1, Color c2)
	{
		return new Color((int)((c1.getRed() + c2.getRed())/2), 
						 (int)((c1.getGreen() + c2.getGreen())/2), 
						 (int)((c1.getBlue() + c2.getBlue())/2), 
						 (int)((c1.getAlpha() + c2.getAlpha())/ 2));
	}
	
	public static Color getRandomColor(int min, int max)
	{
		return new Color(Utility.random(min, max), Utility.random(min, max), Utility.random(min, max));
	}
	
	public static float scaleBetween(float n, float minOut, float maxOut, float minIn, float maxIn) 
	{
		 return (maxOut - minOut) * (n - minIn) / (maxIn - minIn) + minOut;
	}
	
	public static float scaleBetweenBounded(float n, float minOut, float maxOut, float minIn, float maxIn) 
	{
		float val = scaleBetween(n, minOut, maxOut, minIn, maxIn);
		return Math.max(Math.min(val,  maxOut), minOut);
	}
}
