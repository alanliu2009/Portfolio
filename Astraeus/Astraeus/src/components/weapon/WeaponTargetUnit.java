package components.weapon;

import objects.entity.Entity;
import objects.entity.unit.Unit;

public abstract class WeaponTargetUnit extends WeaponTargetEntity
{
	public WeaponTargetUnit(Unit owner, int size, int mineralCost) 
	{
		super(owner, size, mineralCost);
	}
	
	abstract protected void animation(Unit target, boolean isHit);
	abstract protected void activation(Unit target, boolean isHit);
	
	protected void triggerActivationEffects(Entity target)
	{
		super.triggerActivationEffects(target);
	
		Unit myTarget = (Unit) target;
		boolean isHit = myTarget.rollToHit(getAccuracy());
		
		//System.out.println(this + " my accuracy " + getAccuracy());
		
		animation(myTarget, isHit);
		activation(myTarget, isHit);

	}
	
	
	protected void animation(Entity target)
	{	
		// do nothing
	}
	
	protected void activation(Entity target)
	{
		// do nothing
	}
	

	

}
