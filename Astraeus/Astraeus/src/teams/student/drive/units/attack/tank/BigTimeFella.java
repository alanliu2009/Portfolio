package teams.student.drive.units.attack.tank;

import components.upgrade.NanobotHull;
import components.upgrade.OptimizedAlgorithms;
import components.upgrade.Plating;
import components.upgrade.Rangefinder;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.energy.SmallLaser;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.explosive.ShadowflightMissile;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.MachineGun;
import components.weapon.resource.Collector;
import components.weapon.utility.Aegis;
import components.weapon.utility.AntiMissileSystem;
import engine.states.Game;
import objects.entity.unit.BaseShip;
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

public class BigTimeFella extends AttackUnit {

	public BigTimeFella(DrivePlayer p, Pod pod) {
		super(p, pod);
		
		this.antiClump = false;
	}
	
	public Frame getFrameCounter() { return null; }
	public Type getType() { return Type.None; }
	public int mineralValue() { 
		return 10;
	}
	public float getBasePriority() { 
		if(attackPod.getEnemyFleet() == null && Game.getTime() > 500) return 10f;
		else return 0f;
	}	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.HEAVY);
		setStyle(Style.ROCKET);
	}

	@Override
	protected void determineUpgrades() {}
	
	@Override
	protected void determineWeapons() {
		addWeapon(new AntimatterMissile(this));
		addWeapon(new AntimatterMissile(this));
	}

	

	@Override
	protected void unitAI() {
		BaseShip enemyBase = getEnemyBase();
		Weapon missile = this.getWeaponOne();
		Weapon missileTwo = this.getWeaponTwo();
		
		if(getDistance(enemyBase) < 900f) {
			this.runFrom(enemyBase);
		} else if(getDistance(enemyBase) > missile.getMaxRange()) {
			this.setTarget(enemyBase);
		}
		
		missile.use(enemyBase);
		missileTwo.use(enemyBase);
	}
	
	@Override
	protected void attack() {}

	@Override
	protected void defend() {}

	@Override
	protected void retreat() {}


	
}