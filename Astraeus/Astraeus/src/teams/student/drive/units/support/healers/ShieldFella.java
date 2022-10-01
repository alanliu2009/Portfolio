package teams.student.drive.units.support.healers;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

//import components.upgrade.LightPlating;
//import components.upgrade.LightShield;
import components.weapon.Weapon;
import components.weapon.kinetic.MassDriver;
import components.weapon.utility.RepairBeam;
import components.weapon.utility.ShieldBattery;
//import components.weapon.special.RepairBeam;
import objects.GameObject;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.controllers.attack.AttackController.DefenseType;
import teams.student.drive.pods.Pod;
import teams.student.drive.units.support.SupportUnit;

public class ShieldFella extends SupportUnit {
	// Shield Healer
	/* Role: Heal friendly units, and stun enemy ones */
	public ShieldFella(DrivePlayer p, Pod pod) { 
		super(p, pod); 
		this.antiClump = false;
	}
	
	@Override
	protected void drawForUnit(Graphics g) {}
	
	public Frame getFrameCounter() { return null; }
	public Type getType() { return Type.Support; }
	public int mineralValue() { return 4; }
	public float getBasePriority() { return 0.20f; }	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.LIGHT); // TODO
		setStyle(Style.ORB); // TODO
	}

	@Override
	protected void determineUpgrades() {}
	
	@Override
	protected void determineWeapons() { this.addWeapon(new ShieldBattery(this)); }

	@Override
	protected void unitAI() {
		Weapon healingBeam = this.getWeaponOne();
		DriveUnit target = attackPod.mostDamagedAlly(DefenseType.Shield, this);
		
		if(target == null) this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
		else {
			healingBeam.use(target);
			if(getDistance(target) < healingBeam.getMaxRange() * 0.9f) {
				this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
			} else {
				this.setTarget(target);
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
}
