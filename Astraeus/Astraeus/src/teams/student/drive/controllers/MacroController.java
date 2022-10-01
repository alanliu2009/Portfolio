package teams.student.drive.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;

import teams.student.drive.units.economy.StationaryFella;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.controllers.attack.AttackController;
import teams.student.drive.controllers.production.ProductionController;
import teams.student.drive.controllers.resource.ResourceController;
import teams.student.drive.pods.Pod;
import teams.student.drive.pods.attack.AttackPod;
import teams.student.drive.pods.attack.SkirmishPod;
import teams.student.drive.pods.economy.ResourcePod;
import teams.student.drive.units.economy.MiningFella;
import teams.student.drive.units.economy.PoorFella;

public class MacroController {
	private DrivePlayer player;
	
	// Sub-Controllers
	private AttackController attack;
	private ProductionController production;
	private ResourceController resource;
	
	// All Pods
	private ArrayList<Pod> pods;
	
	private AttackPod attackPod;
	private ResourcePod resourcePod;
	private SkirmishPod skirmishPod;
	
	/* Controls the overall strategy for the team */
		
	public MacroController(DrivePlayer player) {
		this.player = player; // Saving Player
		
		// Creating Pods List
		this.pods = new ArrayList<Pod>(); 
		
		// Activation Time
		this.activationTime = Sys.getTime();
		
		// Creating Sub-Controllers
		this.attack = new AttackController(this);
		this.production = new ProductionController(this);
		this.resource = new ResourceController(this);
	}
	
	// Draw method macrocontroller
	public void draw(Graphics g) {
		// Drawing for attack
		attack.draw(g);
		
		// Drawing for production
		production.draw(g);
		
		// Drawing for resource
		resource.draw(g);
		
		// Drawing for pods
		for(Pod pod: pods) { pod.draw(g); }
	}
	
	// Accessor Methods
	public AttackPod getAttackPod() { return attackPod; }
	public ResourcePod getResourcePod() { return resourcePod; }
	public SkirmishPod getSkirmishPod() { return skirmishPod; }
	
	public DrivePlayer getPlayer() { return player; }
	

	public ProductionController getProductionController() { return production; }
	public ResourceController getResourceController() { return resource; }
	public AttackController getAttackController() { return attack; }
	
	// Helper Methods
	public void requestUnit(DriveUnit unit) { production.addToQueue(unit); }
	public void newPod(Pod pod) { pods.add(pod); }
	
	// Determine initial strategy for the team
	public void initialStrategy() {
		// Initialize Pods
		this.resourcePod = new ResourcePod(this);
		this.attackPod = new AttackPod(this);
		this.skirmishPod = new SkirmishPod(this);
		
		// Add Pods
		pods.add(resourcePod);
		pods.add(attackPod);
		pods.add(skirmishPod);
		
		// Chunk initial resources
		resource.setUp();
	}
	
	public static final float DelayTime = 0.1f;
	private float activationTime;
	// Update the controller
	public void update() {
		// Update EnemyFleet Data
		attack.update();
		
		// Update Resource Data
		resource.update();
		
		// Update Pods
		for(Pod pod: pods) { pod.update(); }
				
		// Production Update Check
		if(Sys.getTime() - activationTime > DelayTime * 1000f) {
			production.setSkirmish(false);
			production.update();
		}
	}
		
}