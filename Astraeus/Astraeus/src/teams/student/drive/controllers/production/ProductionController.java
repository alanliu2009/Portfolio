package teams.student.drive.controllers.production;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import teams.student.drive.DrivePlayer;
import teams.student.drive.DriveUnit;
import teams.student.drive.DriveUnit.Counter;
import teams.student.drive.DriveUnit.Purpose;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.attack.AttackController;
import teams.student.drive.pods.Pod;
import teams.student.drive.units.economy.StationaryFella;
import teams.student.drive.units.skirmish.SkirmishFella;
import teams.student.drive.units.attack.tank.BigTimeFella;
import teams.student.drive.units.economy.MiningFella;
import teams.student.drive.units.economy.PoorFella;
import ui.display.message.PlayerMessage;

// Determines Pod / Unit Production
public class ProductionController {
//	public static final 
	
	/*
	 * Step 1: Get total unit count
	 * Step 2: Calculate every queue units priority
	 * Step 3: Keep producing - for every queueunit produced, immediately replace with a new instance
	 * Every pod will have ONE queue unit for every class.
	 * 
	 */	
	private DrivePlayer player;
	private AttackController attack;
	
	// Unit counts that must always be met
	private HashMap<Class<? extends DriveUnit>, Integer> requiredUnits;
	
	// Unit count of every class
	private HashMap<Class<? extends DriveUnit>, Integer> unitCount;
	
	// Skirmish production switch
	private boolean skirmish;
	
	// Queue
	private ArrayList<QueueUnit> queue;
	private int scalingLevel;
	
	public ProductionController(MacroController macro) {
		this.player = macro.getPlayer();
		this.attack = macro.getAttackController();
		
		this.queue = new ArrayList<QueueUnit>();
		
		this.unitCount = new HashMap<Class<? extends DriveUnit>, Integer>();
		
		this.requiredUnits = new HashMap<Class<? extends DriveUnit>, Integer>();
		requiredUnits.put(StationaryFella.class, 1);
		requiredUnits.put(PoorFella.class, 2);
		requiredUnits.put(MiningFella.class, 3);
//		requiredUnits.put(BigTimeFella.class, 1);
	}
	
	// Get Unit Count
	public int getUnitCount(Class<? extends DriveUnit> clazz) { 
		if(unitCount.containsKey(clazz)) return unitCount.get(clazz); 
		else return 0;
	}
	public int getScalingLevel() { return scalingLevel; } // Get scaling level
	public void addToQueue(DriveUnit unit) { queue.add(new QueueUnit(unit)); }	// Add a unit to the production queue
	
	public void setSkirmish(boolean b) { skirmish = b; }
	
	// Update the production controller
	public void update() {
		scalingLevel = ( (int) player.getMineralsMined() ) / 50 - 1;
		
		// Update Total Unit Count
		updateUnitCount();
		
		// Produce Units
		produce();
	}
	
	// Update Total Unit Count
	private void updateUnitCount() {
		unitCount.clear();
		
		for(Unit unit: player.getMyUnits()) {
			@SuppressWarnings("unchecked")
			Class<? extends DriveUnit> clazz = (Class<? extends DriveUnit>) unit.getClass();
			
			if(unitCount.containsKey(clazz)) {
				unitCount.replace(clazz, unitCount.get(clazz) + 1);
			} else {
				unitCount.put(clazz, 1);
			}
		}
	}
	// Update QueueUnit Priorities
	private final static float PurposeScaling = 2.5f;
	private void updatePriorities() {
		// Check for any units that MUST be produced
		Class<? extends DriveUnit> priorityClass = null;
		for(Class<? extends DriveUnit> clazz: requiredUnits.keySet()) {
			if(!unitCount.containsKey(clazz) || unitCount.get(clazz) < requiredUnits.get(clazz)) {
				priorityClass = clazz;
				break;
			}
		}

		// Assign priorities
		for(QueueUnit q: queue) {
			// Priority Calculation when required unit does not exist
			if(priorityClass != null) {
				if(priorityClass.isAssignableFrom(q.unit.getClass())) q.setPriority(1000f);
				else q.setPriority(0f);
				
				continue;
			}
			
			// Default Priority Calculations
			DriveUnit unit = q.getUnit(); // Get unit
			// Ignore units whose frames don't match
			if(unit.getFrameCounter() == null || unit.getFrameCounter() == attack.getFrame()) {
				Class<? extends DriveUnit> clazz = unit.getClass(); // Get class
				Pod unitPod = unit.getPod(); // Get pod
				
				float unitCount = unitPod.getUnitCount();
				if(unitCount == 0) unitCount = 0.1f;
				
				float classCount = unitPod.countUnits(clazz);
				if(classCount == 0) classCount = 0.1f;
				
				float priority = (float) unit.getBasePriority() * unitPod.preferredCount(clazz) / unitCount / classCount;
				// If purpose matches, multiply by some multiplier
				if(unit.getPurpose() != Purpose.None && unit.getPurpose() == attack.getStrategy()) { priority *= PurposeScaling; }
				
				// Skirmish Switch
				if(unit instanceof SkirmishFella && !skirmish) { priority = 0f; }
				
				q.setPriority(priority);
			} else {
				q.setPriority(0f);
			}
			
		}
	}
	
	// Determine initial production of units
	public void initialUnits(HashMap<Class<? extends DriveUnit>, Integer> quota) {
		for(Class<? extends DriveUnit> clazz: quota.keySet()) {
			for(int i = 0; i < quota.get(clazz); i++) {
				String name = clazz.getName();
				
				// Search for QueueUnit, build it
				for(QueueUnit q: queue) { 
					if(q.unit.getClass().getName().equals(name)) {
						if(build(q)) break; 
					}
				}
			}
			
		}
		// Remove Produced Units 
		queue.removeIf(QueueUnit::hasBeenProduced);
	}
	
	public void draw(Graphics g) {
		player.addMessage(new PlayerMessage("Scaling Level: " + scalingLevel, Color.white));
		
		if(queue.size() > 0) {
			g.setColor(Color.white);
			player.addMessage(new PlayerMessage("----", Color.white));
			player.addMessage(new PlayerMessage("Skirmish Switch: " + skirmish, Color.white));
			player.addMessage(new PlayerMessage("Next Unit in Queue: " + highestPriority().unit.getName(), Color.white));
		}	
	}
	

	private QueueUnit highestPriority() {
		QueueUnit highestPriority = queue.get(0);
		for(QueueUnit q: queue) {
			if(q.getPriority() > highestPriority.getPriority()) {
				highestPriority = q;
			}
		}
		return highestPriority;
	}
	
	// Produce the Next Units in Queue
	private void produce() {
		boolean producing = true;
		
		while(producing) {
			if(player.getMyUnits().size() == player.getMaxFleetSize()) {
				producing = false;
				break;
			}
			// Update priorities
			updatePriorities();
		
			// Find the highest priority unit
			QueueUnit highestPriority = highestPriority();			
			if(!build(highestPriority)) {
				producing = false;
			}
		}
	}
	
	private boolean build(QueueUnit q) {
		if(player.getMinerals() >= q.unit.mineralValue()) {
			DriveUnit unit = q.getUnit();
			
			// Update Unit Count
			Class<? extends DriveUnit> clazz = unit.getClass();
			if(unitCount.containsKey(clazz)) {
				unitCount.replace(unit.getClass(), unitCount.get(unit.getClass()) + 1);
			} else {
				unitCount.put(clazz, 1);
			}
			
			player.buildUnit(unit); // Build Unit
			unit.getPod().addUnit(unit.getClass(), unit); // Add Unit to Pod
			q.replaceUnit(newUnit(unit)); // Replace unit with new unit
			
			return true;
		} 
		else return false;
	}
	
	protected DriveUnit newUnit(DriveUnit u) {
		// Create new unit
		DriveUnit unit = null;
		try {
			unit = (DriveUnit) u.getClass().getConstructors()[0].newInstance(player, u.getPod());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
		}
		return unit;
	}	
	
}