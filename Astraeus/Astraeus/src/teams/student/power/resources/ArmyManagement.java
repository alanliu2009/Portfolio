package teams.student.power.resources;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import components.weapon.WeaponType;
import components.weapon.resource.MiningLaser;
import engine.Utility;
import objects.entity.unit.Frame;
import objects.entity.unit.Unit;
import player.Player;
import teams.student.power.PowerPlayer;
import teams.student.power.resources.groups.ArmyBuddies;
import teams.student.power.resources.groups.BrawlerBuddies;
import teams.student.power.resources.groups.Buddies;
import teams.student.power.resources.groups.HealerBuddies;
import teams.student.power.resources.groups.SniperBuddies;
import teams.student.power.units.PowerUnit;
import teams.student.power.units.hitAndRun.Stinger;
import teams.student.power.units.resourceUnits.Camper;

public class ArmyManagement 
{
	private PowerPlayer p;
	private ArrayList<PowerUnit> allyCombatUnits;
	
	private ArrayList<Buddies> combatGroups;
 	private SniperBuddies snipers;
	private BrawlerBuddies brawlers;
	private HealerBuddies healers;
	
	private Point sniperLocation;
	private Point brawlerLocation;
	private Point healerLocation;
	
	private Point target;
	
	private CombatCircle fightingRing;
	private CombatCircle healingRing;
	private CombatCircle sniperTower;
	
	private Point fleetCenter;
	private Unit protect;
	private float myPower;
	private float theirPower;
	
	private float enemyFleetX;
	private float enemyFleetY;
	private ArrayList<Unit> enemyFleet;
	private Unit weakestEnemyUnit;
	private Camper camper;
	
	private int stingerCount;
	private Point stingerLocation;
	
	public ArmyManagement(PowerPlayer p) {
		this.p = p;
		allyCombatUnits = new ArrayList<PowerUnit>();
		
		snipers = new SniperBuddies();
		brawlers = new BrawlerBuddies();
		healers = new HealerBuddies();
		
		combatGroups = new ArrayList<Buddies>();
		combatGroups.add(brawlers);
		combatGroups.add(snipers);
		combatGroups.add(healers);
		
		sniperLocation = new Point(0, 0);
		brawlerLocation = new Point(0, 0);
		healerLocation = new Point(0, 0);
		
		target = new Point(0, 0);
		
		protect = null;
		fleetCenter = new Point(0, 0);
		myPower = 0;
		theirPower = 0;
		
		enemyFleetX = 0;
		enemyFleetY = 0;
		enemyFleet = new ArrayList<Unit>();
		weakestEnemyUnit = null;
		
		fightingRing = new CombatCircle(p, new Point(0,0), 0);
		sniperTower = new CombatCircle(p, new Point(0,0), 0);
		healingRing = new CombatCircle(p, new Point(0,0), 0);
		stingerLocation = new Point(0, 0);
	}
	
	public ArrayList<PowerUnit> getAllyCombatUnits() { return allyCombatUnits; }
	//public Unit getExposedMiner() { return protect; }
	public Unit getExposedAllyUnit() { return protect; }
	
	public void update() 
	{
		evalUnits();
		evalCombatUnits();
		enemyFleet();
		findTargets();
		
		snipers.update();
		brawlers.update();
		healers.update();
		
		protect = exposedAllyUnit();
		
		//retreat if needed
		
		//if we have POWER, attack nearest fleet
			
		//if their fleet comes too close, attack them
				
		//idle somewhere (near our most exposed miners/gatherers or just in the middle of the map)
		//float myPower = calculateMyPower(allyCombatUnits); //changed it to calculate our power when it loops through
		//float myPower = calculateMyPower(allArmyUnits);
		
		if(enemyFleet != null)
		{
			theirPower = calculatePower(getEnemyFleet());
		}
		else
		{
			theirPower = calculatePower(p.getEnemyUnits());
		}

		
		//the multipliers determine the thresholds
		//Point myCenter = calculateCenter(allArmyUnits);
		fleetCenter = calculateCenter(allyCombatUnits);
//		if(myPower * 2 < theirPower && Utility.distance(myCenter.getX(), myCenter.getY(), enemyFleetX, enemyFleetY) < 1800) {
//			setAllCircles(false);
//			retreat();
//			
//		}else if (myPower > theirPower * 1) {
		
		if(getAllyCombatUnits().size() < p.getStrats().getArmySize() * 0.6f) //change to the power thing later
		{
			stall();
		}
		
		engage();

			
//		}else {
//			setAllCircles(false);
//			idle();
//		}
		
//		clearAllDead();
		stingerLocation();
	}
	
	public void stall() //retreat but it goes to a resource unit
	{
		for(PowerUnit u : allyCombatUnits)
		{
			if(u.getDistance(u.getEnemy()) > 2000
					&& (u.getFrame() == Frame.LIGHT || u.getFrame() == Frame.MEDIUM))
			{
				if(protect != null) u.moveTo(protect);
			}
		}
		
	}
	
	public void engage() {
//		fightingRing.setRadius(350);
//		healingRing.setRadius(100);
//		sniperTower.setRadius(50);
//		if(camper!=null) {
//			fightingRing.setCenter(calcCombatCenter(camper, 300));
//			healingRing.setCenter(calcCombatCenter(camper, 500)); 
//			sniperTower.setCenter(calcCombatCenter(camper, 600)); 
//		}
//		setAllCircles(true);
//		
//		for(PowerUnit u : allySniperUnits) {
//			dynamicCircle(u, sniperTower);
//		}
//		
//		for(PowerUnit u : allyCombatUnits) { //the ring works well for the brawling units; the snipers have difficulty getting around
//			dynamicCircle(u, fightingRing);
//		}
//		
//		for(PowerUnit u : allyHealerUnits) {
//			unitCircle(u, healingRing, 50, 1);
//		}
		
		if(enemyFleet != null)
		{
			snipers.setTarget(enemyFleetX, enemyFleetY);
			brawlers.setTarget(enemyFleetX, enemyFleetY);
		}
		else
		{
			snipers.setTarget(weakestEnemyUnit.getCenterX(), weakestEnemyUnit.getCenterY());
			brawlers.setTarget(weakestEnemyUnit.getCenterX(), weakestEnemyUnit.getCenterY());
		}
		
		healers.setTarget(fleetCenter);
	}
	
	public void retreat() //replaced with stall/protecting a resource unit
	{
		for(Unit u : allyCombatUnits) {
			u.moveTo(p.getMyBase());
		}
	}
	
	public void idle() {
		protect = exposedAllyUnit();
		if(protect != null) {
			for(PowerUnit u : allyCombatUnits) {
				u.circle(protect, 250, 1);
			}
		}
	}
	
	public void dynamicCircle(PowerUnit u, CombatCircle c) {
		if(healers.getUnits().size() != 0 && u.needHealing()) {
			unitCircle(u, healingRing, 50, 1);
		}else {
			unitCircle(u, c, 100, -1);
		}
	}
	
	public void setAllCircles(boolean t) {
		fightingRing.setState(t);
		healingRing.setState(t);
		sniperTower.setState(t);
	}
	
	public void unitCircle(PowerUnit u, CombatCircle c, float threshold, int direction) {
		if(u.getDistance(c.getCenterX(), c.getCenterY()) > 500
				&& u.getDistance(fleetCenter.getX(), fleetCenter.getY()) > 200)
		{
			u.moveTo(fleetCenter.getX(), fleetCenter.getY());
		} 
		else if(u.getDistance(c.getCenterX(), c.getCenterY()) > c.getRadius() + threshold) 
		{
			u.moveTo(c.getCenterX(), c.getCenterY());
		} 
		else 
		{
			u.circle(c.getCenterX(), c.getCenterY(), c.getRadius(), direction);
		}
		
	}
	
	public Point calcCombatCenter(PowerUnit u, float r) {
		Point c = new Point (0,0);
		float radius = r;
		//Unit nearest = nearestEnemyFleetUnit(camper);

//		float targetX = p.getMyBase().getNearestEnemy().getCenterX();
//		float targetY = p.getMyBase().getNearestEnemy().getCenterY();
		float targetX = u.getEnemyFleetX();
		float targetY = u.getEnemyFleetY();
		
		if(u.getEnemies().size() < 4)
		{
			targetX = u.getEnemyBase().getCenterX();
			targetY = u.getEnemyBase().getCenterY();
		}
//		if(nearest == null) {
//			return c;
//		}
//		float targetX = nearest.getCenterY();
//		float targetY = nearest.getCenterX();
		float angle = p.getMyBase().getAngleToward(targetX, targetY);
		float totalDist = p.getMyBase().getDistance(targetX, targetY);
		float realDist = totalDist - radius;
		
		float changeX = (float) (realDist * Math.cos(angle * Math.PI / 180));
		float changeY = (float) (realDist * Math.sin(angle * Math.PI / 180));
		
		c.setX(p.getMyBase().getCenterX() + changeX);
		c.setY(p.getMyBase().getCenterY() + changeY);
		
		return c;
	}
	
	public void evalUnits() //constructor calling was kinda buggy
	{
		snipers.clearUnits();
		brawlers.clearUnits();
		healers.clearUnits();
		allyCombatUnits.clear();
		
		for(Unit a : p.getMyUnits())
		{
			if(a != a.getHomeBase())
			{
				PowerUnit temp = (PowerUnit) a;
				
				if(temp.isMainCombatUnit()) allyCombatUnits.add(temp);
				if(temp.isSniperUnit()) snipers.addUnit(temp);
				if(temp.isSupportUnit()) healers.addUnit(temp);
				if(temp.isBrawlerUnit()) brawlers.addUnit(temp);
			}
		}
		
		sniperLocation = snipers.getFleetCenter();
		brawlerLocation = brawlers.getFleetCenter();
		healerLocation = healers.getFleetCenter();
	}
	
	//enemyFleet stuff
	
	public float getEnemyFleetX() { return enemyFleetX; }
	public float getEnemyFleetY() { return enemyFleetY; }
	public ArrayList<Unit> getEnemyFleet() { return enemyFleet; }
	
	//possible in future: add parameter to adjust calculation of fleet based on whichever unit we want
	public void enemyFleet()
	{
		for(Unit u : p.getMyUnits())
		{
			if(u.getClass() == Camper.class)
			{
				camper = (Camper) u;
				break;
			} else
			{
				camper = null;
			}
		}
		
		if(camper != null)
		{
			enemyFleetX = camper.getEnemyFleetX();
			enemyFleetY = camper.getEnemyFleetY();
			enemyFleet = camper.getEnemyFleet();	
			
			if(enemyFleet.size() == 0)
			{
				enemyFleet.add(p.getMyBase().getNearestEnemy());
			}
		}
	}
	
	public float getEnemyFleetRadius() {
		float max = 0;
		if(enemyFleet == null) return max;
		
		for(int i = 0; i < enemyFleet.size(); i++) {
			float a = enemyFleet.get(i).getDistance(enemyFleetX, enemyFleetY);
			if(a > max) {
				max = a;
			}
		}
		return max;
	}
	
	public void evalCombatUnits()
	{
		float defenseScore = 0;
		float offenseScore = 0;
		
		for(PowerUnit a : allyCombatUnits)
		{
			defenseScore += a.getCurEffectiveHealth();
			if(a.hasWeaponOne()) offenseScore += getDPS(a.getWeaponOne());
		}
		
		myPower = defenseScore + offenseScore;
	}
	
	//-----------------------------------------------------------------------------------------
	//enemy finding things
	//-----------------------------------------------------------------------------------------
	
	public Unit getWeakestEnemyUnit() { return weakestEnemyUnit; }
	
	public void findTargets()
	{
		weakestEnemyUnit = weakestEnemyUnit(5000);
		
		if(weakestEnemyUnit == null)
		{
			weakestEnemyUnit = weakestEnemyUnit();
		}
	}
	
	public Unit nearestEnemyFleetUnit(PowerUnit u) {
		if(enemyFleet.size() == 0) {return null;};
		float min = u.getDistance(enemyFleet.get(0));
		int index = 0;
		for(int i = 1; i < enemyFleet.size(); i++) {
			float dist = u.getDistance(enemyFleet.get(i));
			if(dist < min) {
				index = i;
				min = dist;
			}
		}
		return enemyFleet.get(index);
	}
	
	public Unit weakestEnemyUnit() {
		ArrayList<Unit> enemies = enemyFleet;
		if(enemies.size() == 0) { enemies = p.getStrats().getHostileEnemies(); }
		
		enemies = p.getStrats().getHostileEnemies();
		
		float lowestHealth = Float.MAX_VALUE;
		Unit enemy = null;
		
		for(Unit e : enemies) {
			if(e.getCurEffectiveHealth() < lowestHealth)
			{
				lowestHealth = e.getCurEffectiveHealth();
				enemy = e;
			}
		}
		
		return enemy;
	}
	
	public Unit weakestEnemyUnit(float distance) {
		ArrayList<Unit> enemies = enemyFleet;
		if(enemyFleet.size() == 0) { enemies = p.getEnemyUnits(); }
		
		float lowestHealth = Float.MAX_VALUE;
		Unit enemy = null;
		
		for(Unit e : enemies) {
			if(e.getCurEffectiveHealth() < lowestHealth
					&& e.getDistance(fleetCenter.getX(), fleetCenter.getY()) < distance)
			{
				enemy = e;
				lowestHealth = e.getCurEffectiveHealth();
			}
		}
		
		return enemy;
	}
	
	//-----------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------
	
	//gets the miner that is the farthest away from base ship
	//possible in the future: make our army hover over our most exposed miner, if another miner is being attacked
	//then have a distress signal on that miner and identify the fleet from the POV of the attacked miner
//	public Unit exposedAllyMiner(){
//		float max = 0;
//		Unit exposed = null;
//		for(MinerBuddies mb : p.getResourceManager().getBuddies()) {
//			for(int i = 0; i < mb.getMiners().length; i++) {
//				Unit miner = mb.getMiners()[i]; 
//				if(miner != null) {
//					float a = miner.getDistance(p.getMyBase());
//					if(a > max) {
//						exposed = miner;
//						max = a;
//					}
//				}
//			}
//		}
//		
//		return exposed;
//	}
	
	public Unit exposedAllyUnit(){
		float min = Float.MAX_VALUE;
		Unit exposed = null;
		
		for(Unit u : p.getMyUnits()) 
		{
			if(u != p.getMyBase())
			{
				PowerUnit temp = (PowerUnit) u;
				float d = temp.getDistance(temp.getNearestHostileEnemy());
				
				if(u.getWeaponOne().getWeaponType() == WeaponType.RESOURCE
						&& d < min
						&& p.getMyBase().getDistance(u) < 5000)
				{
					min = d;
					exposed = u;
				}
			}
		}

		return exposed;
	}
	
	public Point calculateCenter(ArrayList<PowerUnit> units) {
		float sumX = 0;
		float sumY = 0;
		for(Unit u : units) {
			sumX += u.getCenterX();
			sumY += u.getCenterY();
		}
		Point c = new Point(0,0);
		c.setX(sumX / units.size());
		c.setY(sumY / units.size());
		return c;
	}
	
	public float calculatePower(ArrayList<Unit> units) {
		float defenseScore = 0;
		float offenseScore = 0;
		
		for(Unit u : units) {
			if(u.getWeaponOne().getWeaponType() != WeaponType.RESOURCE)
			{
				defenseScore += u.getCurEffectiveHealth();
				if(u.hasWeaponOne()) offenseScore += getDPS(u.getWeaponOne());
			}
			
		}
		
		return defenseScore + offenseScore;
	}
	
	public float getDPS(Weapon w)
	{
		if(w == null) return 0;

		return w.getDamage() * w.getAccuracy() / (w.getUseTime() + w.getCooldown());
	}
	
	public float getStingersX() { return stingerLocation.getX(); }
	public float getStingersY() { return stingerLocation.getY(); }
	public float getStingerCount() { return stingerCount; }
	
	public void stingerLocation()
	{
		float tempX = 0;
		float tempY = 0;
		
		ArrayList<Unit> stingers = p.getMyUnits(Stinger.class);
		
		stingerCount = stingers.size();
		
		for(Unit u : stingers)
		{
			tempX += u.getCenterX();
			tempY += u.getCenterY();
		}
		
		tempX /= stingers.size();
		tempY /= stingers.size();
		
		stingerLocation.setX(tempX);
		stingerLocation.setY(tempY);
	}
	
	public void draw(Graphics g)
	{
		float r = getEnemyFleetRadius();
		g.setColor(new Color(255, 255, 255));
		g.drawOval(enemyFleetX - r, enemyFleetY - r, r*2, r*2);
		fightingRing.draw(g);
		healingRing.draw(g);
		sniperTower.draw(g);
		
		g.fillOval(snipers.getFleetCenter().getX(), snipers.getFleetCenter().getY(), 10, 10);
		g.fillOval(brawlers.getFleetCenter().getX(), brawlers.getFleetCenter().getY(), 10, 10);
		g.fillOval(healers.getFleetCenter().getX(), healers.getFleetCenter().getY(), 10, 10);
		g.fillOval(stingerLocation.getX() - 5, stingerLocation.getY() - 5, 10, 10);
		
		g.drawString("our power:   " + myPower, 0, 0);
		g.drawString("their power: " + theirPower, 0, 20);
	}
}
