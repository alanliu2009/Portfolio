package animations.effects;

import engine.Utility;
import ui.display.Images;

public class BoomDark extends Boom
{
	public BoomDark(float x, float y, float size) 
	{
		super(x, y, size);
	}
	
	protected void setImage()
	{
		image = Images.boomsDark.getSprite(Utility.random(0, 3), 0);
	}

	
	public Boom constructChild(float centerX, float centerY, float newSize)
	{
		return new BoomDark(centerX, centerY, newSize);
	}

	
}
