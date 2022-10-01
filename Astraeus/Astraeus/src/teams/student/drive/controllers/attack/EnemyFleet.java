package teams.student.drive.controllers.attack;

import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

import components.DamageType;
import components.weapon.Weapon;
import engine.Utility;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.Scrap;
import teams.student.drive.DriveUnit;
import teams.student.drive.controllers.attack.AttackController.DefenseType;

public class EnemyFleet {
	
	private ArrayList<Unit> enemyUnits;
	
	private float centerX, centerY;
	private float radius;
	private float fleetShield, fleetPlating, fleetStructure;
	private DefenseType fleetPrimaryDefense;
	
	public EnemyFleet(ArrayList<Unit> units) {
		this.enemyUnits = units;
		
		this.initialize();
	}
	
	public ArrayList<Unit> getUnits() { return enemyUnits; }
	public Unit closestUnit(Unit unit) {
		Unit closest = null;
		for(Unit u: enemyUnits) {
			if(closest == null || u.getDistance(unit) < closest.getDistance(unit)) {
				closest = u;
			}
		}
		return closest;
	}
	public int unitCount() { return enemyUnits.size(); }
	
	public float getCenterX() { return centerX; }
	public float getCenterY() { return centerY; }
	
	public float getRadius() { return radius; }
	
	private void initialize() {
		// Center X and Y of Chunk
		centerX = centerY = 0;
		
		for (Unit u: enemyUnits) {
			centerX += u.getCenterX();
			centerY += u.getCenterY();
		}
		
		centerX /= (float) enemyUnits.size();
		centerY /= (float) enemyUnits.size();
		
		// Find radius of the chunk
		Unit furthestEnemy = null;
		for(Unit u: enemyUnits) {
			if(furthestEnemy == null || Utility.distance(new Point(centerX, centerY), new Point(u.getCenterX(), u.getCenterY())) > radius) {
				furthestEnemy = u;
				radius = Utility.distance(new Point(centerX, centerY), new Point(u.getCenterX(), u.getCenterY()));
			}
		}
		this.radius += 25f; // Add some base radius
	}
	
	private void updateFleetDefenses() {
		fleetStructure = fleetShield = fleetPlating = 0;
		for (Unit u: getUnits()) {
			fleetStructure += u.getCurStructure();
			fleetShield += u.getCurShield();
			fleetPlating += u.getCurPlating();
		}
	}
	
	public DefenseType getPrimaryDefense() {
		updateFleetDefenses();
		if(fleetStructure * 0.1 > fleetShield + fleetPlating) { fleetPrimaryDefense = DefenseType.Structure; }
		else if(fleetShield > fleetPlating) { fleetPrimaryDefense = DefenseType.Shield; }
		else if (fleetPlating > fleetShield){ fleetPrimaryDefense = DefenseType.Plating; }
		else { fleetPrimaryDefense = DefenseType.None; }
		return fleetPrimaryDefense;
	}
	
	public float getHealthValue() {
		float healthValue = 0;
		for(Unit u: enemyUnits) {
			healthValue += u.getCurEffectiveHealth();
		}
		return healthValue;
	}
	public float getAttackValue() {
		float attackValue = 0;
		for(Unit u: enemyUnits) {
			if (u.hasWeaponOne()) {
				attackValue += u.getWeaponOne().getDamage();
			}
			if (u.hasWeaponTwo()) {
				attackValue += u.getWeaponTwo().getDamage();
			}
		}
		return attackValue;
	}
}