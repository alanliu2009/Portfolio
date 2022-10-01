package animations.projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import objects.entity.Entity;

public class AnimProjectileBullet extends AnimProjectile
{	
	public AnimProjectileBullet(Entity origin, Point target, int size, int duration) 
	{
		super(origin, target, size, duration);
	}
	
	
	public AnimProjectileBullet(Entity origin, Point target, int size, int duration, Color c) 
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
		
		
		g.setColor(getColor());
		g.fillOval(xMid,  yMid,  size,  size);
		// Draw brighter core
	
	}

}
