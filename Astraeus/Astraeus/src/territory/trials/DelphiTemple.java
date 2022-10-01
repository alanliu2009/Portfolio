package territory.trials;

import org.newdawn.slick.Color;

import territory.Territory;
import ui.display.Images;

public class DelphiTemple extends Territory
{
	public DelphiTemple() 
	{
		super();
		background = Images.getBackgroundGod(5);
	}

//	public Color getDerelictColor()
//	{
//		int b = Utility.random(150, 200);
//		return new Color(150, 150, b);
//	}
	
	public Color getAsteroidColor()
	{
		return new Color(200, 200, 200);	
	}
	
	public Color getMineralColor()
	{
		return new Color(255, 255, 200);
	}	
	
	public void spawnNodes()
	{	
		int spacing = 900;
	
		for(int i = -4; i <= 4; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				spawnAsteroid(i * spacing, j * spacing + spacing/2, 2);
			}			
		}
		
		// Left and Right Sides
		for(int j = -4; j <= 4; j++)
		{
			spawnAsteroid(9 * spacing, j * spacing, 2);			
		}
		
		// Top and Bottom
		for(int i = -9; i <= 9; i++)
		{
			spawnAsteroid(i * spacing, 5 * spacing, 2);			
		}
	}
	

	public void spawnResources()
	{

	}
}
