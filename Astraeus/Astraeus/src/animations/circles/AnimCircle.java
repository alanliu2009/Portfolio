package animations.circles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import animations.Animation;
import engine.Utility;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class AnimCircle extends Animation 
{
	protected Entity origin;
	protected int team;
	protected int diameter;
	protected Color color;
	
	public AnimCircle(Entity origin, int diameter, int duration) 
	{
		super(origin.getX(), origin.getY(), duration);

		this.origin = origin;
		this.diameter = diameter;
	}
	
	public AnimCircle(Entity origin, int diameter, int duration, Color color) 
	{
		super(origin.getX(), origin.getY(), duration);
		this.origin = origin;
		this.diameter = diameter;
		this.color = color;
	}

	public Color getColor()
	{
		if(color != null)
		{
			return color;
		}
		else if(origin instanceof Unit)
		{
			return ((Unit) origin).getColorPrimary();
		}	
		else
		{
			return null;
		}
	}
	
	public Color getSecondaryColor()
	{
		if(color != null)
		{
			return color;
		}
		else if(origin instanceof Unit)
		{
			return ((Unit) origin).getColorSecondary();
		}	
		else
		{
			return null;
		}
	}

	public int getAlpha()
	{
		return 5;
	}
	
	public void render(Graphics g)
	{
		if (ticks < duration)
		{
	
			Color color = Utility.modifyAlpha(getColor(), getAlpha());
			
			// Draw background color
			
			g.setColor(color);
			g.fillOval(origin.getCenterX()-diameter/2, origin.getCenterY()-diameter/2, diameter, diameter);
	
			// Draw brighter core
			
			color = Utility.modifyAlpha(getColor(), getAlpha() * 3);

			g.setColor(color.brighter());
			g.drawOval(origin.getCenterX()-diameter/2, origin.getCenterY()-diameter/2, diameter, diameter);
					
		}
	}

}
