package teams.student.drive.pods.economy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.newdawn.slick.geom.Point;

import engine.Utility;
import objects.entity.node.Node;
import teams.student.drive.DriveUnit;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.resource.ResourceChunk;
import teams.student.drive.controllers.resource.ResourceController;
import teams.student.drive.controllers.resource.ResourceNode;
import teams.student.drive.pods.Pod;
import teams.student.drive.units.economy.StationaryFella;
import teams.student.drive.units.economy.PoorFella;
import teams.student.drive.utility.DriveHelper;
import teams.student.drive.units.economy.MiningFella;

public class ResourcePod extends Pod {
	private ResourceController resource;
	
	// Unit Priority
	private static float scavengerPriority;
	private static float minerPriority;
	
	final private static float minerBasePriority = 0.5f;
	final private static float scavengerBasePriority = 0.5f;
	
	public float getScavengerPriority() { return scavengerPriority; }
	public float getMinerPriority() { return minerPriority; }
	
	public ResourcePod(MacroController macro) {
		super(macro);
		
		this.resource = macro.getResourceController();
		
		// Maximum number of units per group
		newUnitGroup(StationaryFella.class, 1); // 1
		newUnitGroup(PoorFella.class, 1); // 1
		newUnitGroup(MiningFella.class, 2); // 2
	}
	
	protected void calculateCenters() {
		ArrayList<DriveUnit> list = getUnitsOfClass(MiningFella.class);
		centerX = centerY = 0;
		
		for(DriveUnit miner: list) {
			centerX += miner.getCenterX();
			centerY += miner.getCenterY();
		}
		centerX /= list.size();
		centerY /= list.size();
	}
	protected void action() {
		// Update Scavenger Priority
		updateScavengerPriority();
		
		// Miner Target Assignment	
		ArrayList<ResourceNode> nodes = resource.getResourceNodes();
		if(nodes.size() > 0) {
			// Find next node that is not dead
			Node targetNode = null;
			for(ResourceNode n: nodes) {
				if(n.getNode().isAlive()) {
					targetNode = n.getNode();
					break;
				}
			}
			// Assign target node to all miners
			for(DriveUnit u: units.get(MiningFella.class)) {
				MiningFella m = (MiningFella) u;
				
				m.setTargetNode(targetNode);
			}
		}
		
		// Scavenger Target Assigment
		ArrayList<ResourceChunk> chunks = resource.getResourceChunks();
		
		for(DriveUnit u: units.get(PoorFella.class)) {
			PoorFella s = (PoorFella) u; // Get scavenger
			
			s.setTargetChunk(bestChunk(s, chunks));
		}
	}
	
	// Find best chunk for a scavenger
	private ResourceChunk bestChunk(PoorFella s, ArrayList<ResourceChunk> chunks) {
		for(ResourceChunk chunk: chunks) { chunk.setPriority(s, macroController.getPlayer().getMyBase()); }
		Collections.sort(chunks); // Sort by chunk priority
		
		for(ResourceChunk chunk: chunks) {
			if(!chunk.isTaken() && chunk.getPriority() > 0f) {
				chunk.taken();
				
				return chunk;
			}
		}
		
		return null;
	}
	
	public void updateScavengerPriority() {
		scavengerPriority = (resource.getMacro().getPlayer().getAllResources().size() / 100f) * scavengerBasePriority;
	}
	
	public void updateMinerPriority() {
		minerPriority = (resource.getMacro().getPlayer().getAllNodes().size() / 60f) * minerBasePriority;
	}
}