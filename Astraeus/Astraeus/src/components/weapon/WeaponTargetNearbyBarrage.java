package components.weapon;

import engine.Utility;
import objects.entity.unit.Unit;

public abstract class WeaponTargetNearbyBarrage extends WeaponTargetNearby
{
	int delayMin;
	int delayMax;
	public WeaponTargetNearbyBarrage(Unit owner, int size, int mineralCost, int delayMin, int delayMax) 
	{
		super(owner, size, mineralCost);
		this.delayMin = delayMin;
		this.delayMax = delayMax;
	}
	
	public void checkForHitAndActivateWeapons(Unit u)
	{
		for(int i = 0; i < getNumShots(); i++)
		{
			boolean isHit = u.rollToHit(getAccuracy());
			int delay = Utility.random(delayMin, delayMax);
			activation(u, isHit, delay);
			animation(u, isHit, delay);
		}
	}
	
	abstract protected void activation(Unit target, boolean isHit, int delay);
	abstract protected void animation(Unit target, boolean isHit, int delay);

	protected void activation(Unit target, boolean isHit)
	{
		// do nothing
	}
	
	protected void animation(Unit target, boolean isHit)
	{
		// do nothing
	}

}
