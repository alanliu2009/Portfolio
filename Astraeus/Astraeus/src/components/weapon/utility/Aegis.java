package components.weapon.utility;

import components.DamageType;
import components.MovementPenalty;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import conditions.buffs.Fortified;
import objects.entity.unit.Unit;

public class Aegis extends Weapon
{
	public static final int SIZE = 1;
	public static final int MINERAL_COST = 1;
	public static final int DURATION = 1;
	public static final int USE_TIME = 1;
	public static final int COOLDOWN = 0;
	public static final float DAMAGE_SCALING = .55f;

	public static final WeaponType WEAPON_TYPE = WeaponType.UTILITY;
	public static final DamageType DAMAGE_TYPE = DamageType.NONE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;
	
	public Aegis(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public boolean appliesCondition()				{ 	return true;					}
	public boolean hasAppliedCondition(Unit u)		{	return u.getConditions().containsType(Fortified.class);	}
	public int getAppliedConditionTimeLeft(Unit u)	{	return u.getConditions().get(Fortified.class).getTimeLeft();}
	public int getDuration()						{ 	return DURATION;	}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	
	protected void playAudio()
	{
		//Sounds.aegis.play(getOwner().getPosition());
		//AudioManager.playLaser(owner.getPosition(), 1.5f);
	}
	
	protected void animation() 
	{
		
	}
	
	protected void activation() 
	{
		getOwner().addCondition(new Fortified(DAMAGE_SCALING, DURATION));
	}
	


}
