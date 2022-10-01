package components.weapon.explosive;

import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import engine.Utility;
import engine.states.Game;
import objects.entity.missile.Missile;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class ShortRangeMissile extends WeaponTargetUnit
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int MAX_RANGE = 600;
	public static final int DAMAGE = 20;
	public static final int RADIUS = 125;
	public static final float ACCURACY = .40f;
	public static final int USE_TIME = 105;
	public static final int COOLDOWN = 60;
	
	public static final WeaponType WEAPON_TYPE = WeaponType.EXPLOSIVE;
	public static final DamageType DAMAGE_TYPE = DamageType.EXPLOSIVE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.LIGHT;
	
	public ShortRangeMissile(Unit owner) 
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
	public int getRadius() 							{	return RADIUS;							}

	protected void playAudio()
	{
		Sounds.missileFire.play(getOwner().getPosition(), Utility.random(1.1, 1.25f));
	}
	
	protected void animation(Unit a, boolean isHit) 
	{

	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		Game.addMissile(new Missile(getOwner(), target, isHit, MAX_RANGE, getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE, RADIUS));
	}

}
