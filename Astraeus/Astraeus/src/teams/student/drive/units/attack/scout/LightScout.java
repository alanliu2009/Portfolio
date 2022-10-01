package teams.student.drive.units.attack.scout;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.SmallLaser;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.explosive.ShadowflightMissile;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.utility.DriveHelper;

public class LightScout extends AttackUnit {

	public LightScout(DrivePlayer p, Pod pod) {
		super(p, pod);
	}

	public Frame getFrameCounter() { return Frame.LIGHT; }
	public Type getType() { return Type.Scout; }
	public int mineralValue() { return 4; }
	public float getBasePriority() { return 1.25f; }	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.LIGHT);
		setStyle(Style.WEDGE);
	}

	@Override
	protected void determineWeapons() {
		addWeapon(new ShadowflightMissile(this));
		addWeapon(new SpeedBoost(this));
	}

	@Override
	protected void determineUpgrades() {
		
	}



	@Override
	protected void attack() {
		Weapon missileOne = this.getWeaponOne();
		Weapon missileTwo = this.getWeaponTwo();
		
		Unit target = this.getNearestEnemy();
		if(target == null) return;
		
		// Simple Kiting Behavior
		if(missileOne.canUse()) {
			this.antiClump = false;
			this.setTarget(target);
			
			if(getDistance(target) < missileOne.getMaxRange() * 0.925f) {
				missileOne.use(target);
				missileTwo.use(target);
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