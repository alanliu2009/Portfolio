package components.weapon.energy;

import animations.AnimationManager;
import animations.beams.AnimBeamConstantTransparent;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.slow.BeamSlow;
import conditions.instant.Damage;
import objects.entity.unit.Unit;

public class EnergySiphon extends WeaponTargetUnit
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int MAX_RANGE = 400;
	public static final int DAMAGE = 3;
	public static final float SHIELD_REGEN = .50f;
	public static final int USE_TIME = 0;
	public static final int COOLDOWN = 30;
	public static final int SLOW_DURATION = USE_TIME+COOLDOWN;
	public static final float SLOW_AMOUNT = 0.50f;
	public static final float ACCURACY = 1.0f;

	public static final WeaponType WEAPON_TYPE = WeaponType.ENERGY;
	public static final DamageType DAMAGE_TYPE = DamageType.ENERGY;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;

	public static final int ANIM_BEAM_WIDTH = 7;
	public static final int ANIM_BEAM_DURATION = USE_TIME+COOLDOWN;
	
	public EnergySiphon(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public boolean appliesCondition()				{ 	return true;					}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(BeamSlow.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(BeamSlow.class).getTimeLeft();}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return BeamSlow.class;					}

	public int getDuration()						{ 	return SLOW_DURATION;	}
	
	protected void playAudio()
	{

	}
	
	protected void animation(Unit a, boolean isHit) 
	{	
		AnimationManager.add(new AnimBeamConstantTransparent(getOwner(), a, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION));		
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit)
		{
			target.addCondition(new Damage(getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE));
			target.addCondition(new BeamSlow(SLOW_DURATION, SLOW_AMOUNT));
			getOwner().regainShield(getModifiedDamage(DAMAGE, DAMAGE_TYPE) * SHIELD_REGEN);
		}
	
	}
	

}
