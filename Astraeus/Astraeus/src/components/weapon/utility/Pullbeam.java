package components.weapon.utility;

import animations.AnimationManager;
import animations.beams.AnimBeamConstantTransparent;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Unit;

public class Pullbeam extends WeaponTargetUnit
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int TECH_COST = SIZE;
	public static final int MAX_RANGE = 600;
	public static final float PULL_FORCE_AT_MAX_DISTANCE = 10f;
	public static final int USE_TIME = 0;
	public static final int COOLDOWN = 30;
	public static final int SLOW_DURATION = USE_TIME+COOLDOWN;

	public static final DamageType DAMAGE_TYPE = DamageType.NONE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.NONE;
	public static final float ACCURACY = 1.0f;

	public static final int ANIM_BEAM_WIDTH = 11;
	public static final int ANIM_BEAM_DURATION = USE_TIME+COOLDOWN;
	public static final int ANIM_BEAM_ALPHA = 75;

	public Pullbeam(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}

	protected void playAudio()
	{

	}
	
	protected void animation(Unit a, boolean isHit) 
	{	
		AnimationManager.add(new AnimBeamConstantTransparent(getOwner(), a, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION, ANIM_BEAM_ALPHA - 25, ANIM_BEAM_ALPHA + 25));		
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit && !(target instanceof BaseShip))
		{
			float percentDistance = Math.max(0, (getOwner().getDistance(target) - getMinRange()) / (MAX_RANGE));
			float pullReductionFromWeight = target.getFrame().getCost() / Frame.LIGHT.getCost();
			target.changeSpeed(PULL_FORCE_AT_MAX_DISTANCE * percentDistance / pullReductionFromWeight, target.getAngleToward(getOwner().getCenterX(), getOwner().getCenterY()));
		}
	
	}
	

}
