package teams.student.power.units.brawlingUnits;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.energy.EnergySiphon;
import components.weapon.energy.SmallLaser;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.RepairBeam;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.power.PowerPlayer;
import teams.student.power.units.Fighter;

public class Rock extends Fighter
{
	public Rock(PowerPlayer p, int weaponType, int defenseType) 
	{
		super(p, weaponType, defenseType);
		
		brawlerUnit = true;
	}
	
	public void design() 
	{
		setFrame(Frame.HEAVY);
		setStyle(Style.BUBBLE);
		
		switch(defenseType)
		{
		case 0 : addUpgrade(new Plating(this)); addUpgrade(new Plating(this));
			break;
		case 1 : addUpgrade(new Shield(this)); addUpgrade(new Shield(this));
			break;
		case 2 : addUpgrade(new Plating(this)); addUpgrade(new Shield(this));
		}
		
		addUpgrade(new Structure(this));
		
		switch(weaponType)
		{
		case 0 : addWeapon(new EnergySiphon(this));
			break;
		case 1 : addWeapon(new MachineGun(this));
			break;
		}
		
		super.design();
	}
}
