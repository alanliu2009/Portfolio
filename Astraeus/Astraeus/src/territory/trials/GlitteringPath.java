package territory.trials;

import org.newdawn.slick.Color;

import engine.Utility;
import objects.resource.Minerals;
import territory.Territory;
import ui.display.Images;

public class GlitteringPath extends Territory
{
	public GlitteringPath() 
	{
		super();
		background = Images.getBackgroundGod(3);
	}

//	public Color getDerelictColor()
//	{
//		int green = Utility.random(30, 100);
//		return new Color(100, green, 30);
//	}
	
	public String getName()
	{
		return "Messenger's Path";
	}


	public Color getDerelictColor()
	{
		int b = Utility.random(150, 200);
		return new Color(150, 150, b);
	}
	
	
	public Color getAsteroidColor()
	{
		int b = Utility.random(150, 200);
		return new Color(150, 150, b);
	}
	
	public Color getMineralColor()
	{
		return Utility.getRandomColor(150, 255);
	}	
	
	public void spawnNodes()
	{		
		for(int i = 0; i < 30; i++)
		{
			spawnAsteroid(getRandomX(), getRandomY(.45f, .95f));
		}
	}
	

	public void spawnResources()
	{
		for(int i = 0; i < 20; i++)
		{
			spawnResourceCluster(Minerals.class, getRandomX(.95f), getRandomY(.45f, .95f), Utility.random(5), 150);
		}
	}
}
