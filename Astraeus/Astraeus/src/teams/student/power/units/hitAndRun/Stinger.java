package teams.student.power.units.hitAndRun;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.weapon.WeaponType;
import components.weapon.energy.EnergySiphon;
import components.weapon.energy.SmallLaser;
import components.weapon.kinetic.FlakBattery;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;
import teams.student.power.PowerPlayer;
import teams.student.power.units.Fighter;

public class Stinger extends Fighter //resource unit destroyer
{
	private Unit target;
	public Stinger(PowerPlayer p, int weaponType, int defenseType) 
	{
		super(p, weaponType, defenseType);
		target = null;
		mainCombatUnit = false;
	}
	
	public void design() 
	{
		setFrame(Frame.LIGHT);
		setStyle(Style.DAGGER);
		
		switch(weaponType)
		{
		case 0 : addWeapon(new MachineGun(this));	
			break;
		case 1 : addWeapon(new EnergySiphon(this));
			break;
		}
		
		 addWeapon(new SpeedBoost(this));
		
		super.design();
	}
	
	public void action() 
	{
		target = powerPlayer.getStrats().getVulnerableUnit();
		
		if(target == null) target = getNearestPassiveEnemy();
		
		getWeaponOne().use(target);
		getWeaponTwo().use();
		
		if(this.getDistance(powerPlayer.getArmyManager().getStingersX(), powerPlayer.getArmyManager().getStingersY()) > 300)
		{
			moveTo(powerPlayer.getArmyManager().getStingersX(), powerPlayer.getArmyManager().getStingersY());
		} 
		else if(target != null)
		{
			moveTo(target);
		} 
		else if(this.getCenterY() > powerPlayer.getStrats().getRushRest().getY() * 0.75f)
		{
			moveTo(powerPlayer.getStrats().getRushRest().getX(), 
					powerPlayer.getStrats().getRushRest().getY());
		} else
		{
			moveTo(this.getCenterX() + this.getSpeedX(),
					powerPlayer.getStrats().getRushRest().getY());
		}
	}
	
	public void draw(Graphics g)
	{
		if(target != null)
		{
			g.drawLine(this.getCenterX(), this.getCenterY(), target.getCenterX(), target.getCenterY());
		}
	}
}
