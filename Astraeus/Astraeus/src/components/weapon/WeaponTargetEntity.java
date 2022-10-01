package components.weapon;

import org.newdawn.slick.Graphics;

import engine.Settings;
import objects.entity.Entity;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;

public abstract class WeaponTargetEntity extends Weapon
{
	/*************** Data ***************/

	private Entity lockedTarget;
	
	/*************** Constructor ***************/
	
	public WeaponTargetEntity(Unit owner, int size, int mineralCost) 
	{
		super(owner, size, mineralCost);
	}

	/*************** Every Frame ***************/

	public void update()
	{	
		updateTimers();
		
		// Every frame make sure my target is still valid and in range, and that I can act, or break the lock
		if(lockedTarget != null && lockedTarget.isAlive() && inRange(lockedTarget) && getOwner().canAct())
		{
			// Then check if I can actually fire - (includes a check for useLockTimer)
			if(canUse(lockedTarget))
			{
				triggerActivationEffects(lockedTarget);
				end();
			}
			
			// Keep rotating toward the target
			else if(!(getOwner() instanceof BaseShip) && rotateUser())
			{
				getOwner().rotate(getOwner().getAngleToward(lockedTarget.getCenterX(), lockedTarget.getCenterY()));
			}
	

		}
		
		// If not, abandon the attack
		else
		{
			end();
		}
	}
	
	public void render(Graphics g)
	{
		if(Settings.dbgShowWeaponAim && lockedTarget != null && inRange(lockedTarget))
		{
			g.setColor(getOwner().getPlayer().getColorPrimary().darker().darker());
			g.setLineWidth(1);
			g.drawLine(getOwner().getCenterX(), getOwner().getCenterY(), lockedTarget.getCenterX(), lockedTarget.getCenterY());
			g.resetLineWidth();
		}
	}

	/*************** Use Methods ***************/
	
	public boolean canUse(Entity target) 
	{
		return super.canUse() && target != null && target != getOwner() && inRange(target);
	}
		
	public void use(Entity target) 
	{
		if (canUse(target)) 
		{
			// Lock onto target and wait to fire.
			if(!(getOwner() instanceof BaseShip))
			{
				getOwner().turnTo(target);
			}
			useLockTimer = getUseTime();
			start(target);
		}
	}
	
	public void use()
	{
		getOwner().dbgMessage("Warning: called use() on + " + this + " without a target.");
	}
	
	protected void activation()
	{
		// No activation effect on self by default
	}
	
	protected void animation()
	{
		// No animation effect on self by default
	}
	
	public void start(Entity target)
	{
		super.start();
		lockedTarget = target; 
	}
	
	public void end()
	{
		super.end();
		lockedTarget = null;
	}
	
	abstract protected void animation(Entity target);
	abstract protected void activation(Entity target);

	protected void triggerActivationEffects(Entity target)
	{
		animation(target);
		activation(target);
		useCooldownTimer = getCooldown();
		playAudio();
	}
	
	public float getRangePercent(Entity target)
	{
		return getOwner().getDistance(target) / (float) getMaxRange();
	}
	
	public int getActualTravelTime(Entity target)
	{
		return (int) (getMaxTravelTime() * getRangePercent(target));
	}
	
	public int getMaxTravelTime()
	{
		return 1;
	}
	
}
