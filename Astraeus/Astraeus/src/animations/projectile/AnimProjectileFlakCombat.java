package animations.projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import objects.entity.Entity;

public class AnimProjectileFlakCombat extends AnimProjectileBulletCombat
{	
	public AnimProjectileFlakCombat(Entity origin, Entity target, int size, int duration) 
	{
		super(origin, target, size, duration);
	}
	
	
	public AnimProjectileFlakCombat(Entity origin, Entity target, int size, int duration, Color c) 
	{
		super(origin, target, size, duration, c);

	}
	
	public void render(Graphics g)
	{
		if (ticks > duration)
		{
			return;
		}
		
		g.setLineWidth((int) size / 4);
		
		g.setColor(new Color(125, 125, 125));
		g.fillRect(xMid,  yMid,  size,  size);
		
		g.setColor(getColor());
		g.drawRect(xMid,  yMid,  size,  size);
		
		g.resetLineWidth();
	
	}

}
