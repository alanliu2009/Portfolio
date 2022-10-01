package components.weapon.energy;

import org.newdawn.slick.Color;

import animations.AnimationManager;
import animations.beams.AnimBeamBurst;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.instant.Damage;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class Brightlance extends WeaponTargetUnit
{
	public static final int SIZE = 3;
	public static final int MINERAL_COST = 3;
	public static final int MAX_RANGE = 800;
	public static final int DAMAGE = 66;
	public static final int USE_TIME = 60;
	public static final int COOLDOWN = 60;
	public static final float ACCURACY = .10f;

	public static final WeaponType WEAPON_TYPE = WeaponType.ENERGY;
	public static final DamageType DAMAGE_TYPE = DamageType.ENERGY;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;

	public static final int ANIM_BEAM_WIDTH = 20;
	public static final int ANIM_BEAM_DURATION = 15;
	
	public Brightlance(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;										}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());	}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());		}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;									}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;									}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;							}

	protected void playAudio()
	{
		Sounds.laser.play(getOwner().getPosition(), .50f);
	}
	
	protected void animation(Unit a, boolean isHit) 
	{
		Color c = getOwner().getColorPrimary();
		Color brightBeamColor = new Color((int) ((c.getRed() * 2 + 255)/3), (int) ((c.getGreen() * 2 + 255) / 3), (int) ((c.getBlue() * 2 + 255) / 3));
		AnimationManager.add(new AnimBeamBurst(getOwner(), a, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION, brightBeamColor));		
	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		if(isHit)
		{
			target.addCondition(new Damage(getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE));
		}

	}






}
