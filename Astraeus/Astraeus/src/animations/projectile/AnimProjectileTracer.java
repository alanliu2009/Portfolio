package animations.projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import objects.entity.Entity;

public class AnimProjectileTracer extends AnimProjectile
{	
	public AnimProjectileTracer(Entity origin, Point target, int size, int duration) 
	{
		super(origin, target, size, duration);
	}
	
	
	public AnimProjectileTracer(Entity origin, Point target, int size, int duration, Color c) 
	{
		super(origin, target, size, duration, c);
	}
	
	public void render(Graphics g)
	{
		if (ticks > duration)
		{
			return;
		}
		
		// Draw background color
		
		g.setLineWidth(size);
		g.setColor(getColor());
		g.drawLine(xMid,  yMid,  xMidLast, yMidLast);
		g.setLineWidth(size);

		// Draw brighter core
	
	}

}
