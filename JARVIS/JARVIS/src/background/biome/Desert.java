package background.biome;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import background.Scene;
import core.Engine;

public class Desert extends Scene
{
	public Desert() throws SlickException
	{
		super();
		
		background.setImage("mountains-60");
		middleground.setImage("dunes-80");
		foreground.setImage("dunes");
		
		frontFiller = new Color(233, 201, 124);
		backFiller = new Color(182, 136, 80);
	}
}
