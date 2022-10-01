package teams.student.drive.units.attack.tank;

import components.upgrade.NanobotHull;
import components.upgrade.OptimizedAlgorithms;
import components.upgrade.Plating;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.energy.SmallLaser;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.Aegis;
import components.weapon.utility.AntiMissileSystem;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.utility.DriveHelper;

public class BiggestFella extends AttackUnit {

	public BiggestFella(DrivePlayer p, Pod pod) {
		super(p, pod);
		
		this.antiClump = false;
	}
	
	public Frame getFrameCounter() { return Frame.HEAVY; }
	public Type getType() { return Type.Tank; }
	public int mineralValue() { 
		if(production.getScalingLevel() == 2) return 9;
		else if(production.getScalingLevel() >= 3) return 10;
		return 8;
	}
	public float getBasePriority() { return 1f; }	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.HEAVY);
		setStyle(Style.BUBBLE);
	}

	@Override
	protected void determineUpgrades() {
		switch(attack.enemyPrimaryDamage()) {
			case ENERGY:
				addUpgrade(new Plating(this)); // 1
				addUpgrade(new Plating(this)); // 1
				break;
			case KINETIC:
				addUpgrade(new Shield(this)); // 1
				addUpgrade(new Shield(this)); // 1
				break;
			case EXPLOSIVE:
				addUpgrade(new Shield(this)); // 1
				break;
			
			default:
				addUpgrade(new Shield(this)); // 1
				addUpgrade(new Shield(this)); // 1
				break;
				
		}
		
		if(production.getScalingLevel() >= 2) addUpgrade(new NanobotHull(this));
		if(production.getScalingLevel() >= 3) {
			addUpgrade(new OptimizedAlgorithms(this));
			addUpgrade(new OptimizedAlgorithms(this));
		}
	}
	
	@Override
	protected void determineWeapons() {
		addWeapon(new Aegis(this)); // 1
		
		switch(attack.enemyPrimaryDamage()) {
			case EXPLOSIVE:
				addWeapon(new AntiMissileSystem(this));
				break;
			default:
				switch (attack.enemyPrimaryDefense()) {
					case Shield:
						addWeapon(new SmallLaser(this));
						break;
						
					case Plating:
						addWeapon(new MachineGun(this));
						break;
					
					default:
						addWeapon(new ShortRangeMissile(this));
						break;
				}
				break;
		}
	}


	@Override
	protected void attack() {
		EnemyFleet fleet = attackPod.getEnemyFleet();
		Unit target = fleet.closestUnit(this);
		
		Weapon aegis = this.getWeaponOne();
		Weapon weapon = this.getWeaponTwo();
		
		if(countEnemiesInRadius(150) > 0) {
			aegis.use(this);
		}
		
		this.setTarget(fleet.getCenterX(), fleet.getCenterY());
		weapon.use(target);
	}

	@Override
	protected void defend() {
		Pod resourcePod = attackPod.getResourcePod(); // Pod to protect
		Unit nearestEnemy = getNearestEnemy(); // Find nearest enemy
		
		float radius = getWeaponTwo().getMaxRange() * 0.65f;
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