package objects.entity;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import components.DamageType;
import conditions.Condition;
import conditions.ConditionSet;
import engine.Settings;
import engine.Values;
import engine.states.Game;
import objects.GameObject;
import objects.entity.unit.Unit;
import player.Boon;
import player.Player;

public class Entity extends GameObject
{
	public static final int SHIELD_RECOVERY_TIME_BASE = 180;

	protected int team = 0;
	// Protected Data
	private float curStructure = 0;
	private float maxStructure = 0;
	private float curPlating = 0;
	private float maxPlating = 0;
	private float curShield = 0;
	private float maxShield = 0;
	private float shieldRegen = 0;
	private float hullRepair = 0;
	private float hullRepairEfficiency = 1;
	private int shieldRecoveryTime = SHIELD_RECOVERY_TIME_BASE;
	private int shieldRecoveryTimer = 0;
	private int framesSinceLastHit = Integer.MAX_VALUE;
	private boolean alive = true;
	private boolean wasHit = false;
	private Player player;
	
//	private boolean hasNotActed = true;

	private boolean selected = false;
	
	private float damageTakenMultiplier = 1f;
	
	private ConditionSet conditions;
	
	public Entity(float x, float y) 
	{
		super(x, y);
		this.team = Values.NEUTRAL_ID;

		conditions = new ConditionSet(this);
	}
	
	public Entity(float x, float y, int team) 
	{
		super(x, y);
		this.team = team;
		conditions = new ConditionSet(this);
	}
	
	/**************** Accessors ******************/

	public boolean canMove() 				{	return super.canMove() && !conditions.stopsMovement() && !conditions.locksPosition();	}
	public final boolean canAct() 				{	return !conditions.stopsAction();						}
//	public float getAverageSize()			{	return image != null ? (image.getWidth()  + image.getHeight()) / 2 : -1;}

	public final boolean isSelected()				{	return selected;		}
	
	public final ConditionSet getConditions()		{	return conditions;	}
	public final float getCurEffectiveHealth()		{	return getCurStructure() + getCurShield() + getCurPlating(); }
	public final float getMaxEffectiveHealth()		{	return getMaxStructure() + getMaxShield() + getMaxPlating(); }
	public final float getPercentEffectiveHealth()	{	return getCurEffectiveHealth() / getMaxEffectiveHealth(); }

	public final boolean isInvulnerable()			{	return conditions.preventsDamage();	}

	// Plating
	public final boolean hasPlating() 				{	return maxPlating > 0;				}
	public final float getCurPlating()				{	return curPlating;					}
	public final float getMaxPlating() 				{	return maxPlating;					}
	public final boolean hasMaxPlating()			{	return curPlating == maxPlating;	}
	public final float getPercentPlating() 			{	return curPlating / maxPlating;		}

	// Shield	
	public final boolean hasShield() 				{	return maxShield > 0;				}
	public final float getCurShield() 				{	return curShield;					}
	public final float getMaxShield() 				{	return maxShield;					}
	public final boolean hasMaxShield()				{	return curShield == maxShield;		}
	public final int getShieldRecoveryTimeLeft()	{	return shieldRecoveryTimer;			}
	public final float getPercentShield() 			{	return curShield / maxShield;		}
	public final float getShieldRegen() 			{	return shieldRegen;					}
	public final boolean isShieldDamaged()			{	return curShield != maxShield;		}
	public final boolean isShieldRegenerating()		{	return shieldRecoveryTimer == 0;	}
	public final boolean canShieldRecover()			{	return !conditions.preventsShieldRecovery();	}
	public final boolean canBeRepaired()			{	return !conditions.preventsRepair();	}
	
	// Structure
	public final float getCurStructure() 			{	return curStructure;					}
	public final float getMaxStructure() 			{	return maxStructure;					}
	public final boolean hasMaxStructure()			{	return curStructure == maxStructure;	}
	public final float getPercentStructure() 		{	return curStructure / maxStructure;		}
	public final float getHullRepair()				{	return hullRepair;						}
	public final float getHullRepairEfficiency()	{	return hullRepairEfficiency;			}

	public final int getTeam() 						{	return team;						}
	public Player getPlayer() 						{	return player;						}

	public final Player getOpponent()				{	return Game.getOpponent(this);		}
	public final int getHitTimer() 					{	return framesSinceLastHit;			}
	public final int getTimeAlive()					{	return timer;						}
	public final boolean wasHit() 					{	return wasHit;						}

	// Life and Damage
	public final boolean isDead() 					{	return !isAlive();												}
	public final boolean isAlive() 					{	return alive;													}
	public final boolean isHullDamaged() 			{	return !isHullUndamaged();										}
	public final boolean isHullUndamaged() 			{	return hasMaxStructure() && hasMaxPlating();					}
	public final boolean isDamaged() 				{	return !isUndamaged();											}
	public final boolean isUndamaged()				{	return hasMaxStructure() && hasMaxShield() && hasMaxPlating();	}
	
	public final float getDamageTakenMultiplier()		{ return damageTakenMultiplier * conditions.getTotalDamageTakenModifier();			}

	
	public final boolean hasCondition(Class<? extends Condition> clazz)
	{
		return conditions.containsType(clazz);
	}
	
	
	public final Condition getCondition(Class<? extends Condition> clazz)
	{
		return hasCondition(clazz) ? conditions.get(clazz) : null;
		
	}
	
	
	public final boolean isShieldRecovered()		
	{ 
		return shieldRecoveryTimer == 0;	
	} 
	
	/**************** Mutators ******************/
	
//	public void actionComplete() 			{	hasNotActed = false;					}
	
	public void setPlayer(Player p)			{	player = p;								}
	public void addCondition(Condition c)
	{
		conditions.add(c);
	}
	
	public final void removeCondition(Condition c)
	{
		conditions.remove(c);
	}
	
	public final void removeAllConditions(Class<? extends Condition> clazz)
	{
		conditions.removeAll(clazz);
	}
	
	public void update()
	{
		super.update();
		
		if(framesSinceLastHit < Integer.MAX_VALUE)
		{
			framesSinceLastHit++;
		}
	
		// If my shields aren't blocked, begin to recover
		if(shieldRecoveryTimer > 0 && canShieldRecover())
		{
			shieldRecoveryTimer--;
		}
		
		conditions.update();
		
		if(timer % 60 == 0)
		{
			regainShield(shieldRegen);
			repairHull(hullRepair);
		}
		
		//hasNotActed = true;
				
		updateSpeed();
		
		if(conditions.locksPosition())
		{
			xSpeed = 0;
			ySpeed = 0;
		}
		
//		if(conditions.stopsAction())
//		{
//			hasNotActed = false;
//		}
//		
		if(conditions.stopsMovement())
		{
			moveComplete();
		}
	
	}
	
	protected void updateSpeed()
	{
		if(getConditions().modifiesSpeed())
		{
			setSpeedCurrent(getConditions().getModifiedSpeed(getMaxSpeedBase()));
			setAccelerationCurrent(getConditions().getModifiedSpeed(getAccelerationBase()));
		}
	}
	
	public void render(Graphics g)
	{	
		super.render(g);	
	}
	
	public final void select()
	{
		selected = true;
	}

	public final void unselect()
	{
		selected = false;
	}
	
	public final void setStructure(int amount)
	{
		int actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.ENGINEER))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
				
		maxStructure = actualAmount;
		curStructure = actualAmount;
	}
	
	public final void increaseMaxStructure(int amount)
	{
		int actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.ENGINEER))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
	
		maxStructure += actualAmount;
		curStructure += actualAmount;
	}

	public void increaseHullRepair(float amount)
	{	
		hullRepair += amount;
	}

	public void increaseHullRepairEfficiency(float amount)
	{
		float actualAmount = amount;
		
		if(getPlayer() != null)
		{
			if(getPlayer().hasBoon(Boon.ENGINEER))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
	
		hullRepairEfficiency += actualAmount;
	}
	
	
	
	public final void setShield(int amount)
	{
		int actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.GUARDIAN))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
		
		maxShield = actualAmount;
		curShield = actualAmount;
	}
	
	public final void increaseMaxShield(int amount)
	{
		int actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.GUARDIAN))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
		maxShield += actualAmount;
		curShield += actualAmount;
	}
	
	
	public final void increaseShieldRegen(float amount)
	{
		float actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.GUARDIAN))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
		shieldRegen += actualAmount;
	}

	public final void setPlating(int amount)
	{
		int actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.ARMORER))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
		
		maxPlating = actualAmount;
		curPlating = actualAmount;
	}
	
	public final void increaseMaxPlating(int amount)
	{
		int actualAmount = amount;
		
		if(getPlayer() != null)
		{
			actualAmount = Math.round((amount * getPlayer().getDifficultyRating()));
			
			if(getPlayer().hasBoon(Boon.ARMORER))
			{
				actualAmount *= Boon.DEFENSE_MULTIPLIER; 
			}
		}
		
		maxPlating += actualAmount;
		curPlating += actualAmount;
	}
	
	public final void decreaseDamageTakenMultiplier(float amount)
	{
		damageTakenMultiplier -= amount;
	}
	
	public final void repairHull(float amount) 
	{
		if(amount == 0)
		{
			return;
		}
		
		// Cannot repair, abort (usually from burning)
		if(!this.canBeRepaired())
		{
			return;
		}
//		System.out.println("base" + amount);
		
		amount *= hullRepairEfficiency; 
		
//		System.out.println("actual " + amount);

		
		float starting = getCurStructure() + getCurPlating();
		
		if (curStructure + amount > maxStructure) 
		{
			float overflow = curStructure + amount - maxStructure;
			curStructure = maxStructure;
			curPlating = Math.min(maxPlating, curPlating + overflow);
		} 
		else 
		{
			curStructure += amount;
		}
		
		float ending = getCurStructure() + getCurPlating();
		float totalRecovery = starting - ending;
		
		if(this instanceof Unit)
		{
			((Unit) this).getPlayer().addRepairRecieved(totalRecovery);
			((Unit) this).getOpponent().addRepairRecieved(totalRecovery);
			Game.addTotalRepairRecieved(totalRecovery);
			Game.addTotalRepairRecieved(totalRecovery);
		}
		
	}

	public final void regainShield(float amount) 
	{
		// Cannot regain shield if recovery is disabled
		if(!canShieldRecover())
		{
			return;
		}
		float starting = getCurShield();
		
		curShield += amount;

		if (curShield > maxShield) 
		{
			curShield = maxShield;
		}
		
		float ending = getCurShield();
		float totalRecovery = starting - ending;
		
		if(this instanceof Unit)
		{
			((Unit) this).getPlayer().addShieldRecieved(totalRecovery);
			((Unit) this).getOpponent().addShieldRecieved(totalRecovery);
			Game.addTotalShieldRecieved(totalRecovery);
			Game.addTotalShieldRecieved(totalRecovery);
		}
		
	}
	
	
	
	public void onDamageTaken(float amount, DamageType type)
	{
		
	}

	public void takeDamage(float amount, DamageType type)
	{
		// Apply base damage resistance *and* effects from conditions
		// Pierce ignores beneficial damage reduction
		if(!type.isType(DamageType.PIERCE) || getDamageTakenMultiplier() > 1)
		{
			amount *= getDamageTakenMultiplier();
		}
		
		// Alert the player to the damage taken after block effects
		onDamageTaken(amount, type);
		
		framesSinceLastHit = 0;	
		
		float starting = getCurEffectiveHealth();
		shieldRecoveryTimer = shieldRecoveryTime;
		
		// Start with Shield Layer
		amount = loseShield(amount, type);

		if(amount > 0)
		{
			// Leftover to Plating Layer
			amount = losePlating(amount, type);

			if(amount > 0)
			{
				// Last to Structure Layer
				loseStructure(amount, type);
			}
		}
		
		float ending = getCurEffectiveHealth();
		float totalDamage = starting - ending;
		
		
		if(this instanceof Unit)
		{
			((Unit) this).getPlayer().addDamageTaken(totalDamage);
			((Unit) this).getOpponent().addDamageDealt(totalDamage);
			Game.addDamageTaken(totalDamage);
			Game.addDamageDealt(totalDamage);
		}
		
		if(Settings.dbgShowDamageOnHit)
		{
//			if(type.isEnergy())
//			{
//				System.out.println("Total damage " + totalDamage);
//			}
			dbgMessage(""+(int) totalDamage, 60, type.getColor());
		}

	}

	// Damage Structure
	public final void loseStructure(float amount, DamageType type) 
	{	
		// If it can't damage structure, end.
		if(type.cannotDamageStructure())
		{
			return;
		}
		
		if(type.isExplosive())
		{
			amount *= type.getDamageMultiplier();
		}
		
		
		curStructure = Math.max(curStructure - amount, 0);

		if (curStructure == 0) 
		{
			die();
		}		 
	}

	// Damage Shield and return the amount of extra damage
	public final float loseShield(float amount, DamageType type) 
	{
		

		
		// If it can't damage shields, end with all damage leftover
		if(type.cannotDamageShields())
		{
			return amount;
		}
		
		// Apply damage bonus against shields
		if(type.isEnergy())
		{
			amount *= type.getDamageMultiplier();
		}	
		

	
//		if(type.isEnergy())
//		{
//			System.out.println("-------");
//
//			System.out.println("Base damage " + amount);
//			System.out.println("My shields " + curShield);
//
//		}
		
		
		float breakthroughDamage = amount - curShield;

		// If we have enough shield
		if(breakthroughDamage <= 0)
		{
			curShield -= amount;
			return 0;
		}

		// Breakthrough damage
		else
		{
			curShield = 0;
			
			// Undo damage bonus against shields to remainder
			if(type.isEnergy())
			{
				breakthroughDamage /= type.getDamageMultiplier();
			}
			
//			if(type.isEnergy())
//			{
//				System.out.println("Breakthrough damage " + breakthroughDamage);
//			}
//			
			return breakthroughDamage;
		}
	}


	// Damage Plating and return the amount of extra damage
	// Plating takes FULL damage now (0.9.2)
	public final float losePlating(float amount, DamageType type) 
	{
		// If it can't damage plating, end with all damage leftover
		if(type.cannotDamagePlating())
		{
			return amount;
		}
		
		// Apply damage bonus against shields
		if(type.isKinetic())
		{
			amount *= type.getDamageMultiplier();
		}
		
		
		float breakthroughDamage = amount - curPlating;

		// If we have enough plating
		if(breakthroughDamage <= 0)
		{
			curPlating -= amount;
			return 0;
		}

		// Breakthrough damage
		else
		{
			curPlating = 0;
			
			// Undo damage bonus against shields to remainder
			if(type.isKinetic())
			{
				breakthroughDamage /= type.getDamageMultiplier();
			}
			
			return breakthroughDamage;
		}
	}
	
	public void die()
	{
		image = null;
		alive = false;
		curStructure = 0;
		curPlating = 0;
		curShield = 0;
	}
	
	public static final Entity getNearestEntity(Point point, Class<? extends Entity> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Entity nearestEntity = null;
		ArrayList<Entity> entities = Game.getEntities();
		
	


		for(Entity e : entities)
		{
			if(clazz.isInstance(e) && e.getDistance(point) < nearestDistance)
			{
				nearestEntity = e;
				nearestDistance = e.getDistance(point);
			}
		}

		return nearestEntity;
	}
	
	
	
}
