package teams.student.power.units.hitAndRun;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import components.DamageType;
import components.upgrade.Plating;
import components.upgrade.Shield;
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
import teams.student.power.resources.Point;
import teams.student.power.units.Fighter;

public class Scout extends Fighter
{	
	private float eTurn;
	private float evadeRange;
	private Unit target;
	
	public Scout(PowerPlayer p, int weaponType, int defenseType)  
	{
		super(p, weaponType, defenseType);
		
		enemy = null;
		
		evadeRange = 0;
		eTurn = 0;
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
		case 1 : addWeapon(new SmallLaser(this));
			break;
		}
		
		//addUpgrade(new Shield(this));
		
		addWeapon(new SpeedBoost(this));
		
		super.design();
	}

	public void action() 
	{
		enemy = getNearestHostileEnemy();
		hitAndRun(this.getWeaponOne());
		
		//scout sometimes passes by some resource units, so might as well hit them a bit
		//scout usually isn't in range of hostile units because of (tax) evasion, so the slow-down shouldn't be an issue
		//getWeaponOne().use(this.getNearestEnemy());
	}	

	public void draw(Graphics g)
	{	
		if(enemy != null && target != null) 
		{
			g.drawString("angle to enemy: " + getAngleToward(enemy.getCenterX(), enemy.getCenterY()) + eTurn, x, y);
			g.setColor(new Color(255, 0, 0));
			g.drawLine(this.getCenterX(), this.getCenterY(), enemy.getCenterX(), enemy.getCenterY());
			g.setColor(new Color(0, 255, 0));
			g.drawLine(this.getCenterX(), this.getCenterY(), target.getCenterX(), target.getCenterY()); 
			g.drawOval(enemy.getCenterX() - evadeRange / 2, enemy.getCenterY() - evadeRange / 2, 
					evadeRange, evadeRange);
		}
	}
	
	public void evade()
	{
		if(enemy == null) return;
		
		evadeRange = 0;
		float weaponRange = 0;
		
		ArrayList<Unit> enemies = getPowerPlayer().getEnemyFleet();
		
		if(enemies.size() == 0 || enemies == null)
		{
			weaponRange = enemy.getWeaponOne().getMaxRange();
			if(enemy.hasWeaponTwo())
			{
				if(enemy.getWeaponTwo().getMaxRange() > weaponRange) weaponRange = enemy.getWeaponTwo().getMaxRange();
			}
			
			if(weaponRange > evadeRange) evadeRange = weaponRange;
		} 
		else
		{
			for(Unit u : enemies)
			{	
				if(u.hasWeaponOne()) {
					weaponRange = u.getWeaponOne().getMaxRange();
					if(u.hasWeaponTwo())
					{
						if(u.getWeaponTwo().getMaxRange() > weaponRange) weaponRange = u.getWeaponTwo().getMaxRange();
					}
					
					if(weaponRange > evadeRange) evadeRange = weaponRange;
				}
			}
		}
		
		evadeRange *= 5;
		
		eTurn = 180 * ( 
				(evadeRange - getDistance(enemy)) 
				/ evadeRange);
	
		if(getDistance(enemy) < evadeRange)
		{	
			turnTo( getAngleToward(enemy.getCenterX(), enemy.getCenterY()) + eTurn );
			move();
		} 
	}
	
	public void hitAndRun(Weapon w)
	{
		target = enemy;
		float distance = getDistance(target);
		
		evade();
		
		if(target == null)
		{
			target = getEnemyBase();
		}
		else if(distance < getWeaponOne().getMaxRange() * 0.8f)
		{
			w.use(target);
		} 
		else
		{
			moveTo(target);
		}
		
		if(getWeaponTwo().canUse())
		{
			getWeaponTwo().use();
		}
		
	}
}
