package teams.student.drive.units.attack.scout;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.SmallLaser;
import components.weapon.explosive.InfernoLauncher;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.MachineGun;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.utility.DriveHelper;

public class BurningFella extends AttackUnit {
	// AntiTank
	public BurningFella(DrivePlayer p, Pod pod) {
		super(p, pod);
	}

	public Frame getFrameCounter() { return null; }
	public Type getType() { return Type.Scout; }
	public int mineralValue() { return 8; }
	public float getBasePriority() { 
		if(attack.getFrame() == Frame.MEDIUM) {
			return 1f;
		} else if(attack.getFrame() == Frame.HEAVY) {
			return 1.5f;
		}
		else return 0f;
	}	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM);
		setStyle(Style.BOXY);
	}

	@Override
	protected void determineWeapons() {
		addWeapon(new InfernoLauncher(this));
		addWeapon(new InfernoLauncher(this));
	}

	@Override
	protected void determineUpgrades() {}
	
	@Override
	protected void attack() {
		Weapon weapon = this.getWeaponOne();
		Unit target = attackPod.getEnemyFleet().closestUnit(this);
		
		// Simple Kiting Behavior
		if(weapon.canUse()) {
			this.antiClump = false;
			this.setTarget(target);
			
			if(getDistance(target) < weapon.getMaxRange() * 0.85f) {
				weapon.use(target);
				getWeaponTwo().use(target);
				this.setTarget(getHomeBase());
			}
		} else {
			this.antiClump = true;
			this.setTarget(getHomeBase());
		}
	}

	@Override
	protected void defend() {
		Pod resourcePod = attackPod.getResourcePod(); // Pod to protect
		Unit nearestEnemy = getNearestEnemy(); // Find nearest enemy
		
		float radius = getWeaponOne().getMaxRange() * 0.65f;
		float protectX;
		float protectY;
		
		
		// Defend resource pod if closer to enemy base
		if(resourcePod.distanceTo(getEnemyBase().getCenterX(), getEnemyBase().getCenterY()) < getHomeBase().getDistance(getEnemyBase())) {
			protectX = resourcePod.getCenterX();
			protectY = resourcePod.getCenterY();
		} else {
			protectX = getHomeBase().getCenterX();
			protectY = getHomeBase().getCenterY();
		}
		
		float angle = DriveHelper.atan(getCenterY() - nearestEnemy.getCenterY(), getCenterX() - nearestEnemy.getCenterX());
		this.setTarget(protectX + radius * DriveHelper.cos(angle), protectY + radius * DriveHelper.sin(angle));
	}

	@Override
	protected void retreat() {
		ResourcePod resource = attackPod.getResourcePod();
		this.setTarget(resource.getCenterX(), resource.getCenterY());
	}
}