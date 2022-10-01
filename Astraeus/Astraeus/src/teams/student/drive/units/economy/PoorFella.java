package teams.student.drive.units.economy;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import components.upgrade.CargoBay;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.resource.Collector;
import engine.Utility;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.controllers.resource.ResourceChunk;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.*;

public class PoorFella extends DriveUnit 
{
	// Scavenger
	
	// Setting Variables
	final private static float MinimumSpeed = Resource.RESOURCE_SPEED; // Minimum speed required to throw
	final private static float ThrowingLeeway = 0.15f; // Size of the dumping circle
	final private static float CollectionCooldown = 1.5f; // 1.5 Seconds
	
	// Scavenger's Target Chunk
	private boolean collect;
	private ResourceChunk targetChunk;
	
	public PoorFella(DrivePlayer p, Pod pod) { 
		super(p, pod); 
		this.antiClump = false;
		
	}
	
	public Purpose getPurpose() { return Purpose.Economy; }
	public Type getType() { return Type.Resource; }
	public Frame getFrameCounter() { return null; }
	public float getBasePriority() {
		try {
			return player.getMacroController().getResourcePod().getScavengerPriority();
		} catch (Exception e) {
			return 0.5f;
		}
		 
	}
	
	// Return the unit's target chunk
	public ResourceChunk getTargetChunk() { return targetChunk; }
	// Set the unit's target chunk
	public void setTargetChunk(ResourceChunk chunk) { this.targetChunk = chunk; }
	// Mineral cost for the unit
	public int mineralValue() { 
		if(attack.getFrame() == Frame.LIGHT) {
			return 4;
		} else return 6;
	}
	
	protected void frameAndStyle() {
		if(attack.getFrame() == Frame.LIGHT) {
			setFrame(Frame.LIGHT); 
		} else setFrame(Frame.MEDIUM);
		
		setStyle(Style.DAGGER);
		
	}
	protected void determineWeapons() {
		addWeapon(new Collector(this)); // 3 Slots
		
		if(attack.getFrame() != Frame.LIGHT) {
			addUpgrade(new CargoBay(this));
		}
	}
	protected void determineUpgrades() {}
	
	// Unit draw method
	protected void drawForUnit(Graphics g) {
		// Throwing Circle
		g.setColor(Color.blue);
		g.draw(new Circle(getHomeBase().getCenterX(), getHomeBase().getCenterY(), Collector.MAX_RANGE * ThrowingLeeway));
	}
	
	// Unit AI
	protected void unitAI() 
	{	
		// If in range of dropped resource, halt collection
		collect = true;
		for(Resource r: resource.getDumpedResources()) {
			if(getDistance(r) < getWeaponOne().getMaxRange()) {
				collect = false;
				break;
			}
		}
		
		// Flee to Attack Pod if Nearby Enemies
		Unit nearbyEnemy = this.getNearestEnemy();
		Pod attackPod = player.getMacroController().getAttackPod();
		
		if(nearbyEnemy.getDistance(attackPod.getCenterX(), attackPod.getCenterY()) > AttackPod.AggroRadius && getDistance(nearbyEnemy) < AntimatterMissile.MAX_RANGE * 1.5f) {
			this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
			return;
		}
		
		/* Dump Behavior */
		// If no Target Chunk, or is full, or next resource is far, then dump
		if(targetChunk == null || isFull()) {	
			this.moveToDump();
			return;
		} 
		
		// Find nearest resource
		Resource nearestResource = closestResource();
		
		// One final dump check
		if(getCargo() > 0 && Utility.distance(this, nearestResource) > Collector.MAX_RANGE * 1.5f) { 
			this.moveToDump(); 
			return;
		}
		
		/* Resource Collection Behavior */
		if(nearestResource != null) {
			setTarget(nearestResource);
			
			if(getDistance(nearestResource) < Collector.MAX_RANGE / 6f && collect) { getWeaponOne().use();  }
		}
	}
	
	// --- Dumping Behavior Method ---
	private void moveToDump() {
		BaseShip homeBase = this.getHomeBase();
		
		if(production.getUnitCount(StationaryFella.class) > 0) {
			// Calculate where the BaseShip would be and create a circle around it 
			float timeTo = this.getDistance(homeBase) / MinimumSpeed; // Distance / Speed
			final HelperCircle BaseShipCircle = new HelperCircle(homeBase.getCenterX() + homeBase.getCurSpeed() * timeTo, homeBase.getCenterY(), Collector.MAX_RANGE);
			
			this.setTarget(BaseShipCircle.x, BaseShipCircle.y); // Move to HomeBase
			
			// Always dump when in gatherer range
			if(getDistance(getHomeBase()) < Collector.MAX_RANGE) { this.dumpResources(); }
			// Dump if resource will make it back to base when dumped
			else if(
					getCurSpeed() >= MinimumSpeed // Condition 1: Moving fast enough
						&& 
					movingTowardsBase() // Condition 2: Moving towards base
						&& 
					DriveHelper.lineIntersectsCircle( // Condition 3: Velocity vector intersects target circle
							new HelperLine(getCenterX(), getCenterY(), new HelperVector(xSpeed, ySpeed)),
							BaseShipCircle,
							ThrowingLeeway)
						) {
				this.dumpResources();
			}
		} else { // Edge case when Gatherer is dead
			this.setTarget(homeBase);
			this.deposit();
		}
	}
	private boolean movingTowardsBase() { // Returns if the unit is moving towards the BaseShip or not
		if(-Math.signum(xSpeed) == Math.signum(getCenterX() - getHomeBase().getCenterX()) &&
				-Math.signum(ySpeed) == Math.signum(getCenterY() - getHomeBase().getCenterY())){
			return true;
		} else return false;
	}
	private void dumpResources() { // Method to dump resources 
		// Dump resources held
		this.dump(); 		
				
		// Add nearby resources to dumped list to ignore them
		for(Resource r: ResourceManager.getResources()) {
			if(getCurSpeed() >= r.getCurSpeed() * 0.95f && Utility.distance(this, r) < Math.sqrt(w * w + h * h)) {
				player.getResourceController().addDumpedResource(r);
			}
		}
	}
	
	// Find the closest resource in the target chunk
	private Resource closestResource() {
		Resource nearestResource = targetChunk.getResources().get(0);
		
		//find if near any resource in chunk
		for(Resource resource: targetChunk.getResources()) {
			// Check if the resource is closer than the currently held "closest" resource
			if(Utility.distance(this, resource) < Utility.distance(this, nearestResource)) {
				nearestResource = resource;
			}
		}
		
		return nearestResource;
	}
}
