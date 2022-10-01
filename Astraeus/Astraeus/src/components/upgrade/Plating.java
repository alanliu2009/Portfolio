package components.upgrade;

import objects.entity.unit.Unit;

public class Plating extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int PLATING = 300;
	//public static final float ACCELERATION_PENALTY = .1f;
	public static final float DEFENSE = .06f;

	public Plating(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();		
		getOwner().increaseMaxPlating(PLATING);		
//		getOwner().decreaseAcceleration(getOwner().getFrame().getAcc() * ACCELERATION_PENALTY);
		getOwner().decreaseDamageTakenMultiplier(DEFENSE);
	}


}
