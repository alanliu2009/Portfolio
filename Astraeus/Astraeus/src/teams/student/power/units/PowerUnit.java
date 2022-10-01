package teams.student.power.units;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import components.weapon.Weapon;
import components.weapon.WeaponType;
import engine.Utility;
import engine.states.Game;
import objects.GameObject;
import objects.entity.Entity;
import objects.entity.node.Node;
import objects.entity.unit.Unit;
import player.Player;
import teams.student.power.PowerPlayer;

public abstract class PowerUnit extends Unit 
{	
	protected GameObject target;
	protected Unit enemy;
	protected PowerPlayer powerPlayer;
	protected boolean mainCombatUnit;
	protected boolean sniperUnit;
	protected boolean supportUnit;
	protected boolean scoutUnit;
	protected boolean brawlerUnit;
	
	//variables for PD (maybe PID in future) loop
	protected float kp;
	protected float kd;
	protected float lastError;
	protected float totalError;
	protected float currError;
	protected float pkp; 
	protected float pki; 
	protected float pkd;
	
	protected float nkp; 
	protected float nki;
	protected float nkd;
		
	protected boolean assigned; //if the unit is assigned to a manager
	
	//-------------------------------------- (lines mean enemy fleet identification)
	protected ArrayList<Unit> nearestEnemies;
	protected ArrayList<Unit> enemyFleet;
	protected float enemyFleetX;
	protected float enemyFleetY;
	protected int enemyFleetSize;
	protected Point fleetPos;
	//--------------------------------------	
	
	public PowerUnit(Player p)  
	{
		super(p);
		target = null;
		kp = 0.4f;
		totalError = 0;
		lastError = 0;
		currError = 0;
		//default values, adjust them for each unit as needed
		pkp = 0.7f; 
		pki = 0.004f; 
		pkd = 0.5f; 
		
		nkp = 0.05f; 
		nki = 0.005f;
		nkd = 20f;
		
		assigned = false;
		enemy = null;
		powerPlayer = (PowerPlayer) p;
		
		mainCombatUnit = false;
		sniperUnit = false;
		supportUnit = false;
		scoutUnit = false;
		brawlerUnit = false;
		
		//--------------------------------------
		enemyFleetX = 0;
		enemyFleetY = 0;
		enemyFleetSize = 0;
		fleetPos = new Point(0, 0);
		nearestEnemies = new ArrayList<Unit>();
		enemyFleet = new ArrayList<Unit>();
		//--------------------------------------
	}
	
	public boolean isMainCombatUnit() {	return mainCombatUnit; }
	public boolean isSniperUnit() {	return sniperUnit; }
	public boolean isSupportUnit() {	return supportUnit; }
	public boolean isScoutUnit() {	return scoutUnit; }
	public boolean isBrawlerUnit() {	return brawlerUnit; }
	public Unit getEnemy() { return enemy; }
	public void setEnemy(Unit e)	{	enemy = e;	}
	
	
	public void design()
	{
		
	}
	
	public void action()
	{
		//enemy = getNearestEnemy();
	}
	
	public void draw(Graphics g) 
	{
		
	}
	
	public GameObject getTarget() {
		return target;
	}
	public void setTarget(GameObject o) {
		target = o;
	}
	
	public PowerPlayer getPowerPlayer()
	{
		return powerPlayer;
	}
	
	public boolean needHealing() {
		if(this.getPercentStructure() < .4f) {
			return true;
		}
		return false;
	}
	
	//---------------------- micro stuff -------------------
	
	public void approach(GameObject o) {
		turnTo(o);
		currError = this.getDistance(o);
		float proportional = currError * kp;
		float derivative = (currError-lastError) * kd;
		
		setPercentSpeed(50 + proportional-derivative);
		
		lastError=currError;
	}
	
	//set direction you want unit to go before calling this method
	public void setPercentSpeed(float pct) {
		float chance = Utility.random(100f);
		if(chance > pct) {
			this.turnAround();
		}
		this.move();
	}	
	

	public Unit getNearestHostileEnemy() 
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestEnemy = null;
		ArrayList<Unit> enemies =  getEnemies();

		for(Unit u : enemies)
		{
			if(getDistance(u) < nearestDistance && u.hasWeaponOne()
					&& u.getWeaponOne().getWeaponType() != WeaponType.RESOURCE)
			{
				
				nearestEnemy = u;
				nearestDistance = getDistance(u);
			}
		}

		return nearestEnemy;
	}
	
	public Unit getNearestPassiveEnemy() 
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> enemies = getEnemies();
		
		if(enemies.size() == 0) return null;
		
		for(Unit u : enemies)
		{
			if(getDistance(u) < nearestDistance && u.hasWeaponOne()
					&& u.getWeaponOne().getWeaponType() == WeaponType.RESOURCE)
			{
				
				nearestUnit = u;
				nearestDistance = getDistance(u);
			}
		}

		return nearestUnit;
	}
	
	public void circle(GameObject o, float r, int direction) {
		circle(o.getCenterX(), o.getCenterY(), r, direction);
	}
	
	public void circle(float x, float y, float r, int direction) {
		float centerX = x;
		float centerY = y;
		float nDegrees = this.getAngleToward(centerX, centerY);
		float targetDistance = r; 
		currError = Math.abs(this.getDistance(centerX, centerY) - targetDistance);

		float proportional = 0;
		float integral = 0;
		float derivative = 0;
		float integralThreshold = 25;
		float adjustment;
		
		if(currError < integralThreshold) {
			totalError += currError;
		}
		if(currError < 2) {
			totalError = 0;
			
		}

		if(this.getDistance(centerX, centerY) > targetDistance){ //if too far
			if(totalError > 50/pki) {//sets hard limit on totalError for the integral
				totalError = 50/pki;
			}
			proportional = -currError * pkp;
			integral = -(float) totalError * pki;
			derivative = -(currError-lastError) * pkd;

			
		}else if(this.getDistance(centerX, centerY) < targetDistance) { //if too close
			if(totalError > 50/nki) {
				totalError = 50/nki;
			}
			proportional = currError * nkp;
			integral = totalError * nki;
			derivative = (currError-lastError) * nkd;

			
		}else {
			turnTo(nDegrees+90);

		}
		
		if(currError < 2) {
			derivative = 0;
		}
		
		adjustment = proportional + integral + derivative;
		
		if(adjustment > 90) {
			adjustment = 90;
		}else if(adjustment < -90) {
			adjustment = -90;
		}
		
		this.turnTo(nDegrees + 75*direction + adjustment*direction);
		this.move();
		
		lastError = currError;
		
	}
	
	//----------------------------------
	//not very relevant stuff below here
	//----------------------------------
	
	public float getEnemyFleetX() { return fleetPos.getX(); }
	public float getEnemyFleetY() { return fleetPos.getY(); }
	public ArrayList<Unit> getEnemyFleet() { return enemyFleet; }
	
	public void enemyFleets()
	{
		nearestEnemies = sortEnemies(getEnemies());
		
		int threshold = 1;
		
		enemyFleet.clear();
		enemyFleetSize = 0;
		enemyFleetX = 0;
		enemyFleetY = 0;
		
		for(int i = 0; i < nearestEnemies.size() - 1; i++)
		{
			if(nearestEnemies.get(i).getDistance(nearestEnemies.get(i + 1))
					< 2000)
			{
				enemyFleetSize++;
			} 
			else if(enemyFleetSize > threshold) 
			{				
				for(int j = i - 1; j > i - enemyFleetSize - 1; j--)
				{			
					enemyFleetX = enemyFleetX + nearestEnemies.get(j).getCenterX();
					enemyFleetY = enemyFleetY + nearestEnemies.get(j).getCenterY();
					
					enemyFleet.add(nearestEnemies.get(j));
				}
				
				enemyFleetX /= enemyFleetSize;
				enemyFleetY /= enemyFleetSize;
						
				fleetPos.setX(enemyFleetX); fleetPos.setY(enemyFleetY);
				
				break;
			}
			else
			{
				enemyFleetSize = 0;
			}
		}
	}
	
	public ArrayList<Unit> sortEnemies(ArrayList<Unit> e)
	{
		ArrayList<Unit >nearestEnemies = e;
		
		int index = nearestEnemies.size();
		
		int errors = 0;
		boolean sorted = false;
				
		while(sorted == false)
		{	
			errors = 0;
			
			for(int i = 0; i < index - 1; i++)
			{
				if(getDistance(nearestEnemies.get(i)) > getDistance(nearestEnemies.get(i + 1)))
				{
					errors++;
				}
			}
			
			if(errors > 0) 
			{
				for(int i = 0; i < index - 1; i++)
				{
					if(getDistance(nearestEnemies.get(i)) > getDistance(nearestEnemies.get(i + 1)))
					{
						Unit temp = nearestEnemies.get(i);
						
						nearestEnemies.set(i, nearestEnemies.get(i + 1));
						nearestEnemies.set(i + 1, temp);
					}
				}
			} 
			else 
			{
				sorted = true;
			}
		}
		
		return(nearestEnemies);
	}
}
