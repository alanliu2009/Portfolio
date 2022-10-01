package animations.circles;

import org.newdawn.slick.Color;

import objects.entity.Entity;


public class AnimEMP extends AnimCircleGrow
{

	public AnimEMP(Entity origin, int diameter, int duration) 
	{
		super(origin, diameter, duration);
	}
	
	public int getAlpha()
	{
		return (int) (10 * percentLeft()) + 10;
	}
	
	public int getAlphaBorder()
	{
		return getAlpha() * 2;
	}
	
	public int getBorderWidth()
	{
		return 80;
	}
	
	public Color getBorderColor()
	{
		return getColor();
	}
	
	

}
