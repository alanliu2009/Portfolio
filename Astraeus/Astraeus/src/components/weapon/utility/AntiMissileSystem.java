package components.weapon.utility;

import org.newdawn.slick.Color;

import animations.AnimationManager;
import animations.projectile.AnimProjectileBulletCombat;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetEntity;
import components.weapon.WeaponType;
import conditions.instant.Damage;
import objects.entity.Entity;
import objects.entity.missile.Missile;
import objects.entity.unit.Unit;

public class AntiMissileSystem extends WeaponTargetEntity
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 700;
	public static final float DAMAGE = 1;
	public static final int USE_TIME = 1;
	public static final int COOLDOWN = 2;
	public static final float ACCURACY = 1.0f;
	public static final int BULLET_TRAVEL_TIME_MAX = 30;
	public static final int BULLET_SIZE = 3;

	
	public static final WeaponType WEAPON_TYPE = WeaponType.UTILITY;
	public static final DamageType DAMAGE_TYPE = DamageType.TRUE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;

//	public static final int ANIM_BEAM_WIDTH = 3;
//	public static final int ANIM_BEAM_DURATION = 5;
	
	
	
	public AntiMissileSystem(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getMinRange() 						{	return 0;											}

	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	public int getSize()							{	return SIZE;								}
	public int getMaxTravelTime()					{	return BULLET_TRAVEL_TIME_MAX;									}
	public boolean rotateUser()						{	return false;	}

	
	protected void playAudio()
	{
		//Sounds.laser.play(getOwner().getPosition(), 1.50f);
	}

	protected void animation(Entity target) 
	{	
		AnimationManager.add(new AnimProjectileBulletCombat(getOwner(), target, BULLET_SIZE, getActualTravelTime(target), Color.gray));

//		AnimationManager.add(new AnimBeamBurst(getOwner(), target, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION));		
	}

	protected void activation(Entity target) 
	{
		target.addCondition(new Damage(getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE, getActualTravelTime(target)));
	}
	
	public void use()
	{
		Missile m;
		
		if(canUse())
		{
			m = getOwner().getNearestEnemyMissile();
			
			if(canUse(m))
			{
				
//				if(!(getOwner() instanceof BaseShip))
//				{
//					getOwner().turnTo(m);
//				}

				useLockTimer = getUseTime();
				start(m);
			}
		}		
	}
	
	
	
	public void use(Entity e)
	{
		use();
	}


}
