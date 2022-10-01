package teams.student.power.units.resourceUnits;


import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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

public class Gatherer extends PowerUnit 
{	
	private float sleep;
	private float sleepTimer;
	private float overUse;
	private float overUseTimer;
	
	private float thetaError;  //error from homebase
	private float vTheta;  //velocity angle
	private float maxThrowError;
	private Point target;
	private Unit danger;
	private float evadeRange;
	private float eTurn;
	
	//debug
	private ArrayList<Resource> targetResources;

	private boolean showTargets;

	public Gatherer(PowerPlayer p)  
	{
		super(p);
		
		sleep = 0;
		sleepTimer = 240;
		overUse = 0;
		overUseTimer = 30;
		
		thetaError = 0;
		vTheta = 0;
		maxThrowError = 20;

		showTargets = true;
		targetResources = new ArrayList<Resource>();
		danger = null;
		
		evadeRange = 0;
		eTurn = 0;
	}
	
	
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.BUBBLE);
		addWeapon(new Collector(this));
		addUpgrade(new CargoBay(this));
	}
	
	//----------------
	// debug draw
	//----------------
	
	public void draw(Graphics g)
	{
		g.setColor(new Color(220, 60, 255));

		if(showTargets)
		{
			float a = 0.6f;
			float aTemp = a;
			
			for(Resource r : targetResources)
			{
				g.setColor(new Color(0.5f, 0.2f, 1f, a));
				g.drawLine(r.getCenterX(), r.getCenterY(), this.getCenterX(), this.getCenterY());
				a -= (aTemp / (float)targetResources.size()) * 0.8f;
			}
			
			g.setColor(new Color(255, 0, 0, 0.8f));
			
			g.drawLine(this.getCenterX(), this.getCenterY(), getHomeBase().getCenterX(), getHomeBase().getCenterY());
			g.drawLine(this.getCenterX(), 
					this.getCenterY(), 
					this.getCenterX() + 1000f * (float)Math.cos(theta * 3.1415962 / 180), 
					this.getCenterY() + 1000f * (float)Math.sin(theta * 3.1415962 / 180));
		}
	}
	
	//----------------
	// actual stuff
	//----------------
	
	public void action() 
	{	
		danger = getNearestHostileEnemy();
		
		evade(); //actually nevermind
		
		if(sleep == sleepTimer - 1)
		{
			turn(180);
		}
		else if(sleep > 0)
		{
			move();
		}
		else if(throwing())
		{
			collect();
			returnResources();
		} 
		else
		{
			collect();
			gatherNearbyResources();	
			timer++;
		}
		
		overUse--;
		sleep--;
	}

	public void gatherNearbyResources()
	{
		if(hasCapacity())
		{	
			ArrayList<Point> targets = getPowerPlayer().getResourceManager().getTargetClusters();
			ArrayList<Point> viableTargets = new ArrayList<Point>();
			
			for(Point p : targets)
			{
				if(getPowerPlayer().getResourceManager().checkCluster(p)
						&& danger != null
						&& danger.getDistance(p.getX(), p.getY()) - getDistance(p.getX(), p.getY()) > 0) //if enemy is closer
				{
					target = p;
					viableTargets.add(p);
				}
			}
			
			target = nearestCluster(viableTargets);
			
			Resource r = nearestResource();
		
			if(r != null)
			{
				if(target != null && getDistance(r) > 200)
				{
					moveTo(target.getX(), target.getY());
				}
				else
				{
					target = new Point(r.getCenterX(), r.getCenterY());
					
					moveTo(r);
				}
			} 
		}
	}
	
	protected void collect()
	{
		if(target == null) return;
		
		boolean check = getDistance(nearestResource()) < Collector.MAX_RANGE * 0.3f 
				|| getDistance(target.getX(), target.getY()) < Collector.MAX_RANGE * 0.7f;
		
		if(hasCapacity() && (check || overUse > 0) )
		{
			if(check)
			{
				overUse = overUseTimer;
			}
			
			if(getWeaponOne() != null && getWeaponOne() instanceof Collector)
			{
				getWeaponOne().use();
			} 
			else if(getWeaponTwo() != null && getWeaponTwo() instanceof Collector)
			{
				getWeaponTwo().use();
			}
		}
	}
	
	public void returnResources()
	{	
		vTheta = (float) Math.atan(this.ySpeed / this.xSpeed) * 180f / 3.1415926f;
		
		if (vTheta < 0) 
		{
			vTheta = 360 + vTheta;
		}
		
		if (xSpeed < 0)
		{
			vTheta = (vTheta + 180) % 360;
		}
		
		thetaError = vTheta - getAngleToward(getHomeBase().getCenterX(), getHomeBase().getCenterY());	
		
		if(getCurSpeed() > Resource.RESOURCE_SPEED
				&& Math.abs(thetaError) < Math.abs(Math.tan(maxThrowError / (float) getDistance(getHomeBase()) )) * 180f / 3.1415926 ) 
		{
			dump();
			sleep = sleepTimer;
		} 
		else
		{
			float turn = 0;	
			
			if(Math.abs(thetaError) < 30) 
			{
				turn = thetaError * 5f;	
			} 
			
			turnTo( 
					getAngleToward( getHomeBase().getCenterX(), getHomeBase().getCenterY() ) - turn
					);
			move();
		}

		deposit();
	}

	public boolean throwing()
	{
		float distance = 0;
		
		if(target != null)
		{
			distance = getDistance(target.getX(), target.getY());
		} 
		
		if((distance > 1000 && (float) this.getCargoPercent() > 0.2f) || this.isFull() ) return true;
		
		return false;
	}
	
	public void evade()
	{
		if(danger == null) return;
		
		if(getDistance(danger) < 1000)
		{
			evadeRange = 2000;
			eTurn = 180 * ( 
					(evadeRange - getDistance(danger)) 
					/ evadeRange);
		
			if(getDistance(danger) < evadeRange)
			{	
				turnTo( getAngleToward(danger.getCenterX(), danger.getCenterY()) + eTurn );
				move();
			} 
			else
			{
				moveTo(danger);
			} 
		} 
		else if(getDistance(danger) < 2000)
		{
			moveTo(this.getHomeBase());
		}
	}
	
	//----------------
	// utility
	//----------------
	
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
			if(clazz.isInstance(r) 
					&& getDistance(r) < nearestDistance 
					&& (r.getCurSpeed() < Resource.RESOURCE_SPEED * 0.9f || getDistance(r) < 80)
					&& powerPlayer.getResourceManager().checkResource(r))
			{
				nearestResource = r;
				nearestDistance = getDistance(r.getPosition());
				powerPlayer.getResourceManager().addResource(r);
			}
		}
		
		return nearestResource;
	}
	
	public Point nearestCluster(ArrayList<Point> c)
	{
		float nearestDistance = Float.MAX_VALUE;
		Point nearestCluster = null;
		ArrayList<Point> clusters = c;

		for(Point p : clusters)
		{
			if(getDistance(p.getX(), p.getY()) < nearestDistance)
			{
				nearestCluster = p;
				nearestDistance = getDistance(p.getX(), p.getY());
			}
		}
		
		getPowerPlayer().getResourceManager().addCluster(nearestCluster);
		return nearestCluster;
	}
}
