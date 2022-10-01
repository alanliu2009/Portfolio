package territory.basic;

import engine.Utility;
import objects.resource.Minerals;
import territory.Territory;
import ui.display.Images;

public class AsteroidBelt extends Territory
{
	public AsteroidBelt() 
	{
		super();
		background = Images.getBackground(3);
		
	}

	public void spawnNodes()
	{		
		for(int i = 0; i < 25; i++)
		{
			spawnAsteroid(getRandomX(.20f), getRandomY(1f));
		}
	}
	
	public void spawnResources()
	{
		for(int i = 0; i < 12; i++)
		{
			spawnResourceCluster(Minerals.class, getRandomX(.3f), getRandomY(1f), Utility.random(5), 150);
		}
	}
}
