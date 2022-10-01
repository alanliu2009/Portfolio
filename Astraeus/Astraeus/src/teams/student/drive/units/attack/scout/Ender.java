package teams.student.drive.units.attack.scout;

import components.DamageType;
import components.upgrade.Plating;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.energy.LargeLaser;
import components.weapon.energy.SmallLaser;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.explosive.LongRangeMissile;
import components.weapon.explosive.ShadowflightMissile;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.attack.AttackUnit;
import teams.student.drive.utility.DriveHelper;

public class Ender extends AttackUnit {

	public Ender(DrivePlayer p, Pod pod) {
		super(p, pod);
	}

	public Frame getFrameCounter() { return null; }
	public Type getType() { return Type.None; }
	public int mineralValue() { return 6; }
	public float getBasePriority() { 
		if(attackPod.getEnemyFleet() == null) return 10f;
		else return 0f;
	}	
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM);
		setStyle(Style.WEDGE);
	}

	@Override
	protected void determineWeapons() {
		addWeapon(new AntimatterMissile(this));
	}

	@Override
	protected void determineUpgrades() {}

	@Override
	protected void unitAI() {
		BaseShip enemyBase = getEnemyBase();
		Weapon missile = this.getWeaponOne();
		
		if(getDistance(enemyBase) < Brightlance.MAX_RANGE) {
			this.runFrom(enemyBase);
		} else if(getDistance(enemyBase) > missile.getMaxRange()) {
			this.setTarget(enemyBase);
		}
		
		missile.use(enemyBase);
	}

	@Override
	protected void attack() {}

	@Override
	protected void defend() {}

	@Override
	protected void retreat() {}
	
}