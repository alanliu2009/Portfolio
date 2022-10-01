package objects.resource;

import engine.Utility;
import territory.TerritoryManager;
import ui.display.Images;

public class Minerals extends Resource
{
	public Minerals(float x, float y) 
	{
		super(x, y);
		int r = Utility.random(0, 2);
		image = Images.minerals.getSprite(r, 0);
		color = TerritoryManager.getMineralColor();
	}
	
	public Minerals(float x, float y, float xSpeed, float ySpeed) 
	{
		super(x, y, xSpeed, ySpeed);
		int r = Utility.random(0, 2);
		image = Images.minerals.getSprite(r, 0);
		color = TerritoryManager.getMineralColor();
	}
	
}
