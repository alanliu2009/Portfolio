package animations.effects;

import org.newdawn.slick.Color;

import engine.Utility;

public class YellowSparks extends Sparks 
{

	public YellowSparks(float x, float y, float size, int duration) 
	{
		super(x, y, size, duration);

		setup();
	}
	
	public void setup()
	{
		if(color == null)
		{
			int r1 = 200;
			int gr1= 200;
			int b1 = Utility.random(75, 200);
			int lighten = Utility.random(55);
			color = new Color(r1+lighten, gr1+lighten, b1, 255);
		}
		
	}
	
	
}