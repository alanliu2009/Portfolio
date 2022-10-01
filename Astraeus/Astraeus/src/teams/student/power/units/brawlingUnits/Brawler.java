package teams.student.power.units.brawlingUnits;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import components.DamageType;
import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.SmallLaser;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;
import teams.student.power.PowerPlayer;
import teams.student.power.units.Fighter;

public class Brawler extends Fighter
{
	public Brawler(PowerPlayer p, int weaponType, int defenseType)  
	{
		super(p, weaponType, defenseType);
		
		pkp = 0.7f; 
		pki = 0.004f; 
		pkd = 0.5f; 
		
		nkp = 0.05f; 
		nki = 0.005f;
		nkd = 20f;
		
		brawlerUnit = true;
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.ROCKET);

		switch(defenseType)
		{
		case 0 : addUpgrade(new Plating(this));
			break;
		case 1 : addUpgrade(new Shield(this));
			break;
		case 2 : addUpgrade(new Plating(this));
		}
		
		addUpgrade(new Structure(this));
		
		switch(weaponType)
		{
		case 0 : addWeapon(new SmallLaser(this));
			break;
		case 1 : addWeapon(new MachineGun(this));
			break;
		}
		
		addWeapon(new SpeedBoost(this));
		
		super.design();
	}
	
	public void action() 
	{
		super.action();

		target = enemy;
		
		if(getDistance(enemy) < 1000
				|| getEnemies().size() == 1)
		{
			evade();
		}
	}	
	
	public void draw(Graphics g)
	{
		if(enemy != null) g.drawLine(this.getCenterX(), this.getCenterY(), 
				target.getCenterX(), target.getCenterY());
	}
	
	public void evade()
	{
		if(enemy == null) return;
		
		evadeRange = getWeaponOne().getMaxRange() * .9f;
		eTurn = 180 * ( 
				(evadeRange - getDistance(enemy)) 
				/ evadeRange);
	
		if(getDistance(enemy) < evadeRange)
		{	
			turnTo( getAngleToward(enemy.getCenterX(), enemy.getCenterY()) + eTurn );
			move();
			if(this.hasWeaponTwo())
			{
				getWeaponTwo().use();
			}
		} 
		else
		{
			moveTo(enemy);
		}
	}
	
	public void brawl(Weapon w)
	{
		if(getDistance(enemy) < w.getMaxRange() && this.getCurSpeed() > this.getMaxSpeed() * 0.9f)
		{
			w.use(enemy);
		} 
		else
		{
			w.use(getNearestEnemy());
		}
	}
}
