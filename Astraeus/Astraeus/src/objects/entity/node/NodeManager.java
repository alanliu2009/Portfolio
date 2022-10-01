package objects.entity.node;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Utility;
import ui.display.Camera;

public  class NodeManager 
{
	
	public static final float MIN_SPEED = .00125f;
	public static final float MAX_SPEED = .0125f;
	
	private static ArrayList<Node> nodes;
	
	public static ArrayList<Node> getNodes()
	{
		return nodes;
	}
	
	public static void setup()
	{
		nodes = new ArrayList<Node>();
	}

	public static void spawnNodePair(Class <? extends Node > clazz, float x, float y, Color nodeColor, Color resourceColor)
	{
		spawnNodePair(Asteroid.class, x, y, -1, nodeColor, resourceColor);
	}
	
	public static void spawnNodePair(Class <? extends Node > clazz, float x, float y, int size, Color nodeColor, Color resourceColor)
	{
		float xSpeed = Utility.random(MIN_SPEED, MAX_SPEED);
		
		if(Math.random() > .5)
		{
			xSpeed *= -1;
		}
		
		float ySpeed = Utility.random(MIN_SPEED, MAX_SPEED);
		
		if(Math.random() > .5)
		{
			ySpeed *= -1;
		}
		
		if(size == 0)
		{
			size = Utility.random(1, 3);
		}

		if(clazz.isAssignableFrom(Asteroid.class))
		{
			nodes.add(new Asteroid(x, y, xSpeed, ySpeed, size, nodeColor, resourceColor));
			nodes.add(new Asteroid(-x, -y, -xSpeed, -ySpeed, size, nodeColor, resourceColor));
		}
		if(clazz.isAssignableFrom(Derelict.class))
		{
			nodes.add(new Derelict(x, y, xSpeed, ySpeed, size, nodeColor));
			nodes.add(new Derelict(-x, -y, -xSpeed, -ySpeed, size, nodeColor));
		}
	}

	
//	public static void colorAsteroids(Color c, float percent)
//	{
//		for(Node n : nodes)
//		{
//			if(n instanceof Asteroid && Math.random() < percent)
//			{
//				n.setColor(c);
//			}
//		}
//	}
//	
	public static void update()
	{	
		for(Node n : nodes)
		{
			if(n.isAlive())
			{
				n.update();
			}
		}
	}
	
	public static void cleanUp()
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);
			
			if(n.isDead())
			{
				nodes.remove(n);
				i--;
			}
		}
	}
	
	public static void render(Graphics g)
	{
		for(Node n : nodes)
		{
			if(Camera.isNearScreen(n.getCenterX(), n.getCenterY()))
			{
				n.render(g);
			}
		}
	}
}
