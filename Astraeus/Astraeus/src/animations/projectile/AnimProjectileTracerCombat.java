package animations.projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import engine.Utility;
import objects.entity.Entity;

public class AnimProjectileTracerCombat extends AnimProjectileTracer
{	
	public AnimProjectileTracerCombat(Entity origin, Entity target, int size, int duration) 
	{
		super(origin, new Point(target.getCenterX() + getVariance(target), target.getCenterY() + getVariance(target)), size, duration);
	}
	
	
	public AnimProjectileTracerCombat(Entity origin, Entity target, int size, int duration, Color c) 
	{
		super(origin, new Point(target.getCenterX() + getVariance(target), target.getCenterY() + getVariance(target)), size, duration, c);

	}
	
	public static float getVariance(Entity target)
	{
		return Utility.random(-target.getWidth()/3, target.getWidth() / 3);
	}

}
