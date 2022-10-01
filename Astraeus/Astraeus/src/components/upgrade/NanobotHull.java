package components.upgrade;

import objects.entity.unit.Unit;

public class NanobotHull extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final float HULL_REPAIR_PER_SECOND = 6f;	
	
	public NanobotHull(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();
		getOwner().increaseHullRepair((HULL_REPAIR_PER_SECOND));
	}

}
