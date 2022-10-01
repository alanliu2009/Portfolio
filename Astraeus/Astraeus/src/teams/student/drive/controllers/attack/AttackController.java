package teams.student.drive.controllers.attack;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import components.DamageType;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import engine.Utility;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.attack.AttackController.DefenseType;
import ui.display.message.PlayerMessage;

public class AttackController {
	// Defense Type Enumerator
	public enum DefenseType { None, Shield, Plating, Structure }
	
	// Reference Variables
	private DrivePlayer player; // Player
	
	// Setting Variables
	private static final float ChunkingRadius = 500f;
	
	// All enemy fleets
	private ArrayList<EnemyFleet> fleets; 
	
	// Primary Strategy and Counter
	private Frame frame; // Primary frame to build 
	private Purpose strategy; // Primary Strategy for the Controller
	private ArrayList<Counter> counters; // Unit counter-building 
	
	/* Enemy Descriptor Values*/
	private float enemyFightValue; // Overall EnemyFight Value
	
	// Enemy Economy Values
	private int enemyEconomy;
	
	// Enemy Defensive Values
	private DefenseType enemyPrimaryDefense; // Enemy Primary Defense (Shield or Structure)
	private float enemyStructure;
	private float enemyShield;
	private float enemyPlating;
	
	// Enemy Damage Values
	private DamageType enemyPrimaryDamage; // Enemy Primary Damage (Proportion)
	private float enemyExplosive; // Explosive Proportion 
	private float enemyEnergy; // Energy Proportion
	private float enemyKinetic; // Kinetic Proportion
	
	// Other Enemy Values
	private float enemyFrame; // Enemy Average Frame
	private float enemySpeed; // Enemy Average Speed 
	private float avgFleetSize;
	
	// Constructor
	public AttackController(MacroController macroController) {		
		this.player = macroController.getPlayer();
		
		this.enemyPrimaryDamage = DamageType.NONE;
		this.enemyPrimaryDefense = DefenseType.None;
		
		this.fleets = new ArrayList<>();
		
		this.frame = Frame.MEDIUM;
		this.counters = new ArrayList<>();
		this.strategy = Purpose.None;
	}
	
	// Drawing
	public void draw(Graphics g) {
		// Circle enemy fleets
		g.setColor(Color.red);
		for(EnemyFleet fleet: fleets) { g.draw(new Circle(fleet.getCenterX(), fleet.getCenterY(), fleet.getRadius())); }
		
		// Draw Strategy and Counter
		Color color = Color.orange;
		player.addMessage(new PlayerMessage("Strategy: " + getStrategyString(), color));
		String counterString = "";
		for(Counter c: counters) {
			counterString += getCounterString(c);
		}
		player.addMessage(new PlayerMessage("Counters: " + counterString, color));
		player.addMessage(new PlayerMessage("Frame Counter: " + getFrameString(), color));
		player.addMessage(new PlayerMessage("----", color));
		
		// Draw Enemy Primary Defense
		switch(enemyPrimaryDefense) {
			default:
				color = Color.white;
				break;
			case Shield:
				color = Color.cyan;
				break;
			case Plating:
				color = Color.yellow;
				break;
		}
		player.addMessage(new PlayerMessage("Enemy Shield " + ((Float) enemyShield).toString(), color));
		player.addMessage(new PlayerMessage("Enemy Plating " + ((Float) enemyPlating).toString(), color));
		player.addMessage(new PlayerMessage("Enemy Structure " + ((Float) enemyStructure).toString(), color));
		player.addMessage(new PlayerMessage("----", color));
		
		// Draw Enemy Primary Damage
		switch(enemyPrimaryDamage) {
			default:
				color = Color.white;
				break;
			case EXPLOSIVE:
				color = Color.red;
				break;
			case KINETIC:
				color = Color.yellow;
				break;
			case ENERGY:
				color = Color.blue;
				break;
		}
		player.addMessage(new PlayerMessage("Enemy Explosive (Percent) " + ((Float) enemyExplosive).toString(), color));
		player.addMessage(new PlayerMessage("Enemy Energy (Percent) " + ((Float) enemyEnergy).toString(), color));
		player.addMessage(new PlayerMessage("Enemy Kinetic (Percent) " + ((Float) enemyKinetic).toString(), color));
		player.addMessage(new PlayerMessage("----", color));
		
		// Draw Enemy Speed
		color = Color.white;
		player.addMessage(new PlayerMessage("Enemy Speed " + enemySpeed, color));
		player.addMessage(new PlayerMessage("Enemy Frame " + enemyFrame, color));
	}
	
	// Accessor Methods
	public Frame getFrame() { return frame; }
	public Purpose getStrategy() { return strategy; }
	public ArrayList<Counter> getCounters() { return counters; }
	
	public int enemyEconomyValue() { return enemyEconomy; }
	
	public DamageType enemyPrimaryDamage() { return enemyPrimaryDamage; }
	public DefenseType enemyPrimaryDefense() { return enemyPrimaryDefense; }
	
	public float enemyEnergyValue() { return enemyEnergy; }
	public float enemyKineticValue() { return enemyKinetic; }
	public float enemyExplosiveValue() { return enemyExplosive; }
	
	public float getSpeedValue() { return enemySpeed; }
	
	public boolean hasExplosiveWeapons() { return enemyExplosive > 0f; }
	public boolean hasEnergyWeapons() { return enemyEnergy > 0f; }
	public boolean hasKineticWeapons() { return enemyKinetic > 0f; }
	
	public ArrayList<EnemyFleet> getEnemyFleets() { return fleets; }
	
	// Helper Methods
	private String getFrameString() {
		switch(frame) {
			default:
				return "None";
				
			case LIGHT:
				return "Light";
			case MEDIUM:
				return "Medium";
			case HEAVY:
				return "Heavy";
		}
	}
	public String getStrategyString() { // Get Strategy as a string
		switch(strategy) {
			default:
				return "None";
				
			case Attack:
				return "Attack";
			case Defense:
				return "Defense";
			case Economy:
				return "Economy";
			case Skirmish:
				return "Skirmish";
		}
	}
	public String getCounterString(Counter c) { // Get Counter as a String
		switch(c) {
			default:
				return "";
			
			case AntiClump:
				return " AntiClump";
			case AntiSpeed:
				return " AntiSpeed";
			case AntiTank:
				return " AntiTank";
			case AntiExplosive:
				return " AntiExplosive";
		}
	}
	

	public float fightValue(ArrayList<? extends Unit> unitList) {
		float healthValue = 0;
		float attackValue = 0;
		
		for(Unit unit: unitList) { // Calculate Health Value
			healthValue += unit.getCurEffectiveHealth(); 
			
			if (unit.hasWeaponOne()) { attackValue += unit.getWeaponOne().getDamage(); }
			if (unit.hasWeaponTwo()) { attackValue += unit.getWeaponTwo().getDamage(); }
		}
		
		return healthValue / attackValue;
	}
	public EnemyFleet getClosestFleet(float x, float y) {
		EnemyFleet closest = null;
		for(EnemyFleet fleet: fleets) {
			if(closest == null) {
				closest = fleet;
			} else if (
					Utility.distance(new Point(fleet.getCenterX(), fleet.getCenterY()), new Point(x, y)) < 
					Utility.distance(new Point(closest.getCenterX(), closest.getCenterY()), new Point(x, y))) {
				closest = fleet;
			}
		}
		return closest;
	}
	// Update Method: Chunk Enemies
	public void update() { this.chunk(); }
	
	// Chunking Algorithm
	private void chunk() {
		avgFleetSize = 0; 									// Set values to 0
		enemySpeed = enemyFrame = 0; 						// Set Values to 0
		enemyShield = enemyPlating = enemyStructure = 0;	// Set values to 0
		enemyExplosive = enemyEnergy = enemyKinetic = 0;	// Set values to 0
		 
		fleets.clear(); 									// Clear enemy fleets
		
		// Get all enemy units
		ArrayList<Unit> enemies = player.getEnemyUnits();
		boolean[] grouped = new boolean[enemies.size()];
		
		for(int i = 0; i < enemies.size(); i++) {
			// Get enemy unit
			Unit e = enemies.get(i);  
			if(e.isDead()) continue; // Ignore if dead
			else if(e instanceof BaseShip) continue; // Ignore if BaseShip
			else if(!e.isInBounds()) continue;
			
			// Calculate Enemy Values
			enemySpeed += e.getMaxSpeed(); // Speed Value 
			frameValue(e); // Frame Value
			defenseValue(e); // Defense Value
			damageValue(e); // Damage Value
			economyValue(e); // Economy Value
			
			// Begin chunking 
			if(grouped[i]) continue; // Ignore if already grouped
			
			ArrayList<Unit> group = new ArrayList<>(); // Create new unit group
			groupEnemiesAround(enemies.get(i), group, grouped); // Chunk
			
			avgFleetSize += group.size(); // Add to average fleet size
			fleets.add(new EnemyFleet(group)); // Add group to enemy fleet list	
		}
		
		// Finalizing Enemy Values
		avgFleetSize /= fleets.size(); // Average fleet size
		finalizeValues(enemies.size() - 1); // (Disclude BaseShip)
		
		// Determine if there are any counters
		determineCounters();
		
		// Determine what type of builds to produce
//		System.out.println(enemyFrame);
		switch(Math.round(enemyFrame)) {
			// Default: Medium Build
			default:
				frame = Frame.LIGHT;
				break;
			
			// Light Frames: Light Build
			case 1:
				frame = Frame.LIGHT;
				break;
				
			// Medium Frames: Medium Build
			case 2:
				frame = Frame.MEDIUM;
				break;
				
			// Heavy Frames: Medium Build
			case 3:
				frame = Frame.MEDIUM; 
				break;
				
			// Assault Frames: Heavy Build
			case 4:
				frame = Frame.HEAVY;
				break;
		}
	}
	
	// Chunking recursive helper method
	private void groupEnemiesAround(Unit u, ArrayList<Unit> group, boolean[] grouped) {
		group.add(u);
		
		for(int i = 0; i < player.getEnemyUnits().size(); i++) {
			if(grouped[i]) continue;
			
			Unit u2 = player.getEnemyUnits().get(i);
			if(u2.isDead()) continue; // Ignore if dead
			else if(u2 instanceof BaseShip) { continue; } // Ignore if BaseShip
			
			if(Utility.distance(u, u2) <= ChunkingRadius) {
				grouped[i] = true;
				groupEnemiesAround(u2, group, grouped);
			}
		}
	}
	
	// Determining Counters
	private void determineCounters() {
		counters.clear();
		
		if(enemySpeed > 2.35f) counters.add(Counter.AntiSpeed);
		if(avgFleetSize > 5) counters.add(Counter.AntiClump);
	}
	// Finalize every enemy value
	private void finalizeValues(int enemyCount) {
		enemyFrame /= enemyCount; // Average Frame Value
		enemySpeed /= enemyCount; // Average Speed
		
		// Find damage proportions
		float sum = enemyEnergy + enemyKinetic + enemyExplosive;
		enemyEnergy /= sum;
		enemyKinetic /= sum;
		enemyExplosive /= sum;
		
		// Determine primary damage type
		if(enemyKinetic >= 0.5f) enemyPrimaryDamage = DamageType.KINETIC;
		else if(enemyEnergy >= 0.5f) enemyPrimaryDamage = DamageType.ENERGY;
		else if(enemyExplosive >= 0.5f) enemyPrimaryDamage = DamageType.EXPLOSIVE;
		else enemyPrimaryDamage = DamageType.NONE;
		
		// Determine enemy primary defensive type
		if(enemyStructure * 0.1 > enemyShield + enemyPlating) { enemyPrimaryDefense = DefenseType.Structure; }
		else if(enemyShield > enemyPlating) { enemyPrimaryDefense = DefenseType.Shield; }
		else if (enemyPlating > enemyShield){ enemyPrimaryDefense = DefenseType.Plating; }
		else { enemyPrimaryDefense = DefenseType.None; }
	}
	
	private void economyValue(Unit u) {
		if(u.hasWeaponOne() && u.getWeaponOne().getWeaponType() == WeaponType.RESOURCE) { this.enemyEconomy += 1; }
		if(u.hasWeaponTwo() && u.getWeaponTwo().getWeaponType() == WeaponType.RESOURCE) { this.enemyEconomy += 1; }
	}
	private void frameValue(Unit u) {
		switch(u.getFrame()) {
			default:
				break;
		
			case LIGHT:
				enemyFrame += 1f;
				break;	
			case MEDIUM:
				enemyFrame += 2f;
				break;
			case HEAVY:
				enemyFrame += 3f;
				break;
			case ASSAULT:
				enemyFrame += 4f;
				break;
		}
	}
	private void defenseValue(Unit u) {
		enemyStructure += u.getMaxStructure();
		enemyShield += u.getMaxShield();
		enemyPlating += u.getMaxPlating();
	}
	private void damageValue(Unit u) {
		if(u.hasWeaponOne()) addWeaponValue(u.getWeaponOne());
		if(u.hasWeaponTwo()) addWeaponValue(u.getWeaponTwo());
	}
	// Adding enemy damage values
	private void addWeaponValue(Weapon w) {
		switch(w.getDamageType()) {
			default:
				break;
				
			case EXPLOSIVE:
				enemyExplosive += w.getDamage();
				break;
				
			case ENERGY:
				enemyEnergy += w.getDamage();
				break;
				
			case KINETIC:
				enemyKinetic += w.getDamage();
				break;
		}
	}
}
