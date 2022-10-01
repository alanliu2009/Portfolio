package teams.student.drive.controllers.resource;

import engine.Utility;
import objects.entity.node.Node;
import objects.entity.unit.BaseShip;

public class ResourceNode implements Comparable<ResourceNode> {
	private static float DistanceScaling = 250000f;
	
	private Node resourceNode;
	private float priority;
	
	public ResourceNode(Node node) {
		this.resourceNode = node;
		
		this.priority = 0;
	}
	
	public Node getNode() { return resourceNode; }
	public float getPriority() { return priority; }
	
	public void update(BaseShip homeBase) { priority = DistanceScaling / Utility.distance(resourceNode, homeBase); }
	
	// Comparison Methods
	public int compareTo(ResourceNode node) { return (int) (100 * (node.priority - this.priority)); }
	public int compare(ResourceNode node1, ResourceNode node2)  { return (int) (100 * (node2.priority - node1.priority)); }
}
