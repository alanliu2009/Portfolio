package teams.student.drive.units.support.other;

import org.newdawn.slick.Graphics;

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
import objects.entity.Entity;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.units.support.SupportUnit;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperCircle;
import teams.student.drive.utility.shapes.HelperLine;
import teams.student.drive.utility.shapes.HelperVector;

// Pulling Support
public class PullUpNoCap extends SupportUnit {
	
	public PullUpNoCap(DrivePlayer p, Pod pod) {
		super(p, pod);
	}
	
	public Frame getFrameCounter() { return null; }
	public Type getType() { return Type.Support; }
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
		this.addWeapon(new Pullbeam(this));
	}


	@Override
	protected void win() { this.setTarget(getHomeBase()); }
	
	@Override
	protected void attack() {
		EnemyFleet fleet = attackPod.getEnemyFleet();
		Weapon pull = this.getWeaponOne();
		
		Unit target = attackPod.highestPriority();
		pull.use(target);
		
		// If unit is too far from front, don't pull
//		if(attackPod.distanceTo(target.getCenterX(), target.getCenterY()) > 1500f) this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
		if(getDistance(target) < pull.getMaxRange())  this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
		else this.setTarget(target);
	}

	@Override
	protected void defend() { this.setTarget(attackPod.getCenterX(), attackPod.getCenterY()); }

	@Override
	protected void retreat() { this.setTarget(attackPod.getCenterX(), attackPod.getCenterY()); }

	
}
