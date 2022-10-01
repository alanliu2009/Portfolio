package components.weapon;

import objects.entity.Entity;
import objects.entity.unit.Unit;

public abstract class WeaponTargetUnitSequentialFire extends WeaponTargetUnit
{
	
	protected int shotsLeft;
	protected int delayLeft;
	protected int delayTotal;
	
	protected Unit myTarget;
	
	public WeaponTargetUnitSequentialFire(Unit owner, int size, int mineralCost, int delayTotal) 
	{
		super(owner, size, mineralCost);
		this.delayTotal = delayTotal;
	}

	protected void triggerActivationEffects(Entity target)
	{
		myTarget = (Unit) target;		
		shotsLeft = getNumShots();
		useCooldownTimer = getCooldown();
		playAudio();
	}
	
	public void update()
	{
		super.update();
		
		if(delayLeft > 0)
		{
			delayLeft--;
		}
		
		if(shotsLeft > 0 && delayLeft == 0)
		{
			boolean isHit = myTarget.rollToHit(getAccuracy());
						
			//System.out.println(this + " my accuracy " + getAccuracy());

			
			activation(myTarget, isHit);
			animation(myTarget, isHit);
						
			shotsLeft--;
			
			if(shotsLeft == 0)
			{
				myTarget = null;
			}
			else
			{
				delayLeft = delayTotal;
			}
		}
	}
	
	protected void activation(Unit target)
	{
		// do nothing
	}
	
	protected void animation(Unit target)
	{
		// do nothing
	}

}
