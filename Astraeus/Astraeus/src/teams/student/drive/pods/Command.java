package teams.student.drive.pods;

import objects.entity.unit.Unit;
import teams.student.drive.DriveUnit;
import teams.student.drive.controllers.attack.EnemyFleet;

public class Command {
	public enum Action { Lead, Retreat, Attack, Defend, Skirmish, None }
	public enum Target { None, Ally, Enemy, Pod, Fleet, Point }
	
	public boolean leader;
	
	public Action action;
	public Target target;
	
	public DriveUnit allyTarget;
	public Pod podTarget;
	
	public Unit enemyTarget;
	public EnemyFleet fleetTarget;
	
	public Command(Action action) {
		this.action = action;
		this.target = Target.None;
		this.leader = false;
	}
	
	public boolean isLeader() { return leader; }
	
	public void leader() { leader = true; }
	public void setAction(Action a) { this.action = a; }
	public void setAllyTarget(DriveUnit ally) {
		allyTarget = ally;
		target = Target.Ally;
	}
	public void setEnemyTarget(Unit enemy) {
		enemyTarget = enemy;
		target = Target.Enemy;
	}
	
	public void setPodTarget(Pod pod) {
		podTarget = pod;
		target = Target.Pod;
	}
	public void setFleetTarget(EnemyFleet fleet) {
		fleetTarget = fleet;
		target = Target.Fleet;
	}
}