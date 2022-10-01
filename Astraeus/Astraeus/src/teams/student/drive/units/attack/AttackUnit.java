package teams.student.drive.units.attack;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import components.DamageType;
import components.weapon.Weapon;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.utility.Aegis;
import components.weapon.utility.SpeedBoost;
import engine.Values;
import objects.entity.node.Node;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.Command.Action;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperPoint;
public abstract class AttackUnit extends DriveUnit {
	// Attack Pod
	protected AttackPod attackPod;
	protected float endX;
	protected float endY;
	
	public AttackUnit(DrivePlayer p, Pod pod) {
		super(p, pod);
		
		this.antiClumpRadius = 45;
		
		this.weapons = new ArrayList<>();
		this.attackPod = (AttackPod) pod;
	}
	
	public void setEnd(float x, float y) {
		endX = x;
		endY = y;
	}
	protected abstract void frameAndStyle();
	protected abstract void determineWeapons();
	protected abstract void determineUpgrades();
	
	public abstract int mineralValue();
	
	public Purpose getPurpose() { return Purpose.Attack; }
	
	// Drawing 
	protected void drawForUnit(Graphics g) {
		// Color Determining
		switch(attackPod.getStatus()) {
			case Attack:
				g.setColor(Color.red);
				break;
			case Defend:
				g.setColor(Color.green);
				break;
			case Retreat:
				g.setColor(Color.blue);
				break;
			
			default:
				break;
		}
		
		g.draw(new Circle(getCenterX(), getCenterY(), ActionRadius));
	}
	
	protected void unitAI() {
		this.antiClump = true;
		
		switch(attackPod.getStatus()) {
			default:
				return;
			
			case Win:
				win();
				break;
			case Attack:
				attack();
				break;
			case Defend:
				defend();
				break;
			case Retreat:
				retreat();
				break;
		}		
	}
	protected abstract void attack();
	protected abstract void defend();
	protected abstract void retreat();
	
	protected void win() {
		this.antiClump = false;
//		System.out.println("End Coordinates:endX " + " " + endY);
		this.setTarget(endX, endY);
	}
}