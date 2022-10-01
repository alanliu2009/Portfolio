package teams.student.power.units.resourceUnits;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import components.weapon.Weapon;
import components.weapon.resource.MiningLaser;
import objects.entity.Entity;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.power.PowerPlayer;
import teams.student.power.units.PowerUnit;

public class Miner extends PowerUnit 
{
	//this target can be moved to PowerUnit to give it to all units, unless Astraeus already has one built in
	
	//this is just for debugging 
	String state;
	
	//variables for circle movement
	float totalError;
	float lastError;
	float currError;
	
	
	public Miner(PowerPlayer p)  
	{
		super(p);
		state = "";
		
		pkp = 0.7f; 
		pki = 0.004f; 
		pkd = 0.5f; 
		
		nkp = 0.05f; 
		nki = 0.005f;
		nkd = 20f;
		//p.getResourceManager().assignBuddies(this);
	}
	
	public void design()
	{
		setFrame(Frame.LIGHT);
		setStyle(Style.BUBBLE);
		addWeapon(new MiningLaser(this));
	}

	public void action() 
	{
		//harvestNearest(getWeaponOne());
	}

	public void harvestNearest(Weapon w)
	{
		harvest(getHomeBase().getNearestNode());
	}
	
	public void harvest(Node n)
	{
		target = n;
		// Approach the node
		if(getDistance(n) > getWeaponOne().getMaxRange())
		{
			moveTo(n);
			state = "approach";
			//reset error variables here?
			lastError = 0;
		}
		
		// Back up if I'm too close
		else if(getDistance(n) < getWeaponOne().getMaxRange())
		{
			circle(n,getWeaponOne().getMaxRange()*0.9f, 1);
			//moveTo(getHomeBase());
		}
		
		getWeaponOne().use(n);
	}
	
	public boolean getAssignedState() {
		return assigned;
	}
	
	public void setAssignedState(boolean s) {
		assigned = s;
	}
	
	public void draw(Graphics g) {
//		if(state.equals("approach")){
//			g.setColor(Color.cyan);
//		}else if(state.equals("too close")) {
//			g.setColor(Color.red);
//		}else if(state.equals("too far")) {
//			g.setColor(Color.green);
//		}else if(state.equals("hard limit")) {
//			g.setColor(Color.orange);
//		}else if(state.equals("target acquired")) {
//			g.setColor(Color.blue);
//		}
		if(target!=null) {
			g.setColor(Color.green);
			//g.drawLine(target.getCenterX(), target.getCenterY(), this.getCenterX(), this.getCenterY());
			float a = getWeaponOne().getMaxRange()*0.9f;
			float b = getWeaponOne().getMaxRange();
			g.drawOval(target.getCenterX() - a, target.getCenterY() - a, a*2, a*2);
			g.drawOval(target.getCenterX() - b, target.getCenterY() - b, b*2, b*2);
			
		}
		
	}
	
}
