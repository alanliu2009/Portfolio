package objects.resource;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import engine.Utility;
import engine.Values;
import ui.display.Camera;

public  class ResourceManager 
{
	

	public static float STARTING_SPEED_MIN = .1f * Values.SPEED;
	public static float STARTING_SPEED_MAX = 1f * Values.SPEED;
	
	private static ArrayList<Resource> resources;
	
	public static ArrayList<Resource> getResources()
	{
		return resources;
	}
	
	public static void setup()
	{
		resources = new ArrayList<Resource>();
		
		// eventually add some scattered minerals in scenarios?
		
//		spawnResourceClusterPair(Minerals.class, 500, 500, 15, 400);

		
	
	}
	
	public static void spawnResourceClusterPair(Class <? extends Resource > clazz, float x, float y,  int amount, int radius)
	{
		float xPos = x;
		float yPos = y;
		float xSpeed = Utility.random(STARTING_SPEED_MIN, STARTING_SPEED_MAX);
		float ySpeed = Utility.random(STARTING_SPEED_MIN, STARTING_SPEED_MAX);
		
		if(clazz.isAssignableFrom(Minerals.class))
		{
			spawnMineralClusterPair(xPos, yPos, xSpeed, ySpeed, amount, radius);
		}
		if(clazz.isAssignableFrom(Scrap.class))
		{
			spawnScrapClusterPair(xPos, yPos, xSpeed, ySpeed, amount, radius);
		}
	}

	public static Point getPositionNear(float x, float y, float radius)
	{
		Point p = new Point(0, 0);
		
		do
		{
			
			
			
			p.setX(Utility.random(x - Utility.random(radius), x + Utility.random(radius)));
			p.setY(Utility.random(y - Utility.random(radius), y + Utility.random(radius)));
			
//			p.setX(Utility.random(Utility.random(x - radius, 0), Utility.random(0, x + radius)));
//			p.setY(Utility.random(Utility.random(y - radius, 0), Utility.random(0, y + radius)));

			
			
		} while(Utility.distance(x, y, p.getX(), p.getY()) > radius);
		
		return p;
	}
	
	public static void spawnMineralNear(float x, float y, float xSpeed, float ySpeed, float radius)
	{
		Point p = getPositionNear(x, y, radius);	
		ResourceManager.add(new Minerals(p.getX(), p.getY(), xSpeed, ySpeed));
	}
	
	public static void spawnScrapNear(float x, float y, float xSpeed, float ySpeed, float radius)
	{
		Point p = getPositionNear(x, y, radius);	
		ResourceManager.add(new Scrap(p.getX(), p.getY(), xSpeed, ySpeed));
	}
		
	public static void spawnMineralClusterPair(float x, float y, float xSpeed, float ySpeed, int count, int radius)
	{
		for(int i = 0; i < count; i++)
		{
			Point p = getPositionNear(x, y, radius);	
			ResourceManager.add(new Minerals(p.getX(), p.getY(), xSpeed, ySpeed));
			ResourceManager.add(new Minerals(-p.getX(), -p.getY(), -xSpeed, -ySpeed));
		}
	}
	
	public static void spawnScrapClusterPair(float x, float y, float xSpeed, float ySpeed, int count, int radius)
	{
		for(int i = 0; i < count; i++)
		{
			Point p = getPositionNear(x, y, radius);	
			ResourceManager.add(new Scrap(p.getX(), p.getY(), xSpeed, ySpeed));
			ResourceManager.add(new Scrap(-p.getX(), -p.getY(), -xSpeed, -ySpeed));
		}
	}
		
	public static void add(Resource r)
	{
		resources.add(r);
	}
	
	public static void update()
	{
		for(Resource r : resources)
		{
			r.update();
		}
	}
	
	public static void cleanUp()
	{
		for(int i = 0; i < resources.size(); i++)
		{
			Resource r = resources.get(i);
			
			if(r.wasPickedUp())
			{
				resources.remove(r);
				r = null;
				i--;
			}
		}
	}
	
	public static void render(Graphics g)
	{
		for(Resource r : resources)
		{
			if(Camera.isOnScreen(r.getX(), r.getY()))
			{
				r.render(g);
			}
		}
	}
}
