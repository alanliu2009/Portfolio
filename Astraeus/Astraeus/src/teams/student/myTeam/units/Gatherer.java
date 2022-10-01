package teams.student.myTeam.units;


import components.upgrade.CargoBay;
import components.weapon.resource.Collector;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.resource.Resource;
import teams.student.myTeam.MyTeam;
import teams.student.myTeam.MyTeamUnit;

public class Gatherer extends MyTeamUnit 
{

	public Gatherer(MyTeam p)  
	{
		super(p);
	}

	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.BUBBLE);
		addWeapon(new Collector(this));
		addUpgrade(new CargoBay(this));
	}

	public void action() 
	{
		gatherNearbyResources();
		returnResources();
	}

	public void gatherNearbyResources()
	{
		if(hasCapacity())
		{
			Resource r = getNearestResource();

			if(r != null)
			{
				moveTo(r);
			}

			collect();
		}
	}

	protected void collect()
	{
		if(!isFull() && getDistance(getNearestResource()) < Collector.MAX_RANGE * .7f)
		{
			if(getWeaponOne() != null && getWeaponOne() instanceof Collector)
			{
				getWeaponOne().use();
			}
			if(getWeaponTwo() != null && getWeaponTwo() instanceof Collector)
			{
				getWeaponTwo().use();
			}
		}
	}

	public void returnResources()
	{
		if(isFull())
		{
			moveTo(getHomeBase());
			deposit();
		}	
	}


}
