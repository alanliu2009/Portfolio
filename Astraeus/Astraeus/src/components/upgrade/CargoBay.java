package components.upgrade;

import objects.entity.unit.Unit;

public class CargoBay extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int CAPACITY = 6;		

	public CargoBay(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();	
		
		getOwner().increaseCapacity(CAPACITY);
	}


}
