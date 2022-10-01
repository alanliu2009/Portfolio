package teams.student.drive;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import components.DamageType;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.kinetic.Railgun;
import components.weapon.resource.MiningLaser;
import engine.states.Game;
import engine.Utility;
import engine.Values;
import objects.GameObject;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Unit;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.attack.AttackController;
import teams.student.drive.controllers.attack.AttackController.DefenseType;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.controllers.production.ProductionController;
import teams.student.drive.controllers.resource.ResourceController;
import teams.student.drive.pods.Command;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.Command.Action;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperCircle;
import teams.student.drive.utility.shapes.HelperLine;
import teams.student.drive.utility.shapes.HelperPoint;
import teams.student.drive.utility.shapes.HelperVector;

public abstract class DriveUnit extends Unit 
{	
	protected AttackController attack;
	protected ResourceController resource;
	protected ProductionController production;
	
	protected final static float ActionRadius = 20f; // Drawing for action
	protected final static float LeaderRadius = 25f; // Drawing for Leader
	
	// Setting Variables
	protected final static float ProtectRadius = 1000f;
	protected final static float HoverRadius = 750f;
	
	// Unit's Intended Counter
	public enum Counter { None, AntiClump, AntiSpeed, AntiTank, AntiExplosive }
	// Purpose of the Unit
	public enum Purpose { None, Attack, Defense, Economy, Skirmish }
	// Type of Unit
	public enum Type { None, Tank, Sniper, Fighter, Resource, Support, Scout }
	
	// Reference to DrivePlayer
	protected DrivePlayer player;
	
	// Unit's Pod
	protected Pod pod;
	
	// Unit's Movement Target Point
	protected HelperPoint target; 
	
	// The unit's time alive
	protected float initialTime;
	protected float aliveTime;
	
	// Weapon Management
	protected ArrayList<Weapon> weapons;
	
	protected float minAttackRadius;
	protected float maxAttackRadius;
	
	protected float avgMaxWeaponRadius;
	protected float avgMinWeaponRadius;
	
	// Unit Descriptors
	protected Purpose purpose; // Purpose of the Unit
	protected Type type; // Type of the unit
	protected float basePriority; // Base Priority of the Unit
	
	public DriveUnit(DrivePlayer p, Pod pod)  
	{
		super(p);
		
		// Player and Pod Reference
		this.player = p;
		this.attack = p.getMacroController().getAttackController();
		this.production = p.getMacroController().getProductionController();
		this.resource = p.getMacroController().getResourceController();
		
		this.pod = pod;
		
		// Default AntiClump Setting
		this.antiClump = true;
		this.antiClumpRadius = 80;
		
		// Default Target: Unit
		this.target = new HelperPoint(this);
		
		// Track time alive
		this.aliveTime = 0;
		
		// Weapons
		this.weapons = new ArrayList<>();
		
		this.type = Type.None;
	}
	
	// Inherited Methods
	public abstract float getBasePriority();
	public abstract Frame getFrameCounter();
	public abstract Purpose getPurpose();
	public abstract Type getType();
	
	protected abstract void unitAI(); // Specific AI for each unit
	protected abstract void drawForUnit(Graphics g);  // Specific draw method for each unit to inherit
	
	protected abstract void frameAndStyle(); // Design - Frame and Style
	protected abstract void determineWeapons(); // Design - Weapons
	protected abstract void determineUpgrades(); // Design - Upgrades
	
	public abstract int mineralValue(); // Mineral Value of Unit
		
	// Design of the DriveUnit
	public void design() {
		frameAndStyle(); // Determine frame and style
		determineWeapons(); // Determine unit's weapons
		determineUpgrades(); // Determine unit's upgrade
		
		// Weapon Management
		minAttackRadius = Game.getMapWidth();
		maxAttackRadius = Game.getMapWidth();
		
		if(hasWeaponOne()) {
			Weapon w = getWeaponOne();
			
			// Set minimum and maximum attack radius
			minAttackRadius = w.getMinRange();
			maxAttackRadius = w.getMaxRange();
			
			weapons.add(w);
		}
		if(hasWeaponTwo()) {
			Weapon w = getWeaponTwo();
			
			// Check if minimum and maximum attack radii are less and if so set them
			if(w.getMinRange() < minAttackRadius) minAttackRadius = w.getMinRange();
			if(w.getMaxRange() < maxAttackRadius) maxAttackRadius = w.getMaxRange();
			
			weapons.add(w);
		}
	}

	// Helper Methods
	protected float getCirclingRadius() { return getCurSpeed() * getCurSpeed() / getAcceleration(); }
	
	// Accessor Methods
	public Pod getPod() { return pod; }
	
	// Set Target Methods
	public void setTarget(Point point) {
		this.target.x = point.getX();
		this.target.y = point.getY();
	}
	public void setTarget(float x, float y) {
		this.target.x = x;
		this.target.y = y;
	}
	
	public void setTarget(GameObject o) {
		this.target.x = o.getCenterX();
		this.target.y = o.getCenterY();
	}
	
	// Ran every frame, determines behavior of unit
	public void action() {
		aliveTime++; // Update time alive (frames alive)
		unitAI(); // Call unit AI
		movement(); // Call movement method; can be overwritten in the unitAI() method
	}
	
	
	/* Default Movement Code */
	protected boolean antiClump;
	protected int antiClumpRadius;
	
	private float pivotX, pivotY; // Circle pivot x and y
	private float radius; // Circle radius
	
	protected static final float MinimumAngle = (float) Math.PI / 720; // Minimum angle necessary to stop circling and move to target, 0.25 degrees
	private static final float MinSpeed = 0.75f; // Minimum speed necessary for circling
	
	protected void clump() { antiClump = false; }
	protected void antiClump() { antiClump = true; }
	protected void movement() { // Unit general movement
		// AntiClump
		if(antiClump) {
			this.antiClump(antiClumpRadius);
		}
		
		// Create the unit's velocity and target vector relative to unit
		final HelperVector VelocityVector = new HelperVector(this.getSpeedX(), this.getSpeedY());
		final HelperVector TargetVector = new HelperVector(target.x - this.getCenterX(), target.y - this.getCenterY());
		
		final float MinimumAngle = (float) Math.PI / 720; // Minimum angle necessary to stop circling and move to target; 0.25 degrees
		final float MinimumSpeed = getMaxSpeed() * MinSpeed; // Minimum speed necessary for circling
		
		final float CurrentSpeed = getCurSpeed(); // Current speed of unit
		final float Acceleration = getAcceleration(); // Acceleration of unit
		
		/* -- Primary Goal: Have a velocity that goes straight towards the target point -- */
		if(DriveHelper.angleBetweenVectors(VelocityVector, TargetVector) <= MinimumAngle) {
			this.moveTo(target.x, target.y);
			return;
		}
		
		/* -- Circle Calculations -- */
		// Else, perform calculations necessary for circling
		float CircleDirection = DriveHelper.crossProductK(VelocityVector, TargetVector); // Determine direction of circle
		
		// Find radius of circle
		radius = CurrentSpeed * CurrentSpeed / Acceleration; // r = v^2 / a ; derived from centripedal acceleration
		
		// Find angle perpendicular to velocity vector
		float angle = (float) Math.atan2(VelocityVector.y, VelocityVector.x) + (float) Math.PI / 2f;
		if(CircleDirection < 0) angle += Math.PI; // Flip angle by 180 degrees if target point is to the left of the line
		
		// Find point of pivot
		pivotX = this.getCenterX() + (float) Math.cos(angle) * radius;
		pivotY = this.getCenterY() + (float) Math.sin(angle) * radius;
		
		/* -- Behavior Determining -- */
		// If target is in the pivot circle
		if(Utility.distance(new Point(pivotX, pivotY), new Point(target.x, target.y)) <= radius) {
			this.moveTo(getCenterX() - getSpeedX(), getCenterY() - getSpeedY()); // Slow down in a straight line
		} 
		// Else if moving too slow, just move directly to target
		else if (CurrentSpeed < MinimumSpeed) { this.moveTo(target.x, target.y); } 
		// Else, pivot in a circle to reach the target
		else { this.moveTo(pivotX, pivotY); }	
		
	}
	
	// Other Movement Methods
	// Speed Up and Slow Down
	protected void speedUp(float speed) { if(this.getCurSpeed() < speed * 0.95f) { move(); } }
	protected void slowDown() { this.moveTo(x - xSpeed, y - ySpeed); }
	
	protected void circle(float x, float y, float radius) {
		
	}
	// Hovering
	float hoverX;
	float hoverY;
	
	protected void hover() { // Hover - Start Circling in Place
		final float CircleRadius = getCurSpeed() * getCurSpeed() / getAcceleration();
		
		// Find angle of movement
		float angle = DriveHelper.atan(getSpeedY(), getSpeedX());
		angle += Math.PI / 2;  // Offset angle by 90 degrees
		
		// Set target point
		this.setTarget(getCenterX() + DriveHelper.cos(angle) * CircleRadius, getCenterY() + DriveHelper.sin(angle) * CircleRadius);
		this.moveTo(target.x, target.y);
	}
	
//	protected void moveToAngle(Unit u, float angle, float radius) {
//		// Find angle between unit and x,y point
//		float angle = DriveHelper.atan(y - u.getCenterY(), x - u.getCenterX()) + (float) Math.PI;
//		
//		// Set target to unit u at a radius r
//		this.setTarget(u.getCenterX() + radius * DriveHelper.cos(angle), u.getCenterY() + radius * DriveHelper.sin(angle));
//		
//	}
	protected void runFrom(Unit u) { setTarget(this.getCenterX() - (u.getCenterX() - this.getCenterX()), this.getCenterY() - (u.getCenterY() - this.getCenterY())); }
	
	protected void antiClump(int radius) {
		float distanceX = getNearestAlly().getCenterX() - this.getCenterX();
		float distanceY = getNearestAlly().getCenterY() - this.getCenterY();
		
		if (getNearestAlly().getDistance(this) < radius) {
			this.setTarget(this.getCenterX() - distanceX, this.getCenterY() - distanceY);
		}
	}
	
	protected void moveToTangent(HelperPoint point, float radius) {
		HelperLine movementLine = new HelperLine(getCenterX(), getCenterY(), new HelperVector(getSpeedX(), getSpeedY()));
		HelperCircle targetCircle = new HelperCircle(point.x, point.y, radius);
		
		target = DriveHelper.closestTangent(movementLine, targetCircle);
	}
	
	// Unit draw method
	public void draw(Graphics g) {
		drawForAll(g);
		drawForUnit(g);
	}
	
	// Draw for all units 
	private void drawForAll(Graphics g) {
		drawTarget(g);
		drawTravelPath(g);
//		drawDescription(g);
	}
	
	/*
	 * Helper Draw Methods
	 */
	// Draws physics variables for the unit
	private void drawDescription(Graphics g) {
		final float Padding = 15f;
		
		g.setColor(Color.orange);
		
		g.drawString("X Position: " + getCenterX(), getCenterX(), getCenterY());
		g.drawString("Y Position: " + getCenterY(), getCenterX(), getCenterY() + Padding);
		g.drawString("Current Speed: " + getCurSpeed(), getCenterX(), getCenterY() + Padding * 2);
		g.drawString("Max Speed: " + getMaxSpeed(), getCenterX(), getCenterY() + Padding * 3);
		g.drawString("Acceleration: " + getAcceleration(), getCenterX(), getCenterY() + Padding * 4);
	}
	// Draws a line from the unit to its movement target
	private void drawTarget(Graphics g) {
		final Color DrawColor = new Color(255, 255, 255, 128);
		
		g.setColor(DrawColor);
		g.drawLine(getCenterX(), getCenterY(), target.x, target.y);
	}
	// Draws a line indicating the unit's path of travel (velocity)
	private void drawTravelPath(Graphics g) {
		final Color DrawColor = new Color(160, 32, 240, 128); // Purple
		final float Expanse = xSpeed * 50f; // Length of the travel time
		
		final float Slope = ySpeed / xSpeed;
		
		g.setColor(DrawColor);
		g.drawLine(getCenterX(), getCenterY(), getCenterX() + Expanse, getCenterY() + Expanse * Slope);
	}
	
	// DriveUnit Helper Methods
	public boolean between(float x1, float y1, float x2, float y2) {
		final float distance = Utility.distance(new Point(x1, y1), new Point(x2, y2));
		if(getDistance(x1, y1) < distance && getDistance(x2, y2) < distance) return true;
		else return false;
	}
	public boolean between(Unit u1, Unit u2) {
		final float distance = Utility.distance(u1, u2);
		if(getDistance(u1) < distance && getDistance(u2) < distance) return true;
		else return false;
	}
	protected Unit getNearestEnemyMiner() {
		Unit nearestMiner = null;
		for (Unit u: this.getEnemies()) {
			if (u.hasWeapon(MiningLaser.class))
			if (nearestMiner == null || this.getDistance(nearestMiner) > this.getDistance(u)) {
				nearestMiner = u;
			}
		}
		return nearestMiner;
	}
	
	protected Unit getNearestEnemySniper() {
		Unit nearestSniper = null;
		for (Unit u: this.getEnemies()) {
			if (u.hasWeapon(Brightlance.class) || u.hasWeapon(Railgun.class) || u.hasWeapon(LongRangeMissile.class) || u.hasWeapon(AntimatterMissile.class))
			if (nearestSniper == null || this.getDistance(nearestSniper) > this.getDistance(u)) {
				nearestSniper = u;
			}
		}
		return nearestSniper;
	}
	// Find best enemy target
	protected Unit findBestEnemyTarget(ArrayList<Unit> units, DamageType damage) { // Find best target for a particular damage type
		Unit bestTarget = null;
		DefenseType targetType;
		switch (damage) {
			case ENERGY:
				targetType = DefenseType.Shield;
				break;
			case KINETIC:
				targetType = DefenseType.Plating;
				break;
			case EXPLOSIVE:
				targetType = DefenseType.Structure;
				break;
			default:
				targetType = DefenseType.Structure;
				break;
		}
		
		float overallHealthValue;
		float damageValue = 0;
		float shieldValue;
		float platingValue;
		float baseOverallHealthValue;
		float baseDamageValue = 0;
		float baseShieldValue;
		float basePlatingValue; 
		
		for (Unit u: units) {
			if (bestTarget == null) {
				bestTarget = u;
				continue;
			}
			
			overallHealthValue = (float) u.getCurEffectiveHealth() / 750f;
			if (u.getWeaponOne() != null) {
				damageValue = u.getWeaponOne().getDamage();
				if (u.getWeaponTwo() != null) {
					damageValue = u.getWeaponTwo().getDamage();
				}
				damageValue /= 750f;
			}
			
			baseOverallHealthValue = (float) bestTarget.getCurEffectiveHealth() / 750f;
			if (bestTarget.getWeaponOne() != null) {
				damageValue = bestTarget.getWeaponOne().getDamage();
				if (bestTarget.getWeaponTwo() != null) {
					damageValue = bestTarget.getWeaponTwo().getDamage();
				}
				damageValue /= 750f;
			}
				
			//if explosive, find enemy with low health and high damage
			if (targetType == DefenseType.Structure) {
				if (damageValue - overallHealthValue > baseDamageValue - baseOverallHealthValue) {
					bestTarget = u;
				}
				continue;
			}
			else if (targetType == DefenseType.Shield) {	
				//if energy or kinetic, find the enemy with high shield/plating AND low structure AND low damage
				shieldValue = (float) u.getCurShield() / 250f;
				baseShieldValue = (float) bestTarget.getCurShield() / 250f;
				
				if (shieldValue + damageValue - overallHealthValue > baseShieldValue + baseDamageValue - baseOverallHealthValue) {
					bestTarget = u;
				}
				continue;
			} else if (targetType == DefenseType.Plating) {
				platingValue = (float) u.getCurPlating() / 250f;
				basePlatingValue = (float) bestTarget.getCurPlating() / 250f;
				
				if (platingValue + damageValue - overallHealthValue > basePlatingValue + baseDamageValue - baseOverallHealthValue) {
					bestTarget = u;
				}
				continue;
			}
		}
		return bestTarget;
	}
	
}
