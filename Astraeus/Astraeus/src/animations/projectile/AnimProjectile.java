package animations.projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import animations.Animation;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public abstract class AnimProjectile extends Animation 
{
	protected Entity origin;
	protected Point target;
	protected int team;
	protected int size;
	protected Color color;
		
	protected float xMid;
	protected float yMid;
	protected float xMidLast;
	protected float yMidLast;
	

	
	public AnimProjectile(Entity origin, Point target, int size, int duration) 
	{
		super(origin.getX(), origin.getY(), duration);
		setBasics(origin, target, size);
	}
	
	public AnimProjectile(Entity origin, Point target, int size, int duration, Color color) 
	{
		super(origin.getX(), origin.getY(), duration);
		setBasics(origin, target, size);
		this.color = color;
	}
	
	private void setBasics(Entity origin, Point target, int size)
	{
		this.origin = origin;
		this.target = target;
		this.size = size;
		xMid = x;
		yMid = y;
		xMidLast = x;
		yMidLast = y;
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

	public void update()
	{
		super.update();
		
		float xDiff = Math.abs(origin.getCenterX() - target.getX());
		if (origin.getCenterX() > target.getX())
		{
			xDiff *= -1;
		}
		xMidLast = xMid;
		xMid = origin.getCenterX() + xDiff * (float) ticks / (float) duration;

		// Calculate Current Y Position
		float yDiff = Math.abs(origin.getCenterY() - target.getY());
		if (origin.getCenterY() > target.getY())
		{
			yDiff *= -1;
		}
		yMidLast = yMid;
		yMid = origin.getCenterY() + yDiff * (float) ticks / (float) duration;
		
//		float f = target.sp
//		
//		xMid += target.getSpeedX();
//		yMid += target.getSpeedY();
	}
	
	abstract public void render(Graphics g);


}
