package teams.student.drive.units.economy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import components.weapon.resource.MiningLaser;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.DriveUnit.Type;
import teams.student.drive.pods.Pod;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperCircle;
import teams.student.drive.utility.shapes.HelperLine;
import teams.student.drive.utility.shapes.HelperPoint;
import teams.student.drive.utility.shapes.HelperVector;

public class MiningFella extends DriveUnit {
	// Miner
	
	// Target Node
	private Node targetNode;
	private boolean circling;
	private double angle;
	
//	// level of miner (temp)
//	private boolean level = true;
	
	public MiningFella(DrivePlayer p, Pod pod) { 
		super(p, pod); 
		antiClump = false;
//		if (p.getTeam() == 1) {
//			level = false;
//		}
	}
	
	public Purpose getPurpose() { return Purpose.Economy; }
	public Type getType() { return Type.Resource; }
	public Frame getFrameCounter() { return null; }
	public float getBasePriority() {
		try {
			return player.getMacroController().getResourcePod().getMinerPriority();
		} catch (Exception e) {
			return 0.5f;
		}
	}
	
	protected void drawForUnit(Graphics g) {}
	
	// Get the target node
	public Node getTargetNode() { return targetNode; }
	// Change the taret node
	public void setTargetNode(Node node) {
		if(targetNode == null || node == null || !node.equals(targetNode)) circling = false;
		this.targetNode = node; 
	}
	// Mineral cost for the unit
	public int mineralValue() {
		return 7;
//		return 4;
	}
	
	protected void frameAndStyle() {
		setFrame(Frame.MEDIUM); 
//		setFrame(Frame.LIGHT); // 2
		setStyle(Style.BUBBLE);
	}
	protected void determineWeapons() {
		addWeapon(new MiningLaser(this));
		addWeapon(new MiningLaser(this)); // 2
	}
	protected void determineUpgrades() {
		
	}
	
	// Miner Behavior
	protected void unitAI() 
	{		
		// Failsafe if targetNode is null
		if(targetNode == null) {
			this.setTarget(getHomeBase());
			return;
		}
		
		// Current Circle Radius
		final float circleRadius = getCurSpeed() * getCurSpeed() / getAcceleration();
		
		/* Mining Behavior */
//		// Default: Move to closest tangent around the node
//		target = DriveHelper.closestTangent(
//				new HelperLine(this.getCenterX(), this.getCenterY(), new HelperVector(xSpeed, ySpeed)),
//				new HelperCircle(targetNode.getCenterX(), targetNode.getCenterY(), circleRadius)
//				);
//		
//		this.angle = Math.atan2((targetNode.getCenterY() - getCenterY()) , (targetNode.getCenterX() - getCenterX()));
//		// Circle when close enough to the tangent point
//		if(getDistance(new Point(target.x, target.y)) < 50f) { circling = true; }
//		
//		// If circling, use laser and use centripetal acceleration to circle the node
//		if(circling) {
//			getWeaponOne().use(targetNode);
//			
//			this.setTarget(targetNode);
//			this.moveTo(targetNode.getCenterX(), targetNode.getCenterY());
//			
//			return;
//		}
		
		
		if(getDistance(targetNode) > getWeaponOne().getMaxRange() * 1.5f) {
			target = DriveHelper.closestTangent(
					new HelperLine(this.getCenterX(), this.getCenterY(), new HelperVector(xSpeed, ySpeed)),
					new HelperCircle(targetNode.getCenterX(), targetNode.getCenterY(), circleRadius)
					);
			
			this.angle = Math.atan2((targetNode.getCenterY() - getCenterY()) , (targetNode.getCenterX() - getCenterX()));
		} else {
			target = circleTarget(targetNode, getWeaponOne().getMaxRange() * 0.8f);
		}
		
		getWeaponOne().use(targetNode);
		if (hasWeaponTwo()) {
			getWeaponTwo().use(targetNode);
		}
		
	}
	
	public HelperPoint circleTarget(Node n, float r) {
		//moveTo --> x and y update angle
		
		angle += 0.009;
		
		float targetX = (float) (n.getCenterX() + r * Math.cos(angle));
		float targetY = (float) (n.getCenterY() + r * Math.sin(angle));
		
		moveTo(targetX, targetY);
		
		
		return new HelperPoint(targetX, targetY);
	}
	
	
}
