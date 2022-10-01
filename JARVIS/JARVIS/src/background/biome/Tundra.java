package background.biome;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import background.Scene;

public class Tundra extends Scene
{
	public Tundra() throws SlickException
	{
		super();
		
		background.setImage("mountains-60");
		middleground.setImage("tundra-80");
		foreground.setImage("tundra");
		
		frontFiller = new Color(220, 222, 238);
		backFiller = new Color(181, 197, 236);
	}
}
