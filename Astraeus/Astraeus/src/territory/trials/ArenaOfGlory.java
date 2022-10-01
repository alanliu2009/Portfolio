package territory.trials;

import org.newdawn.slick.Color;

import engine.Utility;
import engine.states.Game;
import objects.resource.Scrap;
import territory.Territory;
import ui.display.Images;

public class ArenaOfGlory extends Territory
{
	public ArenaOfGlory() 
	{
		super();
		background = Images.getBackgroundGod(0);

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
		for(int i = 0; i < 8; i++)
		{
			spawnDerelict(getRandomX(.50f), getRandomY(.50f));
		}
		
		for(int i = 0; i < 8; i++)
		{
			spawnDerelict(getRandomX(.65f), getRandomY(.65f));
		}
		
		for(int i = 0; i < 8; i++)
		{
			spawnDerelict(getRandomX(.80f), getRandomY(.80f));
		}
	}
	
	public void spawnResources()
	{
		for(int i = 0; i < 10; i++)
		{
			spawnResourceCluster(Scrap.class, getRandomX(.50f), getRandomY(.50f), Utility.random(5), 150);
		}
		for(int i = 0; i < 10; i++)
		{
			spawnResourceCluster(Scrap.class, getRandomX(.65f), getRandomY(.65f), Utility.random(5), 150);
		}
		for(int i = 0; i < 10; i++)
		{
			spawnResourceCluster(Scrap.class, getRandomX(.80f), getRandomY(.80f), Utility.random(5), 150);
		}
		for(int i = 0; i < 10; i++)
		{
			spawnResourceCluster(Scrap.class, getRandomX(), getRandomY(), Utility.random(5), 150);
		}
	}
}
