package teams.student.drive.units.support.other;

import org.newdawn.slick.Graphics;

import components.DamageType;
import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.kinetic.MassDriver;
import components.weapon.kinetic.Railgun;
import components.weapon.resource.Collector;
import components.weapon.resource.MiningLaser;
import components.weapon.utility.Pullbeam;
import components.weapon.utility.RepairBeam;
import conditions.debuffs.Stun;
import conditions.debuffs.slow.BeamSlow;
import objects.entity.Entity;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.units.support.SupportUnit;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperCircle;
import teams.student.drive.utility.shapes.HelperLine;
import teams.student.drive.utility.shapes.HelperVector;

// Pulling Support
public class Stunner extends SupportUnit {
	
	public Stunner(DrivePlayer p, Pod pod) {
		super(p, pod);
		
		this.antiClump = false;
	}
	
	public Frame getFrameCounter() { return null; }
	public int mineralValue() { return 4; }
	public float getBasePriority() { return 0.30f; }	
	
	@Override
	protected void drawForUnit(Graphics g) {}
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.LIGHT);
		setStyle(Style.WEDGE); 
	}

	@Override
	protected void determineUpgrades() {}
	
	@Override
	protected void determineWeapons() {
		this.addWeapon(new MassDriver(this));
		this.addWeapon(new MassDriver(this));
	}

	@Override
	protected void unitAI() {
		EnemyFleet fleet = attackPod.getEnemyFleet();
		
		Weapon driver1 = this.getWeaponOne();
		Weapon driver2 = this.getWeaponTwo();
		
		Unit nearestEnemy = this.getNearestEnemy();
		
		// Movement Targetting
		if(this.hasCondition(BeamSlow.class)) {
			this.setTarget(getHomeBase());
		} else if(attackPod.distanceTo(nearestEnemy.getCenterX(), nearestEnemy.getCenterY()) < attackPod.getRadius() * 1.5f) {
			this.setTarget(nearestEnemy);
		} else {
			this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());		
		}
		
		if(!nearestEnemy.hasCondition(Stun.class)) {
			if(!driver1.canUse() && !driver1.inUse()) {
				driver2.use(nearestEnemy);
			} else {
				driver1.use(nearestEnemy);
			} 
		}
	}
	
	@Override
	protected void win() {}
	
	@Override
	protected void attack() {}

	@Override
	protected void defend() {}

	@Override
	protected void retreat() {}

	@Override
	public Type getType() { return Type.Support; }

	
}
