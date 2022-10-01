package components.weapon;


import components.Component;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.resource.MiningLaser;
import conditions.Condition;
import conditions.system.UseHeavy;
import conditions.system.UseLight;
import conditions.system.UseMedium;
import conditions.system.UseStop;
import objects.entity.Entity;
import objects.entity.unit.Unit;
import player.Boon;

public abstract class Weapon extends Component 
{
	/*************** Data ***************/

	protected int useCooldownTimer;
	protected int useLockTimer;
	protected boolean using;
	private float weaponMaxRangeMultiplier;
	private float weaponCooldownMultiplier;
	private float weaponUseMultiplier;
	
	/*************** Constructor ***************/
	
	public Weapon(Unit owner, int size, int mineralCost) 
	{
		super(owner, size, mineralCost);
		
		weaponCooldownMultiplier = 1;
		weaponUseMultiplier = 1;
		weaponMaxRangeMultiplier = 1;
		
		if(owner.getPlayer().hasBoon(Boon.EXPERT) && getWeaponType() == WeaponType.UTILITY)
		{
			weaponCooldownMultiplier = Boon.EXPERT_USE_AND_COOLDOWN_MULTIPLIER;
			weaponUseMultiplier = Boon.EXPERT_USE_AND_COOLDOWN_MULTIPLIER;
		}
	}
	
	/*************** Information ***************/
	
	abstract public int getUseTime();
	abstract public int getCooldown();
	
	public int getCooldownTimer()									{ 	return useCooldownTimer;		}
	public int getUseTimer()										{	return useLockTimer;			}
	public boolean onCooldown()										{	return getCooldownTimer() != 0;	}
	public Class<? extends Condition> getAppliedConditionType()		{ 	return null;					}

	public boolean appliesCondition()				{ 	return false;					}
	public WeaponType getWeaponType()				{	return WeaponType.UTILITY;		}
	public DamageType getDamageType()				{	return DamageType.NONE;			}
	public MovementPenalty getMovementPenalty()		{	return MovementPenalty.NONE;	}
	public float getAccuracy()						{	return 0;						}
	public boolean hasAppliedCondition(Unit u)		{	return false;					}
	public int getAppliedConditionTimeLeft(Unit u)	{	return 0;						}
	
	protected final float getCooldownMultiplier()	{	return getOwner().getCooldownTimeMultiplier() * weaponCooldownMultiplier;	}
	protected final float getUseMultiplier()		{	return getOwner().getUseTimeMultiplier() * weaponUseMultiplier;				}
	protected final float getMaxRangeMultiplier()	{	return getOwner().getMaxRangeMultiplier() * weaponMaxRangeMultiplier;		}
	protected final float getAccuracyMultiplier()	{	return getOwner().getAccuracyMultiplier();									}

	public int getMaxRange()						{	return 0;		}
	public int getMinRange() 						{	return (int) (getMaxRange() * getOwner().getMinRangeMultiplier());					}
	public float getDamage() 						{	return 0;		}
	public int getNumShots()						{	return 1;		}
	public int getRadius() 							{	return 0;		}
	public int getDuration()						{	return 0;		}
	public boolean rotateUser()						{	return true;	}

	public boolean inRange(Entity e)
	{
		return getOwner().getDistance(e) >= getMinRange() && getOwner().getDistance(e) <= getMaxRange();
	}
	
	/*************** Every Frame ***************/
	
	public void update() 
	{	
		updateTimers();
		
		if(inUse() && canUse())
		{
			activation();
			animation();
			playAudio();
			end();
			useCooldownTimer = getCooldown();
		}
		
		// Cancel if interrupted by condition
		if(inUse() && !getOwner().canAct())
		{
			end();
		}
	}
	
	public void updateTimers()
	{
		if(getOwner().canAct() && useCooldownTimer > 0)
		{
			useCooldownTimer--;
		}
		if(getOwner().canAct() && useLockTimer > 0)
		{
			useLockTimer--;
		}
	}
	
	/*************** Use Methods ***************/
	
	public void use() 
	{		
		if (!inUse() && canUse())
		{
			start();
			useLockTimer = getUseTime();
		}
	}
	
	public void use(Entity e)
	{
		use();
	}
	
	public boolean canUse()
	{
		return getOwner() != null && getOwner().canAct() && useCooldownTimer == 0 && useLockTimer == 0;
	}
	
	public boolean canUse(Entity e)
	{
		return canUse();
	}
		
	public boolean inUse()
	{
		return using;
	}

	protected void start()
	{
		startConditions();
		using = true;
	}
	
	protected void end()
	{
		endConditions();
		using = false;
	}
	
	protected void startConditions() 
	{
		if(getMovementPenalty() == MovementPenalty.LIGHT)
		{
			getOwner().addCondition(new UseLight());
		}
		else if(getMovementPenalty() == MovementPenalty.MEDIUM)
		{
			getOwner().addCondition(new UseMedium());
		}
		else if(getMovementPenalty() == MovementPenalty.HEAVY)
		{
			getOwner().addCondition(new UseHeavy());
		}
		else if(getMovementPenalty() == MovementPenalty.STOP)
		{
			getOwner().addCondition(new UseStop());
		}
	}

	protected void endConditions() 
	{
		if(getMovementPenalty() == MovementPenalty.LIGHT)
		{
			getOwner().removeAllConditions(UseLight.class);
		}
		else if(getMovementPenalty() == MovementPenalty.MEDIUM)
		{
			getOwner().removeAllConditions(UseMedium.class);
		}
		else if(getMovementPenalty() == MovementPenalty.HEAVY)
		{
			getOwner().removeAllConditions(UseHeavy.class);
		}
		else if(getMovementPenalty() == MovementPenalty.STOP)
		{
			getOwner().removeAllConditions(UseStop.class);
		}
	}
	
	public int getUseProgress()
	{
		return getUseTime() - getUseTimer();
	}
	
	public int getCooldownProgress()
	{
		return getCooldown() - getCooldownTimer();
	}
	
	/*************** Effects ***************/

	abstract protected void activation();
	abstract protected void animation();

	protected void playAudio()
	{	
	 
	}
	
	public String toString()
	{
		return getClass().getSimpleName();
	}
	
	public float getModifiedDamage(float amount, DamageType damageType)
	{
		float bonusDamage = 0;
		
		
		if(this instanceof MiningLaser && getOwner().getPlayer().hasBoon(Boon.MINER) )
		{
			bonusDamage += amount * Boon.MINER_MINING_SPEED_MULTIPLIER;
		}
		
		if((damageType == DamageType.KINETIC) && getOwner().getPlayer().hasBoon(Boon.GUNNER))
		{
			bonusDamage += amount * Boon.OFFENSIVE_DAMAGE_BONUS;
		}
		else if((damageType == DamageType.ENERGY) && getOwner().getPlayer().hasBoon(Boon.LANCER))
		{
			bonusDamage += amount * Boon.OFFENSIVE_DAMAGE_BONUS;
		}
		else if((damageType == DamageType.EXPLOSIVE) && getOwner().getPlayer().hasBoon(Boon.BLASTER))
		{
			bonusDamage += amount * Boon.OFFENSIVE_DAMAGE_BONUS;
		}
		return amount + bonusDamage;
	}

	public void onAddition()
	{
		super.onAddition();
//		System.out.println("Base speed before " + this.getOwner().getMaxSpeed());
		getOwner().decreaseAccelerationByPercent(getMovementPenalty().getPassiveMovementPenalty());
		getOwner().decreaseSpeedByPercent(getMovementPenalty().getPassiveMovementPenalty());
//		System.out.println("Base speed after " + this.getOwner().getMaxSpeed());
	}
	
}
