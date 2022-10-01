package components.upgrade;

import objects.entity.unit.Unit;

public class OptimizedAlgorithms extends Upgrade
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final float USE_TIME_SCALAR = .80f;
	public static final float COOLDOWN_SCALAR = .80f;

	public OptimizedAlgorithms(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public void onAddition()
	{
		super.onAddition();
		getOwner().applyUseMultiplier(USE_TIME_SCALAR);
		getOwner().applyCooldownTimeMultiplier(COOLDOWN_SCALAR);
	}

}
