package territory.trials;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import engine.Utility;
import objects.entity.unit.BaseShip;
import objects.resource.Minerals;
import territory.Territory;
import ui.display.Images;

public class HiddenReserve extends Territory
{
	public HiddenReserve() 
	{
		super();
		background = Images.getBackgroundGod(4);
	}

//	public Color getDerelictColor()
//	{
//		int b = Utility.random(150, 200);
//		return new Color(150, 150, b);
//	}
	
//	public Color getAsteroidColor()
//	{
//		int b = Utility.random(150, 200);
//		return new Color(150, 150, b);
//	}
	
	public Color getMineralColor()
	{
		return new Color(0, 220, 0);
	}	
	
	public void spawnNodes()
	{	
		int i = 0;
	
		while(i < 25)
		{
			Point p = new Point(getRandomX(), getRandomY());
			Point p2 = new Point(BaseShip.BASE_SHIP_X_POSITION-1500, 0);
			float distance = Utility.distance(p, p2);
			
			if(distance > 2500 && distance < 3500 && p.getX() > p2.getX())
			{
				spawnAsteroid(p.getX(), p.getY() * 1.4f);
				spawnResourceCluster(Minerals.class, p.getX(), p.getY()  * 1.4f, Utility.random(7), 900);

				i++;
			}
		}
		

	}
	

	public void spawnResources()
	{		

	}
}
