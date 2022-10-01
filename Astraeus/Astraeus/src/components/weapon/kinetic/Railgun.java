package components.weapon.kinetic;

import animations.AnimationManager;
import animations.projectile.AnimProjectileTracerCombat;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.instant.Damage;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class Railgun extends WeaponTargetUnit
{
	public static final int SIZE = 3;
	public static final int MINERAL_COST = 3;
	public static final int TECH_COST = 0;
	public static final int MAX_RANGE = 800;
	public static final int DAMAGE = 75;
	public static final int USE_TIME = 90;
	public static final int COOLDOWN = 75;

	public static final WeaponType WEAPON_TYPE = WeaponType.KINETIC;
	public static final DamageType DAMAGE_TYPE = DamageType.PIERCE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;
	public static final float DAMAGE_TAKEN_SCALAR = 1.05f;
	//public static final int SHATTER_DURATION = 4 * 60;

	public static final float ACCURACY = .05f;

	public static final int TRACER_WIDTH = 14;
	public static final int TRACER_TRAVEL_TIME_MAX = 20;

	public Railgun(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	//public int getDuration()						{ 	return SHATTER_DURATION;	}

	public int getMaxTravelTime()					{	return TRACER_TRAVEL_TIME_MAX;	}

	protected void playAudio()
	{
		Sounds.laser.play(getOwner().getPosition(), .20f);
	}
	
	protected void animation(Unit target, boolean isHit) 
	{
		AnimationManager.add(new AnimProjectileTracerCombat(getOwner(), target, TRACER_WIDTH, getActualTravelTime(target)));
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit)
		{
			target.addCondition(new Damage(DAMAGE, DAMAGE_TYPE,  getActualTravelTime(target)));
			
//			No longer applies SHATTER
			
			//if(target.hasCondition(Shattered.class))
//			{
//				 target.getCondition(Shattered.class).setDuration(SHATTER_DURATION);
//			}
//			else
//			{
//				target.addCondition(new Shattered(DAMAGE_TAKEN_SCALAR, SHATTER_DURATION, getActualTravelTime(target)));
//			}

		}
	}
		

	





}
