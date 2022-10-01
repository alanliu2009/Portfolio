package animations.beams;

import org.newdawn.slick.Color;

import engine.Utility;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class AnimBeamConstantTransparent extends AnimBeamConstant 
{
	
	public AnimBeamConstantTransparent(Entity origin, Entity target, int width, int duration) 
	{
		super(origin, target, width, duration, 0, 0);
		
		int r = ((Unit) origin).getColorPrimary().getRed();
		int gr = ((Unit) origin).getColorPrimary().getGreen();
		int b = ((Unit) origin).getColorPrimary().getBlue();
		color = new Color(r, gr, b, Utility.random(100, 150));

	}
	
	public AnimBeamConstantTransparent(Entity origin, Entity target, int width, int duration, int minAlpha, int maxAlpha) 
	{
		super(origin, target, width, duration, 0, 0);
		
		int r = ((Unit) origin).getColorPrimary().getRed();
		int gr = ((Unit) origin).getColorPrimary().getGreen();
		int b = ((Unit) origin).getColorPrimary().getBlue();
		color = new Color(r, gr, b, Utility.random(minAlpha, maxAlpha));

	}
		
	public int getWidth()
	{
		return width;		
	}
	
	



}
