package teams.student.drive.units.support.other;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import components.weapon.utility.AntiMissileSystem;
import engine.states.Game;
import objects.entity.missile.Missile;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.units.support.SupportUnit;

// Pulling Support
public class AntiMissile extends SupportUnit {
	
	public AntiMissile(DrivePlayer p, Pod pod) {
		super(p, pod);
	}
	
	public Frame getFrameCounter() { return null; }
	public Type getType() { return Type.Support; }
	public int mineralValue() { return 7; }
	public float getBasePriority() { 
		// Will only be produced if enemy has a lot of explosive weapons
		if(attack.enemyExplosiveValue() > 0.45f) {
			return 0.25f; 
		} else return 0f;
	}	
	
	@Override
	protected void drawForUnit(Graphics g) {}
	
	@Override
	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM); 
		setStyle(Style.ROCKET); 
	}

	@Override // No Upgrades
	protected void determineUpgrades() {}
	
	@Override
	protected void determineWeapons() {
		this.addWeapon(new AntiMissileSystem(this));
		this.addWeapon(new AntiMissileSystem(this));
	}


	@Override // On win, AFK
	protected void win() { this.setTarget(getHomeBase()); }
	
	@Override
	protected void unitAI() {
		Weapon antiMissile = this.getWeaponOne();
		Weapon antiMissile2 = this.getWeaponTwo();
		Missile missileTarget = this.closestEnemyMissile();
		
		// If no missile, hover around AttackPod center
		if(missileTarget == null) this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
		// If missile is present
		else {
			antiMissile.use(missileTarget);
			antiMissile2.use(missileTarget);
			
			// If missile within antimissile range, or too far away from attack pod, move to attackPod center
			if(getDistance(missileTarget) < antiMissile.getMaxRange() * 0.90f) {
				this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
			} else if(attackPod.distanceTo(missileTarget.getCenterX(), missileTarget.getCenterY()) > attackPod.getRadius() + AttackPod.AggroRadius) {
				this.setTarget(attackPod.getCenterX(), attackPod.getCenterY());
			} 
			// Else, move to missileTarget
			else {
				this.setTarget(missileTarget);
			}
			
		}
	}
	
	private Missile closestEnemyMissile() {
		ArrayList<Missile> missiles = player.getEnemyMissiles();
		float nearestMissileDistance = Float.MAX_VALUE;
		Missile nearestMissile = null;
		
		for(Missile m : missiles)
		{
			if(getDistance(m) < nearestMissileDistance)
			{
				nearestMissileDistance = getDistance(m);
				nearestMissile = m;
			}
		}
		
		return nearestMissile;
	}
	@Override
	protected void attack() {}

	@Override
	protected void defend() {}

	@Override
	protected void retreat() {}

	
}
