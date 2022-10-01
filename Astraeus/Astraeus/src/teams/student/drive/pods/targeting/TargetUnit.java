package teams.student.drive.pods.targeting;

import components.weapon.Weapon;
import components.weapon.WeaponType;
import components.weapon.utility.Aegis;
import objects.entity.unit.Unit;
import teams.student.drive.controllers.attack.AttackController.DefenseType;
import teams.student.drive.pods.Pod;

public class TargetUnit implements Comparable<TargetUnit> {
	final private static float PriorityScaling = 10000000f;
	private Pod pod;
	
	public Unit unit;
	public float priority;
	
	public TargetUnit(Unit u, Pod pod) {
		this.pod = pod;
		
		this.unit = u;
		this.priority = calculatePriority();
	}

	public boolean usingAegis() {
		if(unit.hasWeaponOne() && unit.getWeaponOne() instanceof Aegis) { 
			if(unit.getWeaponOne().inUse()) return true; 
		}
		if(unit.hasWeaponTwo() && unit.getWeaponTwo() instanceof Aegis) { 
			if(unit.getWeaponTwo().inUse()) return true; 
		}
		return false;
	}
	
	private float calculatePriority() {
		final float damage = getDamageValue();
		final float defense = getDefenseValue();
		final float distance = pod.distanceTo(unit.getCenterX(), unit.getCenterY()) / 5f;
		
		// Base Priority
		float priority = damage / defense * PriorityScaling;
		
		// Priority Multipliers
		if(isSupport()) priority *= 1.5f; // Support 
		
		return priority;
	}
	
	private float getDamageValue() {
		float damage = 10f; // Minimum
		
		if(unit.hasWeaponOne()) { damage += unit.getWeaponOne().getDamage(); }
		if(unit.hasWeaponTwo()) { damage += unit.getWeaponTwo().getDamage(); }
		
		return damage;
	}
	private float getDefenseValue() {
		return unit.getMaxEffectiveHealth();
	}
	
	
	private boolean isSupport() {
		if(unit.hasWeaponOne() && unit.getWeaponOne().getWeaponType() == WeaponType.UTILITY) { return true; }
		else if(unit.hasWeaponTwo() && unit.getWeaponTwo().getWeaponType() == WeaponType.UTILITY) { return true; }
		else return false;
	}
	
	@Override
	public int compareTo(TargetUnit o) { return (int) (o.priority - this.priority) * 100; }
	
	
	
	
}