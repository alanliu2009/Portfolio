package territory.trials;

import org.newdawn.slick.Color;

import objects.resource.Scrap;
import territory.Territory;
import ui.display.Images;

public class RiverStyx extends Territory
{
	public RiverStyx() 
	{
		super();
		background = Images.getBackgroundGod(6);

	}
//	
	public Color getDerelictColor()
	{
		return new Color(100, 50, 100);	
	}
	
	public Color getScrapColor()
	{
		return new Color(150, 0, 150);
	}	
	
	public void spawnNodes()
	{
		for(int i = 0; i < 30; i++)
		{
			float x = getRandomX(.4f, 1.0f);
			float y = getRandomY();
			spawnDerelict(x, y);
			spawnResourceCluster(Scrap.class, x, y, 4, 400);
		}
//		
//		for(int i = 0; i < 10; i++)
//		{
//			spawnAsteroid(getRandomX(.50f, .95f), getRandomY(.50f, .95f));
//		}
//		
//		for(int i = 0; i < 12; i++)
//		{
//			float x = getRandomX();
//			float y = getRandomY();
//			spawnDerelict(x, y);
//						
//			spawnResourceCluster(Scrap.class, x, y, 4, 400);
//		}
	}
	
	public void spawnResources()
	{
//		for(int i = 0; i < 2; i++)
//		{
//			spawnResourceCluster(Minerals.class, getRandomX(), getRandomY());
//			spawnResourceCluster(Tech.class, getRandomX(), getRandomY());
//			spawnResourceCluster(Scrap.class, getRandomX(), getRandomY());
//		}

	}
}
