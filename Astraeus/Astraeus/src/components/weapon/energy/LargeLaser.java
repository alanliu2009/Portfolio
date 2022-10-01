package components.weapon.energy;

import animations.AnimationManager;
import animations.beams.AnimBeamBurst;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.instant.Damage;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class LargeLaser extends WeaponTargetUnit
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 650;
	public static final int DAMAGE = 43;
	public static final int USE_TIME = 60;
	public static final int COOLDOWN = 60;
	public static final float ACCURACY = .30f;

	public static final WeaponType WEAPON_TYPE = WeaponType.ENERGY;
	public static final DamageType DAMAGE_TYPE = DamageType.ENERGY;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;

	public static final int ANIM_BEAM_WIDTH = 9;
	public static final int ANIM_BEAM_DURATION = 15;
	
	public LargeLaser(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}

	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;				}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}

	protected void playAudio()
	{
		Sounds.laser.play(getOwner().getPosition(), .75f);
	}
	
	protected void animation(Unit a, boolean isHit) 
	{
		AnimationManager.add(new AnimBeamBurst(getOwner(), a, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION));		
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit)
		{
			target.addCondition(new Damage(getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE));
		}

	}
	


}
