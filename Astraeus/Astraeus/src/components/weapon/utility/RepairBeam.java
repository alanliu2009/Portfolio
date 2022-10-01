package components.weapon.utility;

import animations.AnimationManager;
import animations.beams.AnimBeamConstantTransparent;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.instant.Repair;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class RepairBeam extends WeaponTargetUnit
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 500;
	public static final float HEAL = 7f;
	public static final int USE_TIME = 0;
	public static final int COOLDOWN = 30;

	public static final WeaponType WEAPON_TYPE = WeaponType.UTILITY;
	public static final DamageType DAMAGE_TYPE = DamageType.NONE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;
	public static final float ACCURACY = 1.00f;

	public static final int ANIM_BEAM_WIDTH = 14;
	public static final int ANIM_BEAM_DURATION = USE_TIME+COOLDOWN;
	
	public RepairBeam(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public int getMinRange() 						{	return 0;												}
	public float getDamage() 						{	return 0f;									}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}

	public boolean canUse(Entity target) 
	{
		return super.canUse() && target != null && inRange(target) && target.isHullDamaged();
	}
	
	protected void playAudio()
	{

	}
	
	protected void animation(Unit a, boolean isHit) 
	{	
		AnimationManager.add(new AnimBeamConstantTransparent(getOwner(), a, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION, 50, 100));		
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		target.addCondition(new Repair(HEAL));
	}
	

}
