package teams.student.drive.units.support;

import org.newdawn.slick.Graphics;

import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;

public abstract class SupportUnit extends DriveUnit {
	protected AttackPod attackPod;
	
	public SupportUnit(DrivePlayer p, Pod pod) {
		super(p, pod);
		this.attackPod = (AttackPod) pod;
	}	
	
	protected abstract void frameAndStyle();
	protected abstract void determineWeapons();
	protected abstract void determineUpgrades();
	
	public abstract int mineralValue();
	
	public Purpose getPurpose() { return Purpose.None; }
	
	protected void unitAI() {
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
	protected abstract void win(); 
	protected abstract void attack();
	protected abstract void defend();
	protected abstract void retreat();
}