package components.upgrade;

import objects.entity.unit.Unit;

public class Structure extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int STRUCTURE = 300;
	public static final float HULL_REPAIR_EFFICIENCY = .25f;
	public static final float ACCELERATION_PENALTY = .1f;
	public static final float DEFENSE = .03f;

	public Structure(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();
		getOwner().increaseMaxStructure(STRUCTURE);
		getOwner().increaseHullRepairEfficiency((HULL_REPAIR_EFFICIENCY));
		getOwner().decreaseAccelerationByPercent(ACCELERATION_PENALTY);
		getOwner().decreaseDamageTakenMultiplier(DEFENSE);

	}

}
