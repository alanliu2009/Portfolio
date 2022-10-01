package teams.student.drive.controllers.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import components.weapon.WeaponType;
import components.weapon.resource.Collector;
import engine.Utility;
import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import teams.student.drive.DrivePlayer;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.units.economy.PoorFella;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.utility.shapes.HelperCircle;
import teams.student.drive.utility.shapes.HelperLine;
import teams.student.drive.utility.shapes.HelperVector;
import ui.display.message.PlayerMessage;

//Determines Resource Management
public class ResourceController {
	private DrivePlayer player; // Player
	
	// Setting Variables
	private static final float ChunkingRadius = Collector.MAX_RANGE;
	
	// List of Resources Dumped
	private ArrayList<Resource> dumped;
	
	// Instance Variables
	private ArrayList<ResourceChunk> chunks; // List of chunks
	private ArrayList<ResourceNode> nodes;
	
	// Constructor
	public ResourceController(MacroController macroController) {		
		this.player = macroController.getPlayer();
		
		// Node Data
		this.nodes = new ArrayList<>();
		
		// Chunk / Scavenging
		this.dumped = new ArrayList<>();
		this.chunks = new ArrayList<>();	
	}
	
	public ArrayList<Resource> getDumpedResources() { return dumped; }
	
	public MacroController getMacro() { return player.getMacroController(); }
	// Drawing Descriptions for Chunks
	public void draw(Graphics g) {
		g.setColor(Color.green);
		for(ResourceChunk chunk: chunks) {
			g.draw(new Circle(chunk.getCenterX(), chunk.getCenterY(), chunk.getRadius()));
			
			g.drawString("Value: " + chunk.getValue(), chunk.getCenterX(), chunk.getCenterY());
			g.drawString("Priority: " + chunk .getPriority(), chunk.getCenterX(), chunk.getCenterY() - 25f);
		}
		
		player.addMessage(new PlayerMessage("Scavenger Modifier " + player.getAllResources().size() / 100f, Color.green));
		player.addMessage(new PlayerMessage("Miner Modifier " + player.getAllNodes().size() / 60f, Color.green));
	}
	
	// Accessor Methods
	public ArrayList<ResourceChunk> getResourceChunks() { return chunks; }
	public ArrayList<ResourceNode> getResourceNodes() { return nodes; }
	
	public BaseShip getHomeBase() { return player.getMyBase(); }
	
	// Helper Methods
	public void addDumpedResource(Resource r) { dumped.add(r); }
	
	// Set-Up: Set up ResourceNodes
	public void setUp() { for(Node n: NodeManager.getNodes()) { nodes.add(new ResourceNode(n)); } }
	
	private boolean movingTowardsBase(Resource r) { // Returns if the unit is moving towards the BaseShip or not
		if(-Math.signum(r.getSpeedX()) == Math.signum(r.getCenterX() - getHomeBase().getCenterX()) &&
				-Math.signum(r.getSpeedY()) == Math.signum(r.getCenterY() - getHomeBase().getCenterY())){
			return true;
		} else return false;
	}
	
	// Update Method
	public void update() {	
		// Remove dumped resources
		BaseShip baseShip = getHomeBase();
		for(int i = dumped.size() - 1; i >= 0; i--) {
			Resource r = dumped.get(i);
			
			float timeTo = r.getDistance(baseShip) / r.getCurSpeed();
			HelperCircle BaseShipCircle = new HelperCircle(baseShip.getCenterX() + timeTo * baseShip.getCurSpeed(), baseShip.getCenterY(), Collector.MAX_RANGE);
			if(!movingTowardsBase(r) // Moving towards base
						||
					!DriveHelper.lineIntersectsCircle( // Velocity vector intersects target circle
							new HelperLine(r.getCenterX(), r.getCenterY(), new HelperVector(r.getSpeedX(), r.getSpeedY())),
							BaseShipCircle,
							1f)
					||
					r.wasPickedUp() // Was picked up
					) {
				dumped.remove(i);
			}
		}
		
		// Update priorities of ResourceNodes
		for(ResourceNode n: nodes) { n.update(getHomeBase()); }
		Collections.sort(nodes); // Sort by node priority
		
		// Chunk Resources
		this.chunk();
	}
	
	// Chunking Algorithm
	private void chunk() {
		chunks.clear(); // Clear chunks
		
		ArrayList<Resource> resources = ResourceManager.getResources(); // Get all resources
		
		// Initialize grouped boolean array
		boolean[] ignore = new boolean[resources.size()];
		for(int i = 0; i < resources.size(); i++) { // Determine what resources to immediately ignore
			Resource r = resources.get(i);
			ignore[i] = r.wasPickedUp() || Utility.distance(r, this.getHomeBase()) <= Collector.MAX_RANGE || dumped.contains(r) || nearbyEnemies(r, 500f);
		}
		
		// Chunk Resources
		for(int i = 0; i < resources.size(); i++) {
			if(ignore[i]) continue;
			
			ArrayList<Resource> group = new ArrayList<>();
			groupResourcesAround(resources.get(i), group, ignore);
			
			chunks.add(new ResourceChunk(this, group));			
		}		
	}
	
	private boolean nearbyEnemies(Resource r, float radius) {
		for(Unit u: player.getEnemyUnits()) {
			if(u.hasWeapon(WeaponType.ENERGY) || u.hasWeapon(WeaponType.KINETIC) || u.hasWeapon(WeaponType.EXPLOSIVE)) {
				if(r.getDistance(u) < radius) return true;
			}
		}
		return false;
	}
	// Group all resources within a radius of a given resource
	private void groupResourcesAround(Resource r, ArrayList<Resource> resources, boolean[] ignore) {
		resources.add(r); // Add to resource ArrayList
		
		for(int i = 0; i < ResourceManager.getResources().size(); i++) {
			if(ignore[i]) continue;
			
			Resource r2 = ResourceManager.getResources().get(i);
			if(Utility.distance(r, r2) <= ChunkingRadius) {
				ignore[i] = true;
				groupResourcesAround(r2, resources, ignore);
			}
		}
	}
	
}