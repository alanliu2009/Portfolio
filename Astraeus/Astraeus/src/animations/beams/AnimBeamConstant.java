package animations.beams;

import org.newdawn.slick.Color;

import objects.entity.Entity;

public class AnimBeamConstant extends AnimBeam 
{
	
	public AnimBeamConstant(Entity origin, Entity target, int width, int duration, float xOff, float yOff) 
	{
		super(origin, target, width, duration);
		xOffset = xOff;
		yOffset = yOff;
	}
	
	
	public Color getColor(int r, int g, int b)
	{
		return new Color(r, g, b, 150);
	}
	
	public int getWidth()
	{
		return width;		
	}



}
