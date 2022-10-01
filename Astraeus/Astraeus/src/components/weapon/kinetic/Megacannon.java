package components.weapon.kinetic;

import animations.AnimationManager;
import animations.projectile.AnimProjectileBulletCombat;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnitSequentialFire;
import components.weapon.WeaponType;
import conditions.instant.Damage;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class Megacannon extends WeaponTargetUnitSequentialFire
{
	public static final int SIZE = 3;
	public static final int MINERAL_COST = 3;
	public static final int TECH_COST = 0;
	public static final int MAX_RANGE = 600;
	public static final int DAMAGE = 21;
	public static final int NUM_SHOTS = 3;
	public static final int USE_TIME = 30;
	public static final int COOLDOWN = 60;

	public static final int BULLET_SIZE = 10;
	public static final int BULLET_TRAVEL_TIME_MAX = 24;
	public static final int BULLET_DELAY = 6;
	
	public static final WeaponType WEAPON_TYPE = WeaponType.KINETIC;
	public static final DamageType DAMAGE_TYPE = DamageType.KINETIC;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;
	public static final float ACCURACY = .05f;

	public Megacannon(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST, BULLET_DELAY);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	
	public int getMaxTravelTime()					{	return BULLET_TRAVEL_TIME_MAX;			}
	public int getNumShots()						{	return NUM_SHOTS;						}


	protected void playAudio()
	{
		Sounds.mg.play(getOwner().getPosition(), .8f, .8f);
	}
		
	protected void animation(Unit target, boolean isHit)
	{
		AnimationManager.add(new AnimProjectileBulletCombat(getOwner(), target, BULLET_SIZE, getActualTravelTime(target)));
	}
	
	protected void activation(Unit target, boolean isHit)
	{
		if(isHit)
		{
			target.addCondition(new Damage(DAMAGE, DAMAGE_TYPE, getActualTravelTime(target)));
		}
	}




}
