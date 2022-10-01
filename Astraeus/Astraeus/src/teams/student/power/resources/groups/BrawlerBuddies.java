package teams.student.power.resources.groups;

import java.util.ArrayList;

import objects.entity.unit.Frame;
import teams.student.power.units.PowerUnit;

public class BrawlerBuddies extends Buddies 
{
	ArrayList<PowerUnit> tanks;
	ArrayList<PowerUnit> notTanks;
	
	public BrawlerBuddies()
	{
		super();
		maxSpread = 500;
		tanks = new ArrayList<PowerUnit>();
		notTanks = new ArrayList<PowerUnit>();
	}
	
	public void update()
	{
		orderTanks();
		super.update();
	}
	
	public void orderTanks()
	{
		tanks.clear();
		notTanks.clear();
		
		for(PowerUnit u : units)
		{
			if(u.getFrame() == Frame.HEAVY
					|| u.getFrame() == Frame.ASSAULT)
			{
				tanks.add(u);
			}
			else
			{
				notTanks.add(u);
			}
		}
		
		if(calcCenter(tanks).getDistance(target) > calcCenter(notTanks).getDistance(target) - 200)
		{
			for(PowerUnit u : notTanks)
			{
				u.circle(calcCenter(tanks).getX(), calcCenter(tanks).getY(), 250, 0);
			}
		}
		
		System.out.println(calcCenter(notTanks).getDistance(target));
		System.out.println(calcCenter(tanks).getDistance(target));
	}
}
