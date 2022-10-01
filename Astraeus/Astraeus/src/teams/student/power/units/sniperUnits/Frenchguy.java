package teams.student.power.units.sniperUnits;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.energy.EnergySiphon;
import components.weapon.kinetic.FlakBattery;
import components.weapon.kinetic.Railgun;
import components.weapon.utility.Pullbeam;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.power.PowerPlayer;
import teams.student.power.units.Fighter;

public class Frenchguy extends Fighter //from a certain videogame
{
	public Frenchguy(PowerPlayer p, int weaponType, int defenseType)  
	{
		super(p, weaponType, defenseType);
		
		sniperUnit = true;
		//p.getArmyManager().addAllySniperUnit(this);
		//p.getArmyManager().addAllyCombatUnit(this);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.ORB);
		
		addWeapon(new FlakBattery(this));
		
		switch(defenseType)
		{
		case 0 : addUpgrade(new Plating(this));
			break;
		case 1 : addUpgrade(new Shield(this));
			break;
		case 2 : addUpgrade(new Plating(this));
		}
		
		super.design();
	}
}
