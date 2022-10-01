package territory.trials;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import engine.Utility;
import objects.resource.Scrap;
import territory.Territory;
import ui.display.Images;

public class RockyShoals extends Territory
{
	public RockyShoals() 
	{
		super();
		background = Images.getBackgroundGod(1);
//		derelictColor = new Color(new Color(120, 30, 30));
//		asteroidColor = new Color(new Color(200, 100, 70));
//		mineralColor = new Color(new Color(225, 200, 75));
//		scrapColor = new Color(new Color(200, 200, 200));
	}

//	public Color getDerelictColor()
//	{
//		int green = Utility.random(30, 100);
//		return new Color(100, green, 30);
//	}
	
	

	public Color getAsteroidColor()
	{
		int g = Utility.random(120, 160);
		return new Color(120, g, 200);
	}
	
	public Color getMineralColor()
	{
		return new Color(0, 200, 200);
	}
	
	public Color getScrapColor()
	{
		return new Color(0, 200, 200);
	}
	
	
	public void spawnNodes()
	{		
		int i = 0;
		while(i < 20)
		{
			Point p = new Point(getRandomX(), getRandomY());
			Point p2 = new Point(0, 0);
			float distance = Utility.distance(p, p2);
			
			if(distance > 3500 && distance < 5000)
			{
				for(int j = 0; j < 3; j++)
				{
					spawnAsteroid(p.getX() + getRandomX(.1f), p.getY() + getRandomY(.1f));
				}
				
				i++;
			}
		}
//		for(int i = 0; i < 10; i++)
//		{
//			float x = getRandomX(.25f, .60f);
//			float y = getRandomY(.25f, .60f);
//
//			for(int j = 0; j < 3; j++)
//			{
//				spawnAsteroid(x + getRandomX(.1f), y + getRandomY(.1f));
//			}
//		}
	}

	public void spawnResources()
	{
		int i = 0;
		
		while(i < 40)
		{
			Point p = new Point(getRandomX(), getRandomY());
			Point p2 = new Point(0, 0);
			float distance = Utility.distance(p, p2);
			
			if(distance < 4000)
			{
				spawnResourceCluster(Scrap.class, p.getX(), p.getY(), Utility.random(5), 150);
				i++;
			}
		}
		
	
	}
}
