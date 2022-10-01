package animations.effects;

import engine.Utility;
import ui.display.Images;

public class BoomInferno extends Boom
{
	public BoomInferno(float x, float y, float size) 
	{
		super(x, y, size);
	}
	
	protected void setImage()
	{
		image = Images.booms.getSprite(Utility.random(0, 3), 0);
	}

	
	public Boom constructChild(float centerX, float centerY, float newSize)
	{
		return new BoomInferno(centerX, centerY, newSize);
	}

	
}
