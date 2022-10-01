package components.weapon.kinetic;

import animations.AnimationManager;
import animations.projectile.AnimProjectileFlakCombat;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnitSequentialFire;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.slow.FlakSlow;
import conditions.instant.Damage;
import engine.Utility;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class FlakBattery extends WeaponTargetUnitSequentialFire
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 600;
	public static final int NUM_SHOTS = 10;
	public static final int DAMAGE = 3;
	public static final int USE_TIME = 30;
	public static final int COOLDOWN = 60;

	public static final WeaponType WEAPON_TYPE = WeaponType.KINETIC;
	public static final DamageType DAMAGE_TYPE = DamageType.KINETIC;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;
	public static final float ACCURACY = .45f;

	public static final int DELAY_BETWEEN_SHOTS = 1;
	public static final int FLAK_SIZE_MIN = 4;
	public static final int FLAK_SIZE_MAX = 8;
	public static final float SLOW_AMOUNT = .50f;
	public static final int SLOW_DURATION_PER_HIT = 15;
	
	public static final int TRACER_MAX_TRAVEL_TIME = 30;
	
	public FlakBattery(Unit owner) 
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
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(FlakSlow.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(FlakSlow.class).getTimeLeft();}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return FlakSlow.class;					}

	public int getDuration()						{ 	return SLOW_DURATION_PER_HIT * NUM_SHOTS;	}

	protected void playAudio()
	{
		Sounds.mg.play(getOwner().getPosition(), 1.7f, .7f);
	}
	
	protected void animation(Unit target, boolean isHit) 
	{
		int modifiedDuration = (int) (Utility.random(.5, 1.5) * getActualTravelTime(target));
 		AnimationManager.add(new AnimProjectileFlakCombat(getOwner(), target, Utility.random(FLAK_SIZE_MIN, FLAK_SIZE_MAX), modifiedDuration));
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit)
		{
			if(target.hasCondition(FlakSlow.class))
			{
				target.getCondition(FlakSlow.class).extendDuration(SLOW_DURATION_PER_HIT);
			}
			else
			{
				target.addCondition(new FlakSlow(SLOW_DURATION_PER_HIT, SLOW_AMOUNT, getActualTravelTime(target)));
			}
			
			target.addCondition(new Damage(DAMAGE, DAMAGE_TYPE, getActualTravelTime(target)));
		}
	}
	
	
}
