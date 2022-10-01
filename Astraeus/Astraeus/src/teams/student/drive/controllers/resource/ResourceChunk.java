package teams.student.drive.controllers.resource;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.geom.Point;

import engine.Utility;
import objects.GameObject;
import objects.entity.unit.BaseShip;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import objects.resource.Scrap;
import teams.student.drive.controllers.attack.EnemyFleet;
import teams.student.drive.controllers.production.QueueUnit;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.units.economy.PoorFella;

public class ResourceChunk implements Comparable<ResourceChunk> {
	final static public float DangerRadius = 2500f;
	final static public float DistanceScaling = 250000f;
	final static public float ValueScaling = 10f;
	
	protected ResourceController resourceController;

	// Instance Variables
	protected ArrayList<Resource> resources; // List of all resources in the chunk

	protected boolean taken;
	
	protected float centerX, centerY; // Center x, y of chunk
	protected float radius;
	
	protected float value; // Mineral Value of the ResourceChunk
	protected float priority;	
	
	protected boolean far;
	
	// Constructor
	public ResourceChunk(ResourceController resourceController, ArrayList<Resource> resources) { 
		this.resourceController = resourceController;
		
		this.resources = resources;
		
		setUp(resourceController);
	}
	
	// Update Method for a Chunk to be Inherited
	private void setUp(ResourceController resourceController) {		
		// Recalculate value of chunk
		value = 0f;
		for(Resource r: resources) {
			if(r instanceof Scrap) value += 0.5f;
			else value += 1f;
		}
		
		// Update x and y centers of the chunk
		centerX = centerY = 0;
		
		for (Resource r: resources) {
			centerX += r.getCenterX();
			centerY += r.getCenterY();
		}
		
		centerX /= (float) resources.size();
		centerY /= (float) resources.size();
		
		// Find radius of the chunk
		Resource furthestResource = null;
		for(Resource r: resources) {
			if(furthestResource == null || Utility.distance(new Point(centerX, centerY), new Point(r.getCenterX(), r.getCenterY())) > radius) {
				furthestResource = r;
				radius = Utility.distance(new Point(centerX, centerY), new Point(r.getCenterX(), r.getCenterY()));
			}
		}
		this.radius += 25f; // Add some base radius
		
		// Find nearby enemies
		if(getDistance(resourceController.getHomeBase()) > 6000f && getDistance(resourceController.getMacro().getAttackPod()) > 3000f) {
			far = true;
		}
	};
	
	
	// Accessor Methods
	public ArrayList<Resource> getResources() { return resources; }
	
	public boolean isTaken() { return taken; }
	public float getCenterX() { return centerX; }
	public float getCenterY() { return centerY; }
	public float getRadius() { return radius; }
	
	public float getValue() { return value; }
	
	public float getPriority() { return priority; }

	
	// Comparison Methods
	public int compareTo(ResourceChunk chunk) { return (int) (100 * (chunk.priority - this.priority)); }
	public int compare(ResourceChunk chunk1, ResourceChunk chunk2)  { return (int) (100 * (chunk2.priority - chunk1.priority)); }

		
	// Helper Methods
	public void taken() { taken = true; }
	public void setPriority(PoorFella s, BaseShip homeBase) { 
		if(far) priority = 0;
		else priority = (DistanceScaling / getDistance(s) + value * ValueScaling); 
	}
	public float getDistance(GameObject o) { return Utility.distance(new Point(centerX, centerY), new Point(o.getCenterX(), o.getCenterY())); }
	public float getDistance(Pod pod) { return Utility.distance(new Point(centerX, centerY), new Point(pod.getCenterX(), pod.getCenterY())); }
	public float getDistance(ResourceChunk chunk) { return Utility.distance(new Point(centerX, centerY), new Point(chunk.centerX, chunk.centerY)); }
	
	public void setPriority(float priority) { this.priority = priority; }
}
