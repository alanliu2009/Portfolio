package teams.student.drive.controllers.production;

import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Purpose;

public class QueueUnit {
	protected boolean produced;
	
	protected DriveUnit unit;
	protected float priority;
	
	public QueueUnit(DriveUnit unit) {
		this.produced = false;
		
		this.unit = unit; // DriveUnit
		this.priority = unit.getBasePriority(); // Default priority
	}
	
	// Helper Methods
	public void setPriority(float f) { priority = f;}
	public void replaceUnit(DriveUnit unit) { this.unit = unit; }
	
	// Accessor Methods
	public boolean hasBeenProduced() { return produced; }
	public DriveUnit getUnit() { return unit; }
	public float getPriority() { return priority; }
}
