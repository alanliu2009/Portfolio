package territory.basic;

import org.newdawn.slick.Color;

import engine.Utility;
import engine.states.Game;
import objects.resource.Scrap;
import territory.Territory;

public class Battlefield extends Territory
{
	public Battlefield() 
	{
		super();
	}

	public Color getDerelictColor()
	{
		int a = 70;
		int b = 40;
		Color c1 = (Game.getPlayerOne().getColorPrimary().multiply(new Color(a, a, a))).addToCopy(new Color(b, b, b));
		Color c2 = (Game.getPlayerTwo().getColorPrimary().multiply(new Color(a, a, a))).addToCopy(new Color(b, b, b));

		if(Math.random() < .5)
		{
			return c1;
		}
		else
		{
			return c2;
		}
	}
	
	
	public void spawnNodes()
	{		
		for(int i = 0; i < 10; i++)
		{
			spawnDerelict(getRandomX(.40f), getRandomY(.40f));
		}
		
		for(int i = 0; i < 10; i++)
		{
			spawnDerelict(getRandomX(.70f), getRandomY(.70f));
		}
	}
	
	public void spawnResources()
	{
		for(int i = 0; i < 12; i++)
		{
			spawnResourceCluster(Scrap.class, getRandomX(.40f), getRandomY(.40f), Utility.random(5), 150);
		}
	}
}
