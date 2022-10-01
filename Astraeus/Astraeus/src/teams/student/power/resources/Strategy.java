package teams.student.power.resources;

import java.util.ArrayList;

import components.weapon.WeaponType;
import objects.entity.unit.Unit;
import teams.student.power.PowerPlayer;
import teams.student.power.units.brawlingUnits.Brawler;
import teams.student.power.units.brawlingUnits.Rock;
import teams.student.power.units.hitAndRun.Scout;
import teams.student.power.units.hitAndRun.Stinger;
import teams.student.power.units.resourceUnits.Camper;
import teams.student.power.units.resourceUnits.Gatherer;
import teams.student.power.units.resourceUnits.Miner;
import teams.student.power.units.sniperUnits.Frenchguy;
import teams.student.power.units.sniperUnits.Kabomber;
import teams.student.power.units.sniperUnits.Lancer;
import teams.student.power.units.supportUnits.Medic;

public class Strategy 
{
	ArrayList<Unit> enemies;
	private PowerPlayer player;
	private int delay; //so we load in the fighters second, based on the enemy's stuff

	private float oppurtunity;
	private Point rushRest;
	
	private int phase;
	private int armyCost;
	private int armySize;
	
	private int gathererCount;
	private int minerCount;
	
	private int brawlerCount;
	private int lancerCount;
	private int frenchguyCount;
	private int kabomberCount;
	private int medicCount;
	private int rockCount;
	
	public Strategy(PowerPlayer player)
	{
		this.player = player;
		delay = 0;
		phase = 0;
		armyCost = 0;
		
		oppurtunity = 1000;
		rushRest = new Point(6000, 0);
		
		gathererCount = 0;
		minerCount = 0;
		brawlerCount = 0;
		lancerCount = 0;
		frenchguyCount = 0;
		kabomberCount = 0;
		medicCount = 0;
		rockCount = 0;
	}
	
	public Point getRushRest() { return rushRest; }
	public int getArmySize() { return armySize; }
	public int getArmyCost() { return armyCost; }
	
	
	public void resManagerUpdate()
	{
		player.getResourceManager().update();
		ArrayList<Unit> myMiners = player.getMyUnits(Miner.class);
		for(Unit u : myMiners) {
			Miner m = (Miner) u;
			if(!m.getAssignedState()) {
				player.getResourceManager().assignBuddies(m);
				m.setAssignedState(true);
			}
		}
	}
	
	public void armyManagerUpdate() {
		player.getArmyManager().update();
	}

	public void checkPhase()
	{
		if(player.countAllUnits() > 35 && player.getMineralsMined() > 360)
		{
			phase = 4;
			
			brawlerCount = 4;
			rockCount = 4;
			lancerCount = 6;
			frenchguyCount = 4;
			kabomberCount = 10;
			medicCount = 2;
			
			minerCount = 10;
			gathererCount = 4;
		} 
		else if(player.countAllUnits() > 25 && player.getMineralsMined() > 270)
		{
			phase = 3;
			
			brawlerCount = 4;
			lancerCount = 6;
			rockCount = 3;
			frenchguyCount = 2;
			kabomberCount = 4;
			medicCount = 2;
			
			minerCount = 12;
			gathererCount = 5;
		} 
		else if(player.countAllUnits() > 15 && player.getMineralsMined() > 180)
		{
			phase = 2;
			
			brawlerCount = 4;
			lancerCount = 4;
			rockCount = 2;
			frenchguyCount = 1;
			kabomberCount = 2;
			medicCount = 2;
			
			minerCount = 8;
			gathererCount = 4;
		} 
		else if(player.countAllUnits() > 10 && player.getMineralsMined() > 90)
		{
			phase = 1;
			
			brawlerCount = 4;
			lancerCount = 2;
			rockCount = 1;
			frenchguyCount = 1;
			kabomberCount = 1;
			medicCount = 1;
			
			minerCount = 4;
			gathererCount = 3;
		} 
		else 
		{
			phase = 0;
			
			brawlerCount = 2; //2
			rockCount = 1;
			lancerCount = 1;
			frenchguyCount = 0;
			kabomberCount = 0;
			medicCount = 1;
			
			minerCount = 2;
			gathererCount = 3;
		}
		
		sumUnits();
	}
	
	public void sumUnits()
	{
		int tempArmySize = 0;
		int tempArmyCost = 0;
		
		for(int i = 0; i < brawlerCount; i++)	{	tempArmyCost += 7;	tempArmySize++; }
		
		for(int i = 0; i < lancerCount; i++) {	tempArmyCost += 6;	tempArmySize++; }
		
		for(int i = 0; i < frenchguyCount; i++)	{	tempArmyCost += 6;	tempArmySize++; }
		
		for(int i = 0; i < kabomberCount; i++)	{	tempArmyCost += 6;	tempArmySize++; }
		
		for(int i = 0; i < medicCount; i++)	{	tempArmyCost += 4;	tempArmySize++; }
		
		for(int i = 0; i < rockCount; i++)	{	tempArmyCost += 8;	tempArmySize++; }
		
		armySize = tempArmySize;
		armyCost = tempArmyCost;
	}
	
	public void strategy()
	{
		delay++;
		
		checkPhase();
		
		enemies = player.getMyBase().getEnemiesExcludeBaseShip();
		
		int wT = getWeaponType();
		int sT = getDefenseType();
		
		//if(player.countMyUnits() >= 50) return;
		
		if(player.getMyUnits(Camper.class).size() == 0) 
		{
			player.buildUnit(new Camper(player));
		}
		
		if(delay > 10)
		{
			mainArmySpawning(wT, sT);
		}	
		
		if(player.countMyUnits(Gatherer.class) < gathererCount)
		{
			player.buildUnit(new Gatherer(player));
		}
		
		if(player.countMyUnits(Miner.class) < minerCount)
		{
			player.buildUnit(new Miner(player));
		}
		
		//rushing the gatherers doesn't really work
		//pretty useless for anything that isn't stalling
		if(player.countMyUnits(Scout.class) < 1) //change to distress signal
		{
			player.buildUnit(new Scout(player, 0, sT));
		}
		
		if(player.countMyUnits(Scout.class) < 2 && phase > 0)
		{
			player.buildUnit(new Scout(player, 0, sT));
		}
		
		if(player.countMyUnits(Stinger.class) < 5 && phase > 2)
		{
			player.buildUnit(new Stinger(player, player.countMyUnits(Stinger.class) % 2, sT));
		}
	}
	
	public void mainArmySpawning(int wT, int sT)
	{
		if(player.countMyUnits(Kabomber.class) < kabomberCount)
		{
			player.buildUnit(new Kabomber(player, wT, sT));
		}
		
		if(player.countMyUnits(Lancer.class) < lancerCount)
		{
			player.buildUnit(new Lancer(player, wT, sT));
		}
		
		if(player.countMyUnits(Frenchguy.class) < frenchguyCount)
		{
			player.buildUnit(new Frenchguy(player, wT, sT));
			//the flakcannon has REALLY good survivability just cause it can slow, interesting to note
		}
		
		if(player.countMyUnits(Brawler.class) < brawlerCount)
		{
			player.buildUnit(new Brawler(player, wT, sT));
		}
		
		if(player.countMyUnits(Rock.class) < medicCount)
		{
			player.buildUnit(new Rock(player, wT, sT));
		}
		
		if(player.countMyUnits(Medic.class) < medicCount)
		{
			player.buildUnit(new Medic(player));
		}
	}
	
	public int getWeaponType() //0 for energy weapons, 1 for kinetic, might add a 2 for missiles
	{
		int type = 0;
		
		if(getEnemyPercent("shield") > 0.5f)
		{
			type = 0;
		}
		else if(getEnemyPercent("plating") > 0.5f)
		{
			type = 1;
		}
		else
		{
			type = 1;
		}
		
		return type;
	}
	
	public int getDefenseType() //0 for energy weapons, 1 for kinetic, might add a 2 for missiles
	{
		int type = 0;
		
		if(getEnemyPercent("energy") > 0.5f)
		{
			type = 0;
		}
		else if(getEnemyPercent("kinetic") > 0.5f)
		{
			type = 1;
		}
		else
		{
			type = 2;
		}
		
		return type;
	}
	
	public float getFleetPercent(Class <? extends Unit> clazz)
	{
		return ( (float) player.countMyUnits(clazz) ) / ( (float) player.countMyUnits() - 1);
	}
	
	
	//------------------------ information on enemy units ---------------------------

	
	public int getCount(String type) {
		int count = 0;

		for(Unit u : enemies) {
			switch(type) {
			case "plating":
				if(u.hasPlating()) {
					count++;
				}
				break;
				
			case "shield":
				if(u.hasShield()) {
					count++;
				}
				break;
				
			case "energy":
				if(u.hasEnergyDamage()) {
					count++;
				}
				break;
			
			case "kinetic":
				if(u.hasKineticDamage()) {
					count++;
				}
				break;
				
			case "explosive":
				if(u.hasExplosiveDamage()) {
					count++;
				}
			default:
				count = -1;
			}
		}
		return count;
	}
	
	//things to consider: excluding miners/resource units in the count
	public float getEnemyPercent(String type) {
		return (float) getCount(type) / (float) getHostileEnemies().size();
	}
	
	public ArrayList<Unit> getHostileEnemies()
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		ArrayList<Unit> curUnits = player.getEnemyUnits();
		
		for(Unit u : curUnits)
		{
			if(u.getWeaponOne() != null)
			{
				switch(u.getWeaponOne().getWeaponType())
				{
					case RESOURCE : break;
					default: units.add(u);
				}
			}
		}
		
		return units;
	}
	
	public Unit getVulnerableUnit() 
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> enemies = player.getEnemyUnits();
		boolean gathererFound = false;
		
		if(enemies.size() == 0) return null;
		
		for(Unit u : enemies)
		{
			if(player.getMyBase().getDistance(u) < nearestDistance 
					&& u.hasWeaponOne()
					&& u.getWeaponOne().getWeaponType() == WeaponType.RESOURCE 
					&& u.getDistance(getNearestHostileEnemy(u)) > oppurtunity)
			{
				nearestUnit = u;
				nearestDistance = player.getMyBase().getDistance(u);
				
				rushRest.setY(u.getCenterY() * 5000 / Math.abs(u.getCenterY()));
				gathererFound = true;
				oppurtunity = 200;
			} 
			else
			{
				if(!gathererFound) rushRest.setY(u.getCenterY() * 5000 / Math.abs(u.getCenterY()));
				oppurtunity = 2000;
			}
		}
		
		return nearestUnit;
	}
	
	public Unit getNearestHostileEnemy(Unit e) 
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> enemies = player.getEnemyUnits();
		
		if(enemies.size() == 0) return null;
		
		for(Unit u : enemies)
		{
			if(e.getDistance(u) < nearestDistance && u.hasWeaponOne()
					&& u.getWeaponOne().getWeaponType() != WeaponType.RESOURCE)
			{
				nearestUnit = u;
				nearestDistance = e.getDistance(u);
			}
		}
		
		return nearestUnit;
	}
}
