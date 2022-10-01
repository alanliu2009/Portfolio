package territory.trials;

import org.newdawn.slick.Color;

import engine.Utility;
import objects.resource.Minerals;
import territory.Territory;
import ui.display.Images;

public class HuntingGrounds extends Territory
{
	public HuntingGrounds() 
	{
		super();
		background = Images.getBackgroundGod(2);
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
	
	
	public Color getDerelictColor()
	{
		int g = Utility.random(90, 110);
		return new Color(80, g, 80);
	}
	
//	public Color getAsteroidColor()
//	{
//		int r = Utility.random(110, 160);
//		int g = Utility.random(110, 160);
//		return new Color(r, g, 80);
//	}
	
	public Color getMineralColor()
	{
		return new Color(200, 200, 0);
	}
	
	public Color getScrapColor()
	{
		return new Color(200, 200, 0);
	}
	
	
	public void spawnNodes()
	{		
		for(int i = 0; i < 20; i++)
		{
			spawnAsteroid(getRandomX(), getRandomY(.0f,.4f));
			spawnDerelict(getRandomX(), getRandomY(.0f,.4f));
		}
	}

	public void spawnResources()
	{
		for(int i = 0; i < 12; i++)
		{
			spawnResourceCluster(Minerals.class, getRandomX(.95f), getRandomY(.40f), Utility.random(11), 150);
		}
	}
}
