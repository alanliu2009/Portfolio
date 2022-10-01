package teams.student.power.units.resourceUnits;

import components.upgrade.CargoBay;
import components.weapon.resource.Collector;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import teams.student.power.PowerPlayer;
import teams.student.power.resources.Point;
import teams.student.power.units.PowerUnit;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Camper extends PowerUnit{

	protected int patrolRange; 
	
	public Camper(PowerPlayer p) {
		super(p);
		kp = 0.25f;
		kd = 0;
		
		pkp = .8f; 
		pki = 0.00f; 
		pkd = 0.7f; 
		
		nkp = 0.0f; 
		nki = 0.00f;
		nkd = 20f;
		
		patrolRange = 200;
	}
	
	public void design() {
		setFrame(Frame.MEDIUM);
		setStyle(Style.BUBBLE);
		addWeapon(new Collector(this));
		addUpgrade(new CargoBay(this));
	}
	
	
	public void action() {
		//if full cargo bay, deposit
		//if resource incoming, catch
		//if no resources incoming immediately, deposit
		enemyFleets();
		
		target = getNearestResource();
		
		dropOff();
		camp();
		
		if(getDistance(target) < patrolRange && getDistance(target) < getWeaponOne().getMaxRange() * 0.3f)
		{
			getWeaponOne().use();
		}
	}
	
	public void camp()
	{
		if(this.getHomeBase() == null) return;
		
		float evadeRange = patrolRange * 0.02f;
		float eTurn = 180 * ( 
				(evadeRange - getDistance(getHomeBase())) 
				/ evadeRange);
	
		if(getDistance(getHomeBase()) < evadeRange)
		{	
			turnTo( getAngleToward(getHomeBase().getCenterX(), getHomeBase().getCenterY()) + eTurn );
			move();
		} 
		else
		{
			moveTo(getHomeBase());
		}
	}
	
	public void dropOff() {
		if(!this.isEmpty())
		{
			moveTo(getHomeBase());//tries to not overshoot home base
		}	
		
		deposit();
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.yellow);
		if(target != null) {
			g.drawLine(this.getCenterX(), this.getCenterY(), target.getCenterX(), target.getCenterY());
		}
		g.setColor(Color.green);
		g.drawOval(getHomeBase().getCenterX()-patrolRange/3, getHomeBase().getCenterY()-patrolRange/3, patrolRange/1.5f, patrolRange/1.5f);
	}
	
	//to get the nearestResource
	
	public Resource nearestResource()
	{
		return nearestResource(Resource.class);
	}
	
	public Resource nearestResource(Class<? extends Resource> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Resource nearestResource = null;
		ArrayList<Resource> resources = ResourceManager.getResources();

		for(Resource r : resources)
		{
			if(clazz.isInstance(r) && getDistance(r) < nearestDistance && 
					(r.getCurSpeed() > 1f || getDistance(r) < patrolRange))
			{
				nearestResource = r;
				nearestDistance = getDistance(r.getPosition());
			}
		}
		
		return nearestResource;
	}
}
