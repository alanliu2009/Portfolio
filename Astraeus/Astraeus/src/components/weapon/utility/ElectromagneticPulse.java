package components.weapon.utility;

import java.util.ArrayList;

import animations.AnimationManager;
import animations.circles.AnimEMP;
import components.MovementPenalty;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.Stun;
import objects.entity.unit.Unit;

public class ElectromagneticPulse extends Weapon
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int DURATION = 240;
	public static final int USE_TIME = 100;
	public static final int COOLDOWN = 4000;
	public static final int RADIUS = 600;
	public static final int ANIM_DURATION = 80;
	public static final WeaponType WEAPON_TYPE = WeaponType.UTILITY;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;
	
	public ElectromagneticPulse(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public int getMinRange()						{ 	return 0;												}
	public int getMaxRange()						{ 	return RADIUS;											}
	public int getRadius()							{ 	return RADIUS;											}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public boolean appliesCondition()				{ 	return true;					}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Stun.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Stun.class).getTimeLeft();}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return Stun.class;					}
	public int getDuration()						{ 	return DURATION;	}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}

	protected void playAudio()
	{
		
	}
	
	protected void animation() 
	{
		AnimationManager.add(new AnimEMP(getOwner(), getRadius()*2, ANIM_DURATION));

	}
	
	protected void activation() 
	{
		ArrayList<Unit> enemies = getOwner().getEnemies();
		
		for(Unit u : enemies)
		{
			float d = getOwner().getDistance(u);
			if(d <  getRadius())
			{
				int delay = (int) (ANIM_DURATION * (d / getRadius()));
				applyStun(u, delay);
//				applyPulse(u, delay);
			}	
		}
	}
	
	public void applyStun(Unit u, int delay)
	{
		if(u.hasCondition(Stun.class) && u.getCondition(Stun.class).getDuration() < DURATION)
		{
			u.getCondition(Stun.class).setDuration(DURATION);
		}
		else
		{
			u.addCondition(new Stun(DURATION, delay));
		}
	}
	
	
//	public void applyPulse(Unit u, int delay)
//	{
//		if(u.hasCondition(Pulse.class) && u.getCondition(Pulse.class).getDuration() < DURATION)
//		{
//			u.getCondition(Pulse.class).setDuration(DURATION);
//		}
//		else
//		{
//			u.addCondition(new Pulse(DURATION, delay));
//		}
//	}
	
	


}
