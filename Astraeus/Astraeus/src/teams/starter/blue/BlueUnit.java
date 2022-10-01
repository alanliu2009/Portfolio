package teams.starter.blue;

import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import objects.entity.unit.Unit;
import player.Player;

public abstract class BlueUnit extends Unit 
{	
	public BlueUnit(Player p)  
	{
		super(p);
	}
	
	public void action()
	{
			
	}
	
	public void draw(Graphics g) 
	{

	}
		
	public void skirmish(Weapon w)
	{
		Unit enemy = getNearestEnemy();
		
		if(enemy != null)
		{
			w.use(enemy);	
			
			if(getDistance(enemy) > getMaxRange())
			{
				moveTo(enemy);
			}
			else
			{
				turnTo(enemy);
				turnAround();
				move();
			}
		}
	}
}
