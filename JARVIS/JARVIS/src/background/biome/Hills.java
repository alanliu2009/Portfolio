package background.biome;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import background.Scene;
import core.Engine;

public class Hills extends Scene
{
	public Hills() throws SlickException
	{
		super();
		resX = Engine.RESOLUTION_X;
		resY = Engine.RESOLUTION_Y;
		
		background.setImage("mountains-60");
		middleground.setImage("hills1-80");
		foreground.setImage("hills1");

		frontFiller = new Color(56, 128, 36);
		backFiller = new Color(43, 132, 69);
	}
}
