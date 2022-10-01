package components.upgrade;

import objects.entity.unit.Unit;

public class Rangefinder extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final float ACCURACY_SCALAR = 1.35f;
	public static final float MAX_RANGE_SCALAR = 1.15f;

	public Rangefinder(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();
		getOwner().applyAccuracyMultiplier((ACCURACY_SCALAR));
		getOwner().applyMaxRangeMultiplier(MAX_RANGE_SCALAR);
	}

}
