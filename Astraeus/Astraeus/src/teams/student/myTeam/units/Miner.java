package teams.student.myTeam.units;


import components.weapon.Weapon;
import components.weapon.resource.MiningLaser;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.myTeam.MyTeam;
import teams.student.myTeam.MyTeamUnit;

public class Miner extends MyTeamUnit 
{
	
	public Miner(MyTeam p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.LIGHT);
		setStyle(Style.WEDGE);
		addWeapon(new MiningLaser(this));
	}

	public void action() 
	{
		harvestNearest(getWeaponOne());
	}

	public void harvestNearest(Weapon w)
	{
		harvest(getNearestNode(), w);
	}
	
	public void harvest(Node n, Weapon w)
	{
		// Approach the node
		if(getDistance(n) > w.getMaxRange())
		{
			moveTo(n);
		}
		
		// Back up if I'm too close
		else if(getDistance(n) < w.getMaxRange() * .9f)
		{
			moveTo(getHomeBase());
		}
		
		w.use(n);
	}
	
}
