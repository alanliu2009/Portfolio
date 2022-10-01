package teams.student.myTeam;

import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import objects.entity.unit.Unit;
import player.Player;

public abstract class MyTeamUnit extends Unit 
{	
	public MyTeamUnit(Player p)  
	{
		super(p);
	}
	
	public MyTeam getPlayer()
	{
		return (MyTeam) super.getPlayer();
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
