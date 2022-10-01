package components.weapon.kinetic;

import animations.AnimationManager;
import animations.projectile.AnimProjectileTracerCombat;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.Stop;
import conditions.instant.Damage;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class MassDriver extends WeaponTargetUnit
{	
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int TECH_COST = 0;
	public static final int MAX_RANGE = 500;
	public static final int DAMAGE = 25;
	public static final int USE_TIME = 75;
	public static final int COOLDOWN = 60;
	//public static final float PIERCE_EFFECT = .75f;
	//public static final float PUSH_FORCE_AT_MAX_DISTANCE = 70f;
	public static final	int	DURATION = 1;
	
	public static final WeaponType WEAPON_TYPE = WeaponType.KINETIC;
	public static final DamageType DAMAGE_TYPE = DamageType.KINETIC;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.LIGHT;
	public static final float ACCURACY = .25f;

	public static final int TRACER_WIDTH = 12;
	public static final int TRACER_TRAVEL_TIME_MAX = 10;
	
	public MassDriver(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}

	public float getDamage() 						{	return DAMAGE;											}
	public int getMinRange() 						{	return 0;												}

	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());		}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	
	public int getMaxTravelTime()					{	return TRACER_TRAVEL_TIME_MAX;	}
	public boolean appliesCondition()				{ 	return true;					}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Stop.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Stop.class).getTimeLeft();}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return Stop.class;					}
	
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
//			target.addCondition(new Damage(DAMAGE * PIERCE_EFFECT, DamageType.STRUCTURE_ONLY,  getActualTravelTime(target)));		
		
	//		target.addCondition(new Stun(DURATION, getActualTravelTime(target)));
			target.addCondition(new Stop(DURATION, getActualTravelTime(target)));

		}
	}

}
