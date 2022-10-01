package components.weapon.explosive;

import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetUnit;
import components.weapon.WeaponType;
import conditions.Condition;
import conditions.debuffs.Burning;
import engine.Utility;
import engine.states.Game;
import objects.entity.missile.MissileInferno;
import objects.entity.unit.Unit;
import ui.sound.Sounds;

public class InfernoLauncher extends WeaponTargetUnit
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 600;
	public static final int DAMAGE = 25;
	public static final int RADIUS = 50;
	public static final float ACCURACY = .20f;
	public static final int USE_TIME = 105;
	public static final int COOLDOWN = 60;
	
	public static final WeaponType WEAPON_TYPE = WeaponType.EXPLOSIVE;
	public static final DamageType DAMAGE_TYPE = DamageType.EXPLOSIVE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;
	
	public static final int BURN_DURATION = 15*60;
	public static final float BURN_DAMAGE_OVER_DURATION = .08f;
	
	public InfernoLauncher(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public float getDamage() 						{	return DAMAGE;												}
	public int getMaxRange() 						{	return (int) (MAX_RANGE * getMaxRangeMultiplier());			}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());			}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());				}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;											}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;											}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;									}
	public float getAccuracy()						{	return ACCURACY * getAccuracyMultiplier();					}
	public int getRadius() 							{	return RADIUS;												}
	public int getDuration()						{ 	return BURN_DURATION;										}

	public boolean appliesCondition()				{ 	return true;												}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Burning.class);			}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Burning.class).getTimeLeft();	}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return Burning.class;					}

	protected void playAudio()
	{
		Sounds.missileFire.play(getOwner().getPosition(), Utility.random(.95, 1.1f));
	}
	
	protected void animation(Unit a, boolean isHit) 
	{

	}
	
	protected void activation(Unit target, boolean isHit) 
	{
		Game.addMissile(new MissileInferno(getOwner(), target, isHit, MAX_RANGE, getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE, RADIUS, BURN_DURATION, BURN_DAMAGE_OVER_DURATION));
	}

}
