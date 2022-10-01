package teams.student.power.resources.groups;

import java.util.ArrayList;

import teams.student.power.resources.Point;
import teams.student.power.units.PowerUnit;

public class Buddies 
{
	protected ArrayList<PowerUnit> units;
	protected Point fleetCenter;
	protected Point target;
	protected float maxSpread;
	
	public Buddies()
	{
		units = new ArrayList<PowerUnit>();
		fleetCenter = new Point(0, 0);
		target = new Point(0, 0);
		maxSpread = 0;
	}
	
	public Point getFleetCenter()	{	return fleetCenter;	}
	public ArrayList<PowerUnit> getUnits()	{	return units;	}
	
	public void setTarget(Point p) { target.setX(p.getX()); target.setY(p.getY()); }
	public void setTarget(float x, float y) { target.setX(x); target.setY(y); }
	public void addUnit(PowerUnit u) { units.add(u); } 
	public void clearUnits() { units.clear(); }
	public void moveUnits(Point p)
	{
		for(PowerUnit u : units)
		{
			u.moveTo(p.getX(), p.getY());
		}
	}
	
	public void update()
	{
		calcCenter();
		groupUnits();
		moveUnits(target);
	}
	
	public void calcCenter()
	{
		float tempX = 0;
		float tempY = 0;
		
		for(PowerUnit u : units)
		{
			tempX += u.getCenterX();
			tempY += u.getCenterY();
		}
		
		tempX /= units.size();
		tempY /= units.size();
		
		fleetCenter.setX(tempX);
		fleetCenter.setY(tempY);
	}
	
	public Point calcCenter(ArrayList<PowerUnit> subUnits)
	{	
		Point temp = new Point(0, 0);
		
		if(subUnits == null) return temp;
		
		for(PowerUnit u : subUnits)
		{
			temp.setX(u.getCenterX() + temp.getX());
			temp.setY(u.getCenterY() + temp.getY());
		}
		
		temp.setY(temp.getY() / subUnits.size());
		temp.setX(temp.getX() / subUnits.size());
		
		return(temp);
	}
	
	public void groupUnits()
	{
		for(PowerUnit u : units)
		{
			if(u.getDistance(fleetCenter.getX(), fleetCenter.getY()) > maxSpread) u.moveTo(fleetCenter.getX(), fleetCenter.getY());
		}
	}
}
