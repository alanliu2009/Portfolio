package teams.student.drive.units.attack.fighter;

import components.upgrade.OptimizedAlgorithms;
import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.SmallLaser;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.MachineGun;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.utility.DriveHelper;

public class StrongerFella extends AttackUnit {

	public StrongerFella(DrivePlayer p, Pod pod) {
		super(p, pod);
		// TODO Auto-generated constructor stub
	}

	public Frame getFrameCounter() { return Frame.MEDIUM; }
	public Type getType() { return Type.Fighter; }
	public int mineralValue() { 
		if(production.getScalingLevel() >= 2) return 7;
		else return 6; 
	}
	public float getBasePriority() { return 1.25f; }	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM);
		setStyle(Style.ARROW);
	}

	@Override
	protected void determineWeapons() {
		switch (attack.enemyPrimaryDefense()) {
			case Shield:
				addWeapon(new LargeLaser(this));
				break;
				
			case Plating:
				addWeapon(new Autocannon(this));
				break;
			
			default:
				addWeapon(new LongRangeMissile(this));
				break;
		}
	}

	@Override
	protected void determineUpgrades() {
		switch(attack.enemyPrimaryDamage()) {
			case ENERGY:
				addUpgrade(new Plating(this)); // 1
				break;
			case KINETIC:
				addUpgrade(new Shield(this)); // 1
				break;
			case EXPLOSIVE:
				addUpgrade(new Shield(this)); // 1
				break;
			
			default:
				addUpgrade(new Shield(this)); // 1
				break;
		}
		
		// 150 - 200 Minerals, Add Algorithms
		if(production.getScalingLevel() >= 2) addUpgrade(new OptimizedAlgorithms(this));
	}



	@Override
	protected void attack() { // Play a forward position
		Weapon weapon = this.getWeaponOne();
		Unit target = attackPod.highestPriorityWithinRadius(this, weapon.getMaxRange());
		
		// Movement Behavior
		if(target == null) { // If no target within radius, move to highest priority in the pod
			this.setTarget(attackPod.highestPriority());
		} else {
			float distance = getDistance(target);
			
			// If target is inside of minimum range, move away
			if(distance < weapon.getMinRange() * 1.2f) {
				this.setTarget(getHomeBase());
			} else {
				this.setTarget(target);
			}
		}
	
		if(weapon.canUse()) { this.antiClump = false;
		} else { this.antiClump = true;	}
		
		weapon.use(target);
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