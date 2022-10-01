package territory.basic;

import objects.resource.Minerals;
import objects.resource.Scrap;
import territory.Territory;
import ui.display.Images;

public class ShatteredSpace extends Territory
{
	public ShatteredSpace() 
	{
		super();
		background = Images.getBackground(1);
	}

	public void spawnNodes()
	{
		for(int i = 0; i < 12; i++)
		{
			spawnAsteroid(getRandomX(), getRandomY());
			spawnDerelict(getRandomX(), getRandomY());
		}
	}
	
	public void spawnResources()
	{
		for(int i = 0; i < 2; i++)
		{
			spawnResourceCluster(Minerals.class, getRandomX(), getRandomY());
			spawnResourceCluster(Scrap.class, getRandomX(), getRandomY());
		}

	}
}
