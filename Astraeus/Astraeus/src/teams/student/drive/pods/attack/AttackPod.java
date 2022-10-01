package teams.student.drive.pods.attack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import components.DamageType;
import components.weapon.utility.RepairBeam;
import engine.Utility;
import objects.GameObject;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.attack.AttackController;
import teams.student.drive.controllers.attack.AttackController.DefenseType;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.Command.Action;
import teams.student.drive.pods.Command;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.pods.targeting.TargetUnit;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.units.attack.tank.*;
import teams.student.drive.units.attack.fighter.*;
import teams.student.drive.units.attack.scout.BurningFella;
import teams.student.drive.units.attack.scout.ConfusedFella;
import teams.student.drive.units.attack.scout.LightScout;
import teams.student.drive.units.attack.sniper.*;
import teams.student.drive.units.economy.StationaryFella;
import teams.student.drive.units.economy.MiningFella;
import teams.student.drive.units.economy.PoorFella;
import teams.student.drive.units.support.healers.HealFella;
import teams.student.drive.units.support.healers.ShieldFella;
import teams.student.drive.units.support.other.AntiMissile;
import teams.student.drive.units.support.other.PullUpNoCap;
import teams.student.drive.units.support.other.Stunner;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperPoint;
import ui.display.message.PlayerMessage;

public class AttackPod extends Pod {
	// Attack Pod Battle Variables
	public static float HealingRadius = RepairBeam.MAX_RANGE * 2;
	public static float ProtectRadius = 1250f; 
	public static float AggroRadius = 1250f;
	
	public enum Status { Win, Attack, Defend, Retreat, Skirmish }
	public Status status;
	
	// Classes 
	private List<Class<? extends DriveUnit>> tankClasses;
	private List<Class<? extends DriveUnit>> fighterClasses;
	private List<Class<? extends DriveUnit>> sniperClasses;
	private List<Class<? extends DriveUnit>> supportClasses;
	
	// References
	private MacroController macro; 
	
	private AttackController attackController;
	
	private ResourcePod resourcePod;
	private BaseShip homeBase;
	
	// Units at the Pod Center
	private ArrayList<DriveUnit> unitsPresent;
	private float radius; // Radius of Pod
	
	// EnemyFleet
	private EnemyFleet closestFleet; 
	private ArrayList<TargetUnit> priorityEnemies; // List of enemies to prioritize
	
	// Positioning Variables
	private float frontX;
	private float frontY;
	
	private float backX; 
	private float backY;
	
	public AttackPod(MacroController macro) {
		super(macro);
		
		// Initializing Classes for Reference
		this.tankClasses = new ArrayList<>();
		tankClasses.add(BiggestFella.class);
		tankClasses.add(BiggerFella.class);
		tankClasses.add(BigFella.class);
		
		this.fighterClasses = new ArrayList<>();
		fighterClasses.add(StrongestFella.class);
		fighterClasses.add(StrongerFella.class);
		fighterClasses.add(StrongFella.class);
		
		this.sniperClasses = new ArrayList<>();
		sniperClasses.add(LongestFella.class);
		sniperClasses.add(LongerFella.class);
		sniperClasses.add(LongFella.class);
		
		this.supportClasses = new ArrayList<> ();
		supportClasses.add(HealFella.class);
		supportClasses.add(ShieldFella.class);
		supportClasses.add(PullUpNoCap.class);
		
		// Initial Status - Defend
		this.status = Status.Defend;
		
		// Units Around Center
		this.unitsPresent = new ArrayList<>();
		this.priorityEnemies = new ArrayList<>();
		
		// References
		this.macro = macro;
		
		this.resourcePod = macro.getResourcePod();
		
		this.attackController = macro.getAttackController();
		this.homeBase = macro.getPlayer().getMyBase();
		
		newUnitGroup(BigTimeFella.class, 10);
		
		// Tanks
		newUnitGroup(BigFella.class, 2);
		newUnitGroup(BiggerFella.class, 2);
		newUnitGroup(BiggestFella.class, 2);
		
		// Fighters
		newUnitGroup(StrongFella.class, 4);
		newUnitGroup(StrongerFella.class, 4);
		newUnitGroup(StrongestFella.class, 4);
		
		// Snipers
		newUnitGroup(LongFella.class, 4);
		newUnitGroup(LongerFella.class, 4);
		newUnitGroup(LongestFella.class, 4);
		
		// Scouts
		newUnitGroup(BurningFella.class, 1);
		newUnitGroup(ConfusedFella.class, 1);
		
		// Supports 
		newUnitGroup(PullUpNoCap.class, 2); // Puller
		newUnitGroup(HealFella.class, 1); // Structure Healer
		newUnitGroup(ShieldFella.class, 1); // Shield Healer
//		newUnitGroup(Stunner.class, 2);
//		newUnitGroup(AntiMissile.class, 1); // AntiMissile Unit
		
		// Trolling
		
//		newUnitGroup(BigTimeFella.class, 1);
	}

	public void draw(Graphics g) {
		macro.getPlayer().addMessage(new PlayerMessage("Attack Pod Status: " + getStatusString(), Color.white));
		if(closestFleet != null) {
			Unit highest = this.highestPriority();
			g.setColor(Color.red);
			g.draw(new Circle(highest.getCenterX(), highest.getCenterY(), 25f));
			
			macro.getPlayer().addMessage(new PlayerMessage("Units Present: " + unitsPresent.size(), Color.white));
			macro.getPlayer().addMessage(new PlayerMessage("Pod Fight Value: " + getHealthValue(unitsPresent) / closestFleet.getAttackValue(), Color.white));
			macro.getPlayer().addMessage(new PlayerMessage("Fleet Fight Value: " + closestFleet.getHealthValue() / getAttackValue(unitsPresent, closestFleet), Color.white));
		}
		
		// Aggro Radius
		g.setColor(Color.red);
		this.radius = 0;
		DriveUnit furthestUnit = null;
		for(DriveUnit u: unitsPresent) {
			if(furthestUnit == null || Utility.distance(new Point(centerX, centerY), new Point(u.getCenterX(), u.getCenterY())) > radius) {
				furthestUnit = u;
				radius = Utility.distance(new Point(centerX, centerY), new Point(u.getCenterX(), u.getCenterY()));
			}
		}
		this.radius += 25f;
		
		g.draw(new Circle(centerX, centerY, radius + AggroRadius));
		
//		g.setColor(Color.white);
//		g.fill(new Circle(frontX, frontY, 25f));
//		g.fill(new Circle(backX, backY, 25f));
	}
	private String getStatusString() {
		switch(status) {
		default: 
			return "None";
		
		case Win:
			return "Winning - GG!";
		case Attack:
			return "Attacking";
		case Defend:
			return "Defending";
		case Retreat:
			return "Retreating";
		}
	}
	
	// Accessor Methods	
	public List<Class<? extends DriveUnit>> getTankClasses() { return tankClasses; }
	public List<Class<? extends DriveUnit>> getFighterClasses() { return fighterClasses; }
	public List<Class<? extends DriveUnit>> getSniperClasses() { return sniperClasses; }
	
	public EnemyFleet getEnemyFleet() { return closestFleet; }
	public ArrayList<TargetUnit> getPriorityEnemies() { return priorityEnemies; }
	
	public ResourcePod getResourcePod() { return resourcePod; }
	public Status getStatus() { return status; }
	
	public float getRadius() { return radius; }
	public float getFrontX() { return frontX; }
	public float getFrontY() { return frontY; }
	public float getBackX() { return backX; }
	public float getBackY() { return backY; }
	
	public Point getFrontPosition() { return new Point(frontX, frontY); }
	
	// Helper Methods
	public Unit highestPriority() { return priorityEnemies.get(0).unit; }
	public Unit highestPriorityWithinRadius(DriveUnit driveUnit, float radius) {
		Unit highestPriority = null;
		float priority = 0f;
		
		for(TargetUnit target: priorityEnemies) {
			Unit unit = target.unit;
			if(driveUnit.getDistance(unit) < radius && target.priority > priority) {
				highestPriority = unit;
				priority = target.priority;
			}
		}
		
		return highestPriority;	
	}
	public DriveUnit closestTank(DriveUnit unit) { return null; } // Closest tank to this unit
	public DriveUnit closestSniper(DriveUnit unit) { return null; } // Closest sniper to this unit
	public DriveUnit closestFighter(DriveUnit unit) { return null; } // Closest fighter to this unit
	
	public DriveUnit mostDamagedAlly(DefenseType defense, DriveUnit unit) { // Most damaged ally of some defense type
		DriveUnit mostDamaged = null;
		float healthLeft = 1f;
		
		ArrayList<Class<? extends DriveUnit>> priorityClasses = new ArrayList<Class<? extends DriveUnit>>();
		priorityClasses.addAll(tankClasses);
		priorityClasses.addAll(fighterClasses);
		priorityClasses.addAll(sniperClasses);
		priorityClasses.addAll(supportClasses);
		
		for(Class<? extends DriveUnit> clazz: priorityClasses) {
			for(DriveUnit u: units.get(clazz)) {
				if(u.equals(unit)) continue;
				
				float health = 1f;
				switch(defense) {
					default:
						break;
					
					case Plating:
						health = (u.getCurStructure() + u.getCurPlating()) / (u.getMaxStructure() + u.getMaxPlating());
						break;
					case Shield:
						health = u.getCurShield() / u.getMaxShield();
						break;
				}
				if(health != 1f && health < healthLeft) mostDamaged = u;
			}
			if(mostDamaged != null) return mostDamaged;
		}
		
		return mostDamaged;
	}
	public boolean hasHealer(DefenseType defense) { return false; } // True if the pod has any healer
	
	public ArrayList<Class<? extends DriveUnit>> getSubClasses(Class<? extends DriveUnit> main) { // Get the SubClasses for a Class
		ArrayList<Class<? extends DriveUnit>> classes = new ArrayList<>();
		
		for(Class<? extends DriveUnit> clazz: units.keySet()) {
			if(main.isAssignableFrom(clazz)) { classes.add(clazz); }
		}
	
		return classes;
	}
	
	
	protected void action() {
//		updateFrontPosition();
//		updateBackPosition();
		if(resourcePod == null) return; // Failsafe

		// Find Closest Fleet
		closestFleet = attackController.getClosestFleet(centerX, centerY);
		
		if(closestFleet == null) endGame(); // If there are no fleets, end game
		else {
			// Determine Priority of Fleet Units
			priorityEnemies.clear();
			for(Unit u: closestFleet.getUnits()) {
				priorityEnemies.add(new TargetUnit(u, this));
			}
			Collections.sort(priorityEnemies);
			
			// Determine Behavior of Pod
			determineBehavior();
		}
	}
	
	// End Game
	private void endGame() {
		status = Status.Win;
		
		
		
		int count = 0;
		float displacement = 0f;
		
		ArrayList<AttackUnit> attackUnits = new ArrayList<>();
		
		for (ArrayList<DriveUnit> list: units.values()) {
			for (DriveUnit u: list) {
				if (u instanceof AttackUnit && !(u instanceof BigTimeFella)) {
					attackUnits.add((AttackUnit) u);
				}
			}
		}
		
		boolean hasSwitched = false;
		for(int i = 0; i < attackUnits.size(); i++) {
			AttackUnit a = attackUnits.get(i);
			
			
			if(i < attackUnits.size() * 0.70f) {
				a.setEnd(0f, -displacement);
			} else {
				if (!hasSwitched) {
					displacement = 0;
					hasSwitched = true;
				}
				a.setEnd(displacement, 0f);
			}
			displacement += 35f;
		}
	}
	// Pod Behavior Determining
	private void attack() { status = Status.Attack; }
	private void defend() { status = Status.Defend; }
	private void retreat() { status = Status.Retreat; }
	private void determineBehavior() {
		float fightValue = getHealthValue(unitsPresent) / closestFleet.getAttackValue();
		float fleetFightValue = closestFleet.getHealthValue() / getAttackValue(unitsPresent, closestFleet);
		
		// Increase fighting chance if more units
		if(macro.getPlayer().countMyUnits() > macro.getPlayer().countEnemyUnits() * 1.35f) { fightValue *= 10f; }
		else if(macro.getPlayer().countMyUnits() == 50) { fightValue *= 10f; }
		
		// if there is an enemy fleet within base range retreat
		for (EnemyFleet fleet: macro.getAttackController().getEnemyFleets()) {
			if (Utility.distance(new Point(fleet.getCenterX(), fleet.getCenterY()), macro.getPlayer().getMyBase().getPosition()) < AggroRadius) {
				this.closestFleet = fleet; 
				attack();
				return;
			}
		}
		
		// Behavior dependent on previous status
		switch(status) {
			// Default (No Status, FailSafe): Defend 
			default:
				defend();
				break;
			
			// Attacking Behavior
			case Attack:
				if(macro.getPlayer().getEnemyBase().getDistance(closestFleet.getCenterX(), closestFleet.getCenterY()) < 1000f) fleetFightValue *= 2f;
				
				// If fight value larger, continue Attacking
				if(fightValue > fleetFightValue) { attack(); }
				
				// Else, begin retreating
				else { retreat(); }
				break;
				
			// Defending Behavior
			case Defend:
				// If fleet is too close to resource pod, always attack
				if(resourcePod.distanceTo(closestFleet.getCenterX(), closestFleet.getCenterY()) < radius + AggroRadius) { attack(); } 
				
				// Or if fleet is too close to base
				else if(homeBase.getDistance(closestFleet.getCenterX(), closestFleet.getCenterY()) < radius + AggroRadius) attack();
				else {
					// If fight value is significantly larger, go on the offense
					if(fightValue > fleetFightValue * 1.25f) attack();
					
					// Else if enemy fleet is closeby
					else if(distanceTo(closestFleet.getCenterX(), closestFleet.getCenterY()) < radius + AggroRadius) {
						// If fight value is slightly greater, attack
						if(fightValue > fleetFightValue) attack();
						// Else, retreat
						else retreat();
					} else {
						// If nothing else, just keep defending
						defend();
					}
				}
				break;
				
			// Retreating Behavior
			case Retreat:
				// If fleet is too close to resource pod, always attack
				if(resourcePod.distanceTo(closestFleet.getCenterX(), closestFleet.getCenterY()) < radius + AggroRadius) { attack(); } 
				// Or if fleet is too close to base
				else if(homeBase.getDistance(closestFleet.getCenterX(), closestFleet.getCenterY()) < radius + AggroRadius) attack();
				// If fleet is far away, move to defend
				else if(distanceTo(closestFleet.getCenterX(), closestFleet.getCenterY()) > (radius + AggroRadius) * 2f) defend();
				else {
					// If fight value is significantly larger, attack
					if(fightValue > fleetFightValue * 1.25f) attack();
					// If not, continue retreating
					else retreat();
				}
				break;
		}
	}
	
	// Calculate center of pod by chunking nearby units to existing center
	static private final float ChunkingRadius = 650f;
	private float newCenterX;
	private float newCenterY;
	private int centerCount;
	@Override
	protected void calculateCenters() {
		this.unitsPresent.clear();
		newCenterX = newCenterY = centerCount = 0;
		
		// If unit count = 0, reset center to BaseShip (Spawn)
		if(unitCount == 0) {
			centerX = homeBase.getCenterX();
			centerY = homeBase.getCenterY();
			
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
	
	private void updateBackPosition() {
		ArrayList<List<Class<? extends DriveUnit>>> classList = new ArrayList<List<Class<? extends DriveUnit>>>();
		classList.add(this.sniperClasses);
		classList.add(this.fighterClasses);
		classList.add(this.tankClasses);
		
		for(List<Class<? extends DriveUnit>> classes: classList) {
			float totalX = 0f;
			float totalY = 0f;
			
			int count = 0;
			
			for(DriveUnit u: unitsPresent) {
				for(Class<? extends DriveUnit> clazz: classes) {
					if(u.getClass().isAssignableFrom(clazz)) {
						totalX += u.getCenterX();
						totalY += u.getCenterY();
						
						count++;
						
						break;
					}
				}
			}
			
			if(count > 0) {
				backX = totalX / count;
				backY = totalY / count;
				
				break;
			}
		}
	}
	private void updateFrontPosition() {
		ArrayList<List<Class<? extends DriveUnit>>> classList = new ArrayList<List<Class<? extends DriveUnit>>>();
		classList.add(this.tankClasses);
		classList.add(this.fighterClasses);
		classList.add(this.sniperClasses);
		
		for(List<Class<? extends DriveUnit>> classes: classList) {
			float totalX = 0f;
			float totalY = 0f;
			
			int count = 0;
			
			for(DriveUnit u: unitsPresent) {
				for(Class<? extends DriveUnit> clazz: classes) {
					if(u.getClass().isAssignableFrom(clazz)) {
						totalX += u.getCenterX();
						totalY += u.getCenterY();
						
						count++;
						
						break;
					}
				}
			}
			
			if(count > 0) {
				frontX = totalX / count;
				frontY = totalY / count;
				
				break;
			}
		}
		
//		ArrayList<DriveUnit> tankUnits = new ArrayList<DriveUnit>();
//		float totalX = 0;
//		float totalY = 0;
//		for (DriveUnit u: unitsPresent) {
//			if (u.getType() == Type.Tank) {
//				tankUnits.add(u);
//				totalX += u.getCenterX();
//				totalY += u.getCenterY();
//			}
//		}
//		frontX = totalX / tankUnits.size();
//		frontY = totalY / tankUnits.size();
	}
		
}
