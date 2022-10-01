package teams.student.power.units.sniperUnits;

import org.newdawn.slick.Graphics;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.LaserBattery;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.FlakBattery;
import components.weapon.kinetic.Megacannon;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.power.PowerPlayer;
import teams.student.power.units.Fighter;

public class Kabomber extends Fighter
{
	public Kabomber(PowerPlayer p, int weaponType, int defenseType) 
	{
		super(p, weaponType, defenseType);
		
		sniperUnit = true;
		
		//p.getArmyManager().addAllySniperUnit(this);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.BOXY);
		
//		switch(weaponType)
//		{
//		case 0 : addWeapon(new LaserBattery(this));
//			break;
//		case 1 : addWeapon(new Megacannon(this));
//			break;
//		}

		addWeapon(new AntimatterMissile(this));
		
		super.design();
		//addUpgrade(new Structure(this));
	}
}
