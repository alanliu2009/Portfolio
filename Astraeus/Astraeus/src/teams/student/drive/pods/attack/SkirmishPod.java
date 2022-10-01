package teams.student.drive.pods.attack;

import java.util.ArrayList;
import java.util.HashMap;

import components.weapon.WeaponType;
import components.weapon.resource.Collector;
import components.weapon.resource.MiningLaser;
import objects.entity.unit.Unit;
import teams.student.drive.DriveUnit;
import teams.student.drive.controllers.attack.*;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.Command;
import teams.student.drive.pods.Command.Action;
import teams.student.drive.pods.attack.AttackPod.Status;
import teams.student.drive.units.skirmish.SkirmishFella;
import teams.student.drive.units.economy.MiningFella;
import teams.student.drive.units.support.healers.ShieldFella;

public class SkirmishPod extends Pod {
	// Units in the Resource Pod
	private MacroController macro;
	
	// Pod target fleet
	EnemyFleet priorityFleet;
	Unit priorityUnit;
	
	// Units Present
	ArrayList<DriveUnit> unitsPresent;	
	
	// Skirmish Switch
	private Status status;
	
	// If enemy has more miners / collectors than other units, go skirmish
	public SkirmishPod(MacroController macro) {
		super(macro);
		
		this.status = Status.Defend;
		
		this.macro = macro;	
		this.unitsPresent = new ArrayList<>();
//		
//		this.newUnitGroup(SkirmishFella.class, 0);
	}
	
	public EnemyFleet getTargetFleet() { return priorityFleet; }
	public Unit getTargetUnit() { return priorityUnit; }
	public Status getStatus() { return status; }
	
	protected void action() {
		priorityFleet = findTargetFleet();
		if(priorityFleet == null) {
			status = Status.Attack;
			return;
		}
		
		// Behavior Determining
		priorityUnit = getPriorityTarget(priorityFleet);
		status = Status.Skirmish;
	}
	
	private Unit getPriorityTarget(EnemyFleet fleet) {
		Unit target = null;
		
		for(Unit u: fleet.getUnits()) {
			if(u.getWeapon(Collector.class) != null) {
				if(target == null || u.getCurEffectiveHealth() < target.getCurEffectiveHealth()) { target = u; }
			}
		}
		
		return target;
	}
	private EnemyFleet findTargetFleet() {
		EnemyFleet economyFleet = null;
		int count = 0;
		
		ArrayList<EnemyFleet> fleets = macro.getAttackController().getEnemyFleets();
		
		for(EnemyFleet fleet: fleets) {
			int newCount = countEconomy(fleet);
			
			if(newCount > count) { economyFleet = fleet; }
		}
		return economyFleet;
	}
	private int countEconomy(EnemyFleet fleet) {
		int count = 0;
		for(Unit u: fleet.getUnits()) {
			if(u.hasWeaponOne() && u.getWeaponOne() instanceof Collector) { count++; }
			if(u.hasWeaponTwo() && u.getWeaponTwo() instanceof Collector) { count++; }
		}
		return count;
	}
	
	
	// Calculate center of pod by chunking nearby units to existing center
	static private final float ChunkingRadius = 400f;
	private float newCenterX;
	private float newCenterY;
	private int centerCount;
	@Override
	protected void calculateCenters() {
		this.unitsPresent.clear();
		newCenterX = newCenterY = centerCount = 0;
		
		// If unit count = 0, reset center to BaseShip (Spawn)
		if(unitCount == 0) {
			centerX = macro.getPlayer().getMyBase().getCenterX();
			centerY = macro.getPlayer().getMyBase().getCenterY();
			
			return;
		}
		
		// HashMap of Grouped Units
		HashMap<Class<? extends DriveUnit>, boolean[]> grouped = new HashMap<Class<? extends DriveUnit>, boolean[]>();
		for(Class<? extends DriveUnit> clazz: units.keySet()) { // Initialization
			grouped.put( clazz, new boolean[units.get(clazz).size()] );
		}
		
		// Find the unit closest to the existing center
		DriveUnit closest = null;
		for(Class<? extends DriveUnit> clazz: units.keySet()) {
			for(DriveUnit u: units.get(clazz)) {
				if(closest == null || u.getDistance(centerX, centerY) < closest.getDistance(centerX, centerY)) {
					closest = u;
				}
			}
		}
		
		// Group around that unit
		groupUnitsAround(closest, grouped);
		
		// Finalize Center
		centerX = newCenterX / centerCount;
		centerY = newCenterY / centerCount;
	}
	
	// Chunking recursive helper method
	private void groupUnitsAround(DriveUnit u, HashMap<Class<? extends DriveUnit>, boolean[]> grouped) {
		unitsPresent.add(u);
		newCenterX += u.getCenterX();
		newCenterY += u.getCenterY();
		centerCount++;
		
		for(Class<? extends DriveUnit> clazz: units.keySet()) {
			for(int i = 0; i < units.get(clazz).size(); i++) {
				DriveUnit unit = units.get(clazz).get(i);
				
				if(grouped.get(clazz)[i]) continue;
				else if(u.getDistance(unit) < ChunkingRadius){
					grouped.get(clazz)[i] = true;
					groupUnitsAround(unit, grouped);
				}
			}
		}
	}
}
