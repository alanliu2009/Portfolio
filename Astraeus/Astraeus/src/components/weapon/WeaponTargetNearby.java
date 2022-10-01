package components.weapon;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import engine.Settings;
import objects.entity.unit.Unit;

public abstract class WeaponTargetNearby extends Weapon
{
	/*************** Data ***************/
	
	private ArrayList<Unit> lockedTargets;
	
	/*************** Constructor ***************/
	
	public WeaponTargetNearby(Unit owner, int size, int mineralCost) 
	{
		super(owner, size, mineralCost);
	}

	/*************** Every Frame ***************/

	public void update()
	{
		updateTimers();
		
		if(lockedTargets != null)
		{
			// Each frame, remove any units that moved away or died
			for(int i = 0; i < lockedTargets.size(); i++)
			{
				Unit target = lockedTargets.get(i);
				
				if(target == null || target.isDead() || !inRange(target))
				{
					lockedTargets.remove(i);
					i--;
				}
			}
			
			// If I still have valid targets and can fire
			if(lockedTargets.size() > 0 && canUse())
			{			
				for(Unit u : lockedTargets)
				{
					checkForHitAndActivateWeapons(u);
				}
				useCooldownTimer = getCooldown();
				playAudio();
				end();
			}
			// If I am out of targets or cannot fire, end it
			else if(lockedTargets.size() == 0 || !getOwner().canAct())
			{
				end();
			}
		}
	}
	
	public void checkForHitAndActivateWeapons(Unit u)
	{
		boolean isHit = u.rollToHit(getAccuracy());
		activation(u, isHit);
		animation(u, isHit);
	}
	
	public void render(Graphics g)
	{
		if(lockedTargets != null)
		{	
			for(Unit target : lockedTargets)
			{
				if(Settings.dbgShowWeaponAim && target != null && inRange(target))
				{
					g.setColor(getOwner().getPlayer().getColorPrimary().darker().darker());
					g.setLineWidth(1);
					g.drawLine(getOwner().getCenterX(), getOwner().getCenterY(), target.getCenterX(), target.getCenterY());
					g.resetLineWidth();
				}
			}
		}
	}
	
	/*************** Use Methods ***************/
	
	public boolean canUse()
	{
		ArrayList<Unit> targets = getValidTargets();
		return super.canUse() && targets != null && targets.size() > 0;
	}
	
	public void use()
	{
		if(canUse())
		{
			ArrayList<Unit> targets = getValidTargets();
			useLockTimer = getUseTime();
			start(targets);
		}
	}

	protected ArrayList<Unit> getValidTargets()
	{
		return getOwner().getNearestEnemiesInRadius(getMaxRange(), getNumTargets());
	}
	
	public abstract int getNumTargets();
	
	public void start(ArrayList<Unit> targets)
	{
		super.start();
		lockedTargets = targets; 
	}
	
	public void end()
	{
		super.end();
		lockedTargets = null;
	}
	
	protected void activation()
	{
		// do nothing
	}
	
	protected void animation()
	{
		// do nothing
	}
	
	protected abstract void animation(Unit target, boolean isHit);
	protected abstract void activation(Unit target, boolean isHit);		
}
