package components.weapon.utility;

import components.weapon.Weapon;
import conditions.buffs.Fast;
import objects.entity.unit.Unit;

public class SpeedBoost extends Weapon
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final float SPEED_MULTIPLIER = 2f;
	public static final int DURATION = 180;
	public static final int USE_TIME = 1;
	public static final int COOLDOWN = 360;
	
	public SpeedBoost(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public boolean appliesCondition()				{ 	return true;					}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Fast.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Fast.class).getTimeLeft();}
	public int getDuration()						{ 	return DURATION;	}

	protected void playAudio()
	{
		
	}
	
	protected void animation() 
	{
		
	}
	
	protected void activation() 
	{
		getOwner().addCondition(new Fast(SPEED_MULTIPLIER, DURATION));
	}
	
	


}
