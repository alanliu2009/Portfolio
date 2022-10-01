package teams.student.drive.units.skirmish;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import components.DamageType;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.kinetic.MachineGun;
import components.weapon.kinetic.Railgun;
import components.weapon.utility.Aegis;
import components.weapon.utility.SpeedBoost;
import engine.Values;
import objects.entity.node.Node;
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
import teams.student.drive.pods.Command.Action;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.pods.attack.SkirmishPod;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperPoint;

public class SkirmishFella extends DriveUnit {
	// Attack Pod
	protected SkirmishPod skirmishPod;
		
	public SkirmishFella(DrivePlayer p, Pod pod) {
		super(p, pod);
		
		this.antiClump = false;
		this.skirmishPod = (SkirmishPod) pod;
	}
	
	public int mineralValue() { return 4; } 
	public Purpose getPurpose() { return Purpose.Skirmish; }
	public float getBasePriority() { return 7.5f; }

	@Override
	public Frame getFrameCounter() { return null; }

	@Override
	public Type getType() { return Type.None; }
	
	protected void frameAndStyle() {
		this.setFrame(Frame.LIGHT);
		this.setStyle(Style.ARROW);
	}
	
	protected void determineWeapons() {
		this.addWeapon(new MachineGun(this));
		this.addWeapon(new SpeedBoost(this));
	}
	protected void determineUpgrades() {}
	
	
	// Drawing 
	protected void drawForUnit(Graphics g) {
		g.setColor(Color.magenta);
		g.draw(new Circle(getCenterX(), getCenterY(), ActionRadius));
	}
	
	protected void unitAI() {
		switch(skirmishPod.getStatus()) {
			default:
				skirmish();
				break;
				
			case Attack:
				attack();
				break;
			case Skirmish:
				skirmish();
				break;
			case Defend:
				defend();
				break;
		}
	}
	
	protected void skirmish() {
		Weapon damage = this.getWeaponOne();
		Weapon boost = this.getWeaponTwo();
		
		EnemyFleet fleet = skirmishPod.getTargetFleet();
		Unit target = skirmishPod.getTargetUnit();
		
		// Boost Algorithm
		Unit nearest = getNearestAttack();
		Weapon longestWeapon = null;
		if(nearest.hasWeaponOne()) longestWeapon = nearest.getWeaponOne();
		if(nearest.hasWeaponTwo()) {
			if(longestWeapon == null || longestWeapon.getMaxRange() < nearest.getWeaponTwo().getMaxRange()) {
				longestWeapon = nearest.getWeaponTwo();
			}
		}
		if(getDistance(nearest) < longestWeapon.getMaxRange()) {
			if(nearest.hasWeaponOne()) { 
				if(nearest.getWeaponOne().getUseTimer() > nearest.getWeaponOne().getUseTime() * 0.85f) boost.use(); 
			}
			if(nearest.hasWeaponTwo()) { 
				if(nearest.getWeaponOne().getUseTimer() > nearest.getWeaponOne().getUseTime() * 0.85f) boost.use(); 
			}
		}

		// Movement Algorithm
		// Inverse 1/x movement path
		float topDown = Math.signum(target.getCenterY() - this.getCenterY());
		
		
		float distance = getDistance(target);
		if(distance < damage.getMinRange() * 1.15f) {
			this.runFrom(target);
		} else if(distance > damage.getMaxRange() * 0.85f){
			this.setTarget(target);
		}
		
		damage.use(target);
	}
	protected void attack() {
		Weapon damage = this.getWeaponOne();
		Weapon boost = this.getWeaponTwo();
		
		Unit nearest = this.getNearestEnemy();
		
		float distance = getDistance(nearest);
		if(distance < damage.getMinRange() * 1.15f) {
			this.runFrom(nearest);
		} else if(distance > damage.getMaxRange() * 0.85f){
			this.setTarget(nearest);
		}
		
		boost.use();
		damage.use(nearest);
	}
	protected void defend() {
		this.setTarget(getHomeBase());
	}
	
	
	protected Unit getNearestAttack() {
		Unit nearest = null;
		float distance = 100000f;
		
		for (Unit u: this.getEnemies()) {
			if( (u.hasWeaponOne() && damagingWeapon(u.getWeaponOne())) || (u.hasWeaponTwo() && damagingWeapon(u.getWeaponTwo())) )  {
				if(getDistance(u) < distance) nearest = u;
			}
		}
		
		return nearest;
	}
	private boolean damagingWeapon(Weapon w) {
		switch(w.getDamageType()) {
			case KINETIC:
			case EXPLOSIVE:
			case ENERGY:
				return true;
			
			default:
				return false;
		}
		
	}
}