package components.weapon.energy;

import animations.AnimationManager;
import animations.projectile.AnimProjectileBulletCombat;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnitSequentialFire;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.Pulse;
import conditions.instant.Damage;
import objects.entity.unit.Unit;

public class PulseCannon extends WeaponTargetUnitSequentialFire
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 500;
	public static final int NUM_SHOTS = 5;
	public static final int DAMAGE = 6;
	public static final int USE_TIME = 45;
	public static final int COOLDOWN = 45;
	public static final float ACCURACY = .30f;

	public static final WeaponType WEAPON_TYPE = WeaponType.ENERGY;
	public static final DamageType DAMAGE_TYPE = DamageType.ENERGY;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;

	public static final int DELAY_BETWEEN_SHOTS = 4;
	public static final int BULLET_SIZE = 8;
	public static final int PULSE_DURATION_PER_HIT = 120;
	
	public static final int TRACER_MAX_TRAVEL_TIME = 30;
	
	public PulseCannon(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST, DELAY_BETWEEN_SHOTS);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	
	public int getMaxTravelTime()					{	return TRACER_MAX_TRAVEL_TIME;	}
	public int getNumShots() 						{	return NUM_SHOTS;		}
	public boolean appliesCondition()				{ 	return true;					}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Pulse.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Pulse.class).getTimeLeft();}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return Pulse.class;					}

	public int getDuration()						{ 	return PULSE_DURATION_PER_HIT * NUM_SHOTS;	}

	protected void playAudio()
	{
//		Sounds.mg.play(getOwner().getPosition(), 1.7f, .7f);
	}
	
	protected void animation(Unit target, boolean isHit) 
	{
		AnimationManager.add(new AnimProjectileBulletCombat(getOwner(), target, BULLET_SIZE, getActualTravelTime(target)));		
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit)
		{
			if(target.hasCondition(Pulse.class))
			{
				target.getCondition(Pulse.class).extendDuration(PULSE_DURATION_PER_HIT);
			}
			else
			{
				target.addCondition(new Pulse(PULSE_DURATION_PER_HIT, getActualTravelTime(target)));
			}
			
			target.addCondition(new Damage(DAMAGE, DAMAGE_TYPE, getActualTravelTime(target)));
		}
	}
	
	
}
