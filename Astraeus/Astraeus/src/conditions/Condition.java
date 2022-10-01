package conditions;

import objects.entity.Entity;

public abstract class Condition 
{
	// Data
	
	private final int TICK_FREQUENCY = 60;
	
	protected Entity owner;
	
	private int prepLeft;
	private int timeLeft;
	private int duration;
	
	protected float mobilityScalar = 1;
	protected float damageScalar = 1;
	protected float damageTakenScalar = 1;

	protected boolean stopsMovement = false;
	protected boolean stopsAction = false;
	protected boolean locksPosition = false;
	protected boolean preventsDamage = false;
	protected boolean preventsShieldRecovery = false;
	protected boolean preventsRepair = false;

	// Accessors
	public boolean stopsAction()						{	return stopsAction;					}
	public boolean stopsMovement()						{	return stopsMovement;				}
	public boolean locksPosition()						{	return locksPosition;				}
	public boolean preventsDamage()						{	return preventsDamage;				}
	public boolean preventsShieldRecovery()				{	return preventsShieldRecovery;		}
	public boolean preventsRepair()						{	return preventsRepair;		}

	public boolean scalesSpeed()						{	return mobilityScalar != 1;			}
	public boolean scalesDamage()						{	return damageScalar != 1;			}
	public float getSpeedModifier()						{	return mobilityScalar;				}
	public float getDamageDealtModifier()				{	return damageScalar;				}
	public float getDamageTakenModifier()				{	return damageTakenScalar;				}

	public int getDuration()							{	return duration;					}
	public int getTimeLeft()							{	return timeLeft;					}

	protected Condition(int duration)
	{
		this.duration = duration;
		timeLeft = duration;
		prepLeft = 0;
	}
	
	protected Condition(int duration, int delay)
	{
		this.duration = duration;
		timeLeft = duration;
		prepLeft = delay;
	}
	
	public void extendDuration(int amount)
	{
		timeLeft = timeLeft + amount;
		duration = duration + amount;
	}
	
	public void setDuration(int amount)
	{
		timeLeft = amount;
		duration = amount;
	}
	
	public void prepare()
	{
		if(prepLeft > 0)
		{
			prepLeft--;
		}
		
		if(prepLeft == 0)
		{
			begin();
		}
	}
	
	public void update()									
	{
		updateFrame();
		
		if(timeLeft % TICK_FREQUENCY == 0)
		{
			updateTick();
		}
		
		if(timeLeft > 0)
		{
			timeLeft--;
		}

	}
	
	public void setEntity(Entity e)
	{
		this.owner = e;
	}
		
	public Entity getOwner()
	{
		return owner;
	}
	
	public boolean isActive() 
	{
		return timeLeft > 0;
	}

	public boolean isPreparing() 
	{
		return prepLeft > 0;
	}
	
	public String toString()
	{
		if(timeLeft > 99999)
		{
			return getClass().getSimpleName();
		}
		else
		{
			return getClass().getSimpleName() + " (" + timeLeft + ")";
		}
	}
	
	public void updateFrame()
	{
		
	}
	
	public void updateTick()
	{
		
	}
	
	public void begin()
	{

	}
	
	public void end()
	{
		
	}
	



}
