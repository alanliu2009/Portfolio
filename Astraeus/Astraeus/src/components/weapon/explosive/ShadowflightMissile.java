package components.weapon.explosive;

import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.Pulse;
import engine.Utility;
import engine.states.Game;
import objects.entity.missile.MissileShadowflight;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class ShadowflightMissile extends WeaponTargetUnit
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int MAX_RANGE = 1200;
	public static final int DAMAGE = 12;
	public static final int RADIUS = 25;
	public static final float ACCURACY = .60f;
	public static final int USE_TIME = 105;
	public static final int COOLDOWN = 60;
	public static final int DURATION = 180;
	
	public static final WeaponType WEAPON_TYPE = WeaponType.EXPLOSIVE;
	public static final DamageType DAMAGE_TYPE = DamageType.EXPLOSIVE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.LIGHT;
	
	public ShadowflightMissile(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;							}
	public int getMinRange() 						{	return super.getMinRange() * 2;			}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();	}
	public int getRadius() 							{	return RADIUS;							}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Pulse.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Pulse.class).getTimeLeft();}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return Pulse.class;					}

	protected void playAudio()
	{
		Sounds.missileFire.play(getOwner().getPosition(), Utility.random(1.1, 1.25f));
	}
	
	protected void animation(Unit a, boolean isHit) 
	{

	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		Game.addMissile(new MissileShadowflight(getOwner(), target, isHit, MAX_RANGE, getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE, RADIUS));
	}
	
	

}
