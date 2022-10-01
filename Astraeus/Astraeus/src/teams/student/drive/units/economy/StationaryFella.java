package teams.student.drive.units.economy;

import engine.Utility;

import org.newdawn.slick.Graphics;

import components.weapon.resource.Collector;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.resource.Resource;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;

public class StationaryFella extends DriveUnit {
	// Gatherer
	public StationaryFella(DrivePlayer p, Pod pod) { 
		super(p, pod); 
		
		this.antiClump = false;
	}
	
	public float getBasePriority() { return 0f; }
	public Purpose getPurpose() { return Purpose.Economy; }
	public Type getType() { return Type.Resource; }
	public Frame getFrameCounter() { return null; }
	
	protected void drawForUnit(Graphics g) { }

	// Mineral cost for unit
	public int mineralValue() { return 6; }

	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM); // Frame Setting
		setStyle(Style.BUBBLE); // Style - Cosmetic
	}
	protected void determineWeapons() {
		addWeapon(new Collector(this)); // 3 Capacity
	}
	protected void determineUpgrades() {
		
	}
	
	// Sit at base and use the collector. What a lazy bum.
	protected void unitAI() {
		// If no scavengers
		if(pod.getUnitsOfClass(PoorFella.class).size() == 0) {
			Resource nearest = this.getNearestResource();
			if(this.isFull() || nearest == null) {
				setTarget(getHomeBase());
				deposit();
			} else {
				setTarget(nearest);
				if(getDistance(nearest) < Collector.MAX_RANGE * 0.5f) {
					this.getWeaponOne().use();
				}
			}
		} 
		// Default AI
		else {
			this.setTarget(getHomeBase()); // Set target at the BaseShip
			
			if(Utility.distance(this, getHomeBase()) < 0.25f) { // If at the center of the BaseShip,
				this.getWeaponOne().use(); // Use weapon
				this.deposit(); // Deposit any minerals
			}
		}
		
	}
}