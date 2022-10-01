package territory.basic;

import objects.resource.Minerals;
import objects.resource.Scrap;
import territory.Territory;

public class ThePassage extends Territory
{
	public ThePassage() 
	{
		super();
//		background = Images.backgrounds[4];
	}

	public void spawnNodes()
	{
		for(int i = 0; i < 20; i++)
		{
			spawnAsteroid(getRandomX(), getRandomY(.45f, 1f));
			spawnDerelict(getRandomX(), getRandomY(.45f, .1f));

		}
		
//		for(int i = 0; i < 10; i++)
//		{
//			float x = getRandomX(.4f);
//			float y = getRandomY();
//			spawnDerelict(x, y);
						
//			spawnResourceCluster(Tech.class, x, y, 5, 100);
//			spawnResourceCluster(Scrap.class, x, y, 5, 100);
//		}
	}
	
	public void spawnResources()
	{
		for(int i = 0; i < 2; i++)
		{
			spawnResourceCluster(Minerals.class, getRandomX(.4f), getRandomY(.45f));
			spawnResourceCluster(Scrap.class, getRandomX(.4f), getRandomY(.45f));
		}

	}
}
