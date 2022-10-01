package teams.student.power.units.sniperUnits;

import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.LaserBattery;
import components.weapon.energy.SmallLaser;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.FlakBattery;
import components.weapon.kinetic.Megacannon;
import components.weapon.kinetic.Railgun;
import components.weapon.resource.Collector;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.power.PowerPlayer;
import teams.student.power.units.Fighter;
import teams.student.power.units.PowerUnit;

public class Lancer extends Fighter
{
	
	public Lancer(PowerPlayer p, int weaponType, int defenseType)  
	{
		super(p, weaponType, defenseType);
		
		sniperUnit = true;
		//p.getArmyManager().addAllySniperUnit(this);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.DAGGER);
		
		switch(weaponType)
		{
		case 0 : 
			addWeapon(new LaserBattery(this));
			break;
		case 1 : 
			addWeapon(new Megacannon(this));
			break;
		}
		
		super.design();
	}

	public void action() 
	{
		super.action();
		
		//skirmish(getWeaponOne());
		//brawl();
		//evade();
	}	
}
