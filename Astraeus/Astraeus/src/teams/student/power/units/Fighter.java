package teams.student.power.units;

import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import components.weapon.WeaponType;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Unit;
import teams.student.power.PowerPlayer;

public class Fighter extends PowerUnit
{
	protected boolean canStall;
	protected float eTurn;
	protected float evadeRange;
	protected int weaponType;
	protected int defenseType;
	
	public Fighter(PowerPlayer p, int weaponType, int defenseType)
	{
		super(p);
		
		evadeRange = 0;
		eTurn = 0;
		this.weaponType = weaponType;
		this.defenseType = defenseType;
		canStall = false;
		
		mainCombatUnit = true;
		
		//p.getArmyManager().addAllyCombatUnit(this);
	}
	
	public void draw(Graphics g)
	{
		if(enemy != null) g.fillOval(enemy.getCenterX(), enemy.getCenterY(), 20, 20);
	}
	
	public void design()
	{
		super.design();
	}
	
	public void action()
	{
		if(enemy == null) 
		{
			enemy = getNearestEnemy();
		}
		
		if(getDistance(enemy) < 1000
				|| getEnemies().size() == 1)
		{
			skirmish(getWeaponOne());
		}
		
		getWeaponOne().use(enemy);
		
		enemy = null;
	}
	
	public void skirmish(Weapon w)
	{	
		Unit firingTarget = enemy;
		
		if(getDistance(target) > getWeaponOne().getMaxRange())
		{
			firingTarget = getNearestEnemy();
		}
		
		if(getDistance(enemy) > getWeaponOne().getMaxRange() * 0.95f)
		{
			moveTo(enemy);
		}
		else if(getDistance(enemy) < getWeaponOne().getMaxRange() * 0.5f)
		{
			turnTo(this.getCenterX() + enemy.getSpeedX(), this.getCenterY() + enemy.getSpeedY());
			move();
		}
		else
		{
			w.use(enemy);
		}
	}
	
}
