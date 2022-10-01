package teams.student.drive.pods;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import components.DamageType;
import engine.Utility;
import teams.student.drive.units.economy.StationaryFella;
import teams.student.drive.DriveUnit;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.attack.EnemyFleet;

public abstract class Pod {
	/*
	 *  A pod coordinates behavior for a group of units.
	 */
	// Reference to Macrocontroller
	protected MacroController macroController;
	
	protected HashMap<Class<? extends DriveUnit>, ArrayList<DriveUnit>> units;
	protected HashMap<Class<? extends DriveUnit>, Integer> ledger;
	
	protected int unitCount;
	
	protected float centerX;
	protected float centerY;
	
	public Pod(MacroController macroController) { 
		this.macroController = macroController; // Initializing Macrocontroller
		
		this.ledger = new HashMap<Class<? extends DriveUnit>, Integer>();
		this.units = new HashMap<Class<? extends DriveUnit>, ArrayList<DriveUnit>>();
	} 
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.draw(new Circle(centerX, centerY, 25f));
	}
	
	// Update Method: Update strategies and behavior
	public void update() {
		cleanUp();
		calculateCenters();
		
		action();
	}
	
	// Method for the pod's behavior
	protected abstract void action();
	protected abstract void calculateCenters();
	private void cleanUp() {
		unitCount = 0;
		for(ArrayList<DriveUnit> unitList: units.values()) {
			unitList.removeIf(DriveUnit::isDead);
			unitCount += unitList.size();
		}
	}
	
	// Accessor Methods
	public float getCenterX() { return centerX; }
	public float getCenterY() { return centerY; }
	
	public Point getCenterPosition() { return new Point(centerX, centerY); }
	
	public int getUnitCount() { return unitCount;  }
	public int countUnits(Class<? extends DriveUnit> clazz) { return units.get(clazz).size(); }
	public int preferredCount(Class<? extends DriveUnit> clazz) { return ledger.get(clazz); }
	
	public ArrayList<DriveUnit> getUnitsOfClass(Class<? extends DriveUnit> clazz) { return units.get(clazz); }
	
	// Helper Methods
	public float distanceTo(float x, float y) { return Utility.distance(new Point(centerX, centerY), new Point(x,y)); }
	public void addUnit(Class<? extends DriveUnit> clazz, DriveUnit unit) { units.get(clazz).add(unit); }
	protected DriveUnit highestHealth(Class<? extends DriveUnit> clazz) {
		DriveUnit highestHealth = null;
		for(DriveUnit unit: units.get(clazz)) {
			if(highestHealth == null || highestHealth.getCurEffectiveHealth() > unit.getCurEffectiveHealth()) {
				highestHealth = unit;
			}
		}
		return highestHealth;
	}
	
	// Add a new unit group
	protected void newUnitGroup(Class<? extends DriveUnit> clazz, int preferredCount) {
		// Add to ledger and create a new unit group
		ledger.put(clazz, preferredCount);
		units.put(clazz, new ArrayList<DriveUnit>());
		
		// Create a queue unit to manage production
		createQueueUnit(clazz);
	}
	
	protected void createQueueUnit(Class<? extends DriveUnit> c) {
		// Create new unit
		DriveUnit unit = null;
		try {
			unit = (DriveUnit) c.getConstructors()[0].newInstance(macroController.getPlayer(), this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
		}
		
		macroController.requestUnit(unit); // Add to queue
	}	
	
	protected float getHealthValue(ArrayList<DriveUnit> unitList) {
		float healthValue = 0;
		for(DriveUnit unit: unitList) { healthValue += unit.getCurEffectiveHealth(); }
		return healthValue;
	}
	
	final static float ATTACK_MODIFIER = 1.5f;
	protected float getAttackValue(ArrayList<DriveUnit> unitList, EnemyFleet fleet) {
		float attackValue = 0;
		DamageType targetDamageType;
		switch (fleet.getPrimaryDefense()) {
			case Shield:
				targetDamageType = DamageType.ENERGY;
			case Plating:
				targetDamageType = DamageType.KINETIC;
			case Structure:
				targetDamageType = DamageType.EXPLOSIVE;
			default:
				targetDamageType = DamageType.NONE;
		}
		
		for(DriveUnit unit: unitList) {
			if (unit.hasWeaponOne()) {
				if (unit.getWeaponOne().getDamageType() == targetDamageType) {
					attackValue += unit.getWeaponOne().getDamage() * ATTACK_MODIFIER;
				}
				attackValue += unit.getWeaponOne().getDamage();
			}
			if (unit.hasWeaponTwo()) {
				if (unit.getWeaponOne().getDamageType() == targetDamageType) {
					attackValue += unit.getWeaponTwo().getDamage() * ATTACK_MODIFIER;
				}
				attackValue += unit.getWeaponTwo().getDamage();
			}
		}
		return attackValue;
	}
}