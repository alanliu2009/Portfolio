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

public class MachineGun extends WeaponTargetUnitSequentialFire
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int MAX_RANGE = 400;
	public static final int DAMAGE = 2;
	public static final int NUM_SHOTS = 9;
	public static final int USE_TIME = 30;
	public static final int COOLDOWN = 60;

	public static final int BULLET_DELAY = 3;
	public static final int BULLET_TRAVEL_TIME_MAX = 15;
	public static final int BULLET_SIZE = 5;

	public static final WeaponType WEAPON_TYPE = WeaponType.KINETIC;
	public static final DamageType DAMAGE_TYPE = DamageType.KINETIC;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.LIGHT;
	public static final float ACCURACY = .45f;
	
	public MachineGun(Unit owner) 
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
	public int getNumShots()						{	return NUM_SHOTS;				}
	public int getMaxTravelTime()					{	return BULLET_TRAVEL_TIME_MAX;	}



	protected void playAudio()
	{
		Sounds.mg.play(getOwner().getPosition(), 1.7f, .7f);
	}
	
	protected void animation(Unit a) 
	{

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
