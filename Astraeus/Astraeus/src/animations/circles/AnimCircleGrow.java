package animations.circles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Settings;
import engine.Utility;
import objects.entity.Entity;

public class AnimCircleGrow extends AnimCircle
{
	private int baseDiameter;
	public AnimCircleGrow(Entity origin, int diameter, int duration) 
	{
		super(origin, diameter, duration);
		baseDiameter = diameter;
	}

	public void update()
	{
		super.update();
		diameter = (int) (baseDiameter * (1-percentLeft()));
		
//		if(Game.getTime() % 100 == 0)
//		{
//			
//		}
//		int sliceSize = baseDiameter / 1000;
//		int sliceCount = Game.getTime() % 1000; 
//		
//		diameter = diameter - sliceSize * sliceCount;
//		System.out.println(diameter);
	}
	
	public int getAlpha()
	{
		return (int) (20 * percentLeft());
	}
	
	public int getAlphaBorder()
	{
		return getAlpha() * 3;
	}
	
	public int getBorderWidth()
	{
		return 1;
	}
	
	public Color getBorderColor()
	{
		return getSecondaryColor();
	}
	
	
	public void render(Graphics g)
	{
		if (Settings.showAnimations && ticks < duration)
		{
			
			Color color = Utility.modifyAlpha(getColor(), getAlpha());
						
			g.setColor(color.brighter());
			g.fillOval(origin.getCenterX()-diameter/2, origin.getCenterY()-diameter/2, diameter, diameter);
				
			g.setLineWidth(getBorderWidth());

			
			color = Utility.modifyAlpha(getBorderColor(), getAlphaBorder());

			g.setColor(color.brighter());
			g.drawOval(origin.getCenterX()-diameter/2, origin.getCenterY()-diameter/2, diameter, diameter);
					
		}
	}

				
}
