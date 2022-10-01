package teams.starter.green.units;


import components.weapon.Weapon;
import components.weapon.resource.Collector;
import components.weapon.resource.MiningLaser;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.resource.Resource;
import teams.starter.green.Green;
import teams.starter.green.GreenUnit;

public class Worker extends GreenUnit 
{
	
	public Worker(Green p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.WEDGE);
		addWeapon(new MiningLaser(this));
		addWeapon(new Collector(this));
	}

	public void action() 
	{
		returnResources();
		gatherNearbyResources(2000);
		harvest(getNearestNode(), getWeaponOne());
		collect();
	}
	
	public void gatherNearbyResources(int radius)
	{
		if(hasCapacity())
		{
			Resource r = getNearestResource();

			if(r != null && getDistance(r) < radius)
			{
				moveTo(r);
			}

			collect();
		}
	}
	
	public void harvest(Node n, Weapon w)
	{
		// Approach the node
		if(getDistance(n) > w.getMaxRange() * .5f)
		{
			moveTo(n);
		}
		
		// Back up if I'm close to my minimum range
		else if(getDistance(n) < w.getMinRange() * 1.5f)
		{
			turnTo(n);
			turnAround();
			move();
		}
				
		w.use(n);
	}

	protected void collect()
	{
		if(!isFull() && getDistance(getNearestResource()) < Collector.MAX_RANGE)
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
