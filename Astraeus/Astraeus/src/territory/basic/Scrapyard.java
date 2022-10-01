package territory.basic;

import objects.resource.Scrap;
import territory.Territory;

public class Scrapyard extends Territory
{
	public Scrapyard() 
	{
		super();
	}

	public void spawnNodes()
	{
		for(int i = 0; i < 10; i++)
		{
			spawnDerelict(getRandomX(.4f), getRandomY(.4f));
		}
		
		for(int i = 0; i < 10; i++)
		{
			spawnAsteroid(getRandomX(.50f, .95f), getRandomY(.50f, .95f));
		}
		
		for(int i = 0; i < 12; i++)
		{
			float x = getRandomX();
			float y = getRandomY();
			spawnDerelict(x, y);
						
			spawnResourceCluster(Scrap.class, x, y, 4, 400);
		}
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
