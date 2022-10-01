package teams.student.power.units.supportUnits;

import java.util.ArrayList;

import components.weapon.explosive.AntimatterMissile;
import components.weapon.utility.RepairBeam;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;
import teams.student.power.PowerPlayer;
import teams.student.power.units.PowerUnit;

public class Medic extends PowerUnit
{
	PowerPlayer p;
	public Medic(PowerPlayer p) 
	{
		super(p);
		this.p = p;
		supportUnit = true;
		mainCombatUnit = true;
	}

	@Override
	public void design() 
	{
		setFrame(Frame.LIGHT);
		setStyle(Style.WEDGE);
		
		addWeapon(new RepairBeam(this));
		//p.getArmyManager().addAllyHealerUnit(this);
		
		super.design();
	}
	
	public void action() {
		getWeaponOne().use(getWeakestAlly());
		
//		if(getDistance(getNearestAlly()) > 5000 && getDistance(enemy) < 2000)
//		{
//			nobleSacrifice();
//		}
	}
	
	public void nobleSacrifice()
	{
		moveTo(this.getX(), this.getY() + this.getSpeedY());
	}
	
	public PowerUnit getWeakestAlly()
	{
		ArrayList<PowerUnit> allies = p.getArmyManager().getAllyCombatUnits();
		float distance = Float.MAX_VALUE;
		float percentHealth = Float.MAX_VALUE;
		PowerUnit target = null;
		
		if(allies.size() == 0) return null;
		
		for(PowerUnit a : allies)
		{
			if(getDistance(a) < getWeaponOne().getMaxRange()
					&& getDistance(a) < distance
					&& a.getPercentEffectiveHealth() < percentHealth)
			{
				percentHealth = a.getPercentEffectiveHealth();
				distance = getDistance(a);
				target = a;
			}
		}
		
		return target;
	}
}
