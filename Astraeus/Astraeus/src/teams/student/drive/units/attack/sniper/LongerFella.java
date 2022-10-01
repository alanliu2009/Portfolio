package teams.student.drive.units.attack.sniper;

import components.upgrade.OptimizedAlgorithms;
import components.upgrade.Plating;
import components.upgrade.Rangefinder;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.energy.LargeLaser;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.Megacannon;
import engine.Utility;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.utility.DriveHelper;

public class LongerFella extends AttackUnit {

	public LongerFella(DrivePlayer p, Pod pod) {
		super(p, pod);
		// TODO Auto-generated constructor stub
	}

	public Frame getFrameCounter() { return Frame.MEDIUM; }
	public Type getType() { return Type.Sniper; }
	public int mineralValue() { 
		if(production.getScalingLevel() >= 2) return 7;
		return 6; 
	}
	public float getBasePriority() { return 1.5f; }	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM);
		setStyle(Style.DAGGER);
	}

	// this.addUpgrade(new Rangefinder(this));  + 1
	@Override
	protected void determineUpgrades() {
		if(production.getScalingLevel() >= 2) addUpgrade(new Rangefinder(this));
	}
	
	@Override
	protected void determineWeapons() {
		switch (attack.enemyPrimaryDefense()) {
			case Shield:
				addWeapon(new Brightlance(this));
				break;
				
			case Plating:
				addWeapon(new Megacannon(this));
				break;
			
			default:
				addWeapon(new AntimatterMissile(this));
				break;
		}
	}

	@Override
	protected void attack() {
		Weapon weapon = this.getWeaponOne();
		Unit target = attackPod.highestPriorityWithinRadius(this, weapon.getMaxRange());
		if(target == null) { // Failsafe
			this.setTarget(attackPod.highestPriority());
			return; 
		} 
		
		// Simple Kiting Behavior
		if(weapon.canUse()) {
			this.antiClump = false;
			this.setTarget(target);
			
			float distance = getDistance(target);
			if(distance < weapon.getMaxRange() * 0.9f && distance > weapon.getMinRange() * 1.1f) {
				weapon.use(target);
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
		Weapon weapon = this.getWeaponOne();
		Unit target = attackPod.highestPriorityWithinRadius(this, weapon.getMaxRange());
		ResourcePod resource = attackPod.getResourcePod();
		
		this.setTarget(resource.getCenterX(), resource.getCenterY());
		
		if (Utility.distance(this.getPosition(), player.getMacroController().getAttackPod().getFrontPosition()) < this.getWeaponOne().getMaxRange()) {
			this.setTarget(player.getMacroController().getAttackPod().getCenterPosition());
		}
		
		if(getDistance(target) < weapon.getMaxRange()) {
			weapon.use(target);
		}
	}

	
}