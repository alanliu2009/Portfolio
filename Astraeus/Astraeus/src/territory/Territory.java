package territory;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import engine.Utility;
import engine.states.Game;
import objects.entity.node.Asteroid;
import objects.entity.node.Derelict;
import objects.entity.node.NodeManager;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import ui.display.Images;

public abstract class Territory 
{
	protected int resourceClusterCount;
	protected int resourcePerCluster;
	protected int resourceClusterRadius;
	protected Image background;
	
	public Territory()
	{
		resourceClusterCount = 6;
		resourcePerCluster = 8;
		resourceClusterRadius = 400;
		background = Images.getRandomBackground();
	}
	
	abstract public void spawnNodes();
	abstract public void spawnResources();
	
	public Image getBackground()
	{
		return background;
	}
	
	public Color getAsteroidColor()
	{
		return new Color(145, 100, 70);
	}
	
	public Color getDerelictColor()
	{
		return new Color(45, 65, 85);
	}
	
	public Color getMineralColor()
	{
		return new Color(200, 200, 200);
	}
	
	public Color getScrapColor()
	{
		return new Color(200, 200, 200);
	}
	
	
	public String getName()
	{
		//System.out.println("hi");

		String simpleName = getClass().getSimpleName();
		String name = ""+simpleName.charAt(0);
		

		
		for(int i = 1; i < simpleName.length(); i++)
		{
			char c = simpleName.charAt(i);
			
			if(c == 7)
			{
				name += "'s";
			}
			if(c >= 65 && c <= 90)
			{
				name += " ";
			}
			name += c;
		}
		
	//	System.out.println(name);

		
		return name;
		
	}

	public void spawn()
	{
		spawnNodes();
		spawnResources();
	}
	
	public static float getRandomX()
	{
		return getRandomX(.95f);
	}
	
	public static float getRandomX(float outerPercent)
	{
		return Utility.random(Game.getMapLeftEdge() * outerPercent, Game.getMapRightEdge() * outerPercent);
	}
	
	
	public static float getRandomX(float innerPercent, float outerPercent)
	{
		float r1 = Utility.random(Game.getMapLeftEdge() * outerPercent,  Game.getMapLeftEdge() * innerPercent);
		float r2 = Utility.random(Game.getMapRightEdge() * innerPercent, Game.getMapRightEdge() * outerPercent);
		return Math.random() < .5 ? r1 : r2;
	}
	
	
	public static float getRandomY()
	{
		return getRandomY(.95f);
	}
	
	public static float getRandomY(float outerPercent)
	{
		return Utility.random(Game.getMapTopEdge() * outerPercent, Game.getMapBottomEdge() * outerPercent);
	}
	
	public static float getRandomY(float innerPercent, float outerPercent)
	{
		float r1 = Utility.random(Game.getMapTopEdge() * outerPercent, Game.getMapTopEdge() * innerPercent);
		float r2 = Utility.random(Game.getMapBottomEdge() * innerPercent, Game.getMapBottomEdge() * outerPercent);
		return Math.random() < .5 ? r1 : r2;
	}
	
	public void spawnNode(float x, float y)
	{
		if(Math.random() < .5)
		{
			spawnAsteroid(x, y);
		}
		else
		{
			spawnDerelict(x, y);
		}
	}
	
	public void spawnAsteroid(float x, float y)
	{
		spawnAsteroid(x, y, 0);
	}
	
	public void spawnAsteroid(float x, float y, int size)
	{
		NodeManager.spawnNodePair(Asteroid.class, x, y, size, getAsteroidColor(), getMineralColor());
	}
	
	public void spawnDerelict(float x, float y)
	{
		spawnDerelict(x, y, 0);
	}
	
	public void spawnDerelict(float x, float y, int size)
	{
		NodeManager.spawnNodePair(Derelict.class, x, y, size, getDerelictColor(), getScrapColor());
	}
	
	public void spawnResourceCluster(Class<? extends Resource> clazz, float x, float y)
	{
		spawnResourceCluster(clazz, x, y, resourcePerCluster, resourceClusterRadius);
	}
	
	protected void spawnResourceCluster(Class<? extends Resource> clazz, float x, float y, int count, int radius)
	{
		ResourceManager.spawnResourceClusterPair(clazz, x, y, count, radius);
	}
	

}
