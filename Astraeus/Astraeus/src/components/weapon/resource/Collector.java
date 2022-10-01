package components.weapon.resource;

import java.util.ArrayList;

import animations.AnimationManager;
import animations.circles.AnimCircleGrow;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import engine.states.Game;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import ui.display.DisplayManager;

public class Collector extends Weapon
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 400;
	public static final int DAMAGE = 0;
	public static final int USE_TIME = 5;
	public static final int COOLDOWN = 0;
	public static final int CAPACITY = 4;

	public static final float PULL_ACC = 3000f;
	public static final int PICKUP_RADIUS = 75;

	public static final WeaponType WEAPON_TYPE = WeaponType.RESOURCE;
	public static final DamageType DAMAGE_TYPE = DamageType.NONE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.HEAVY;

	private int consecutiveFramesActivated = 0;
	private int lastActivation;
	
	public Collector(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}
	
	public int getMaxRange() 						{	return (int) (MAX_RANGE);	}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}

	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	
	protected void playAudio()
	{
		//AudioManager.playLaser(owner.getPosition(), 1.5f);
	}
	
	protected void animation() 
	{
		if(lastActivation == Game.getTime() - 5)
		{
			consecutiveFramesActivated++;
		}
		else
		{
			consecutiveFramesActivated = 0;
		}
		
		lastActivation = Game.getTime();
		
		int diameter = (int) (getMaxRange());
//		
//		if(consecutiveFramesActivated < buildupTime)
//		{
//			float scale = (float) consecutiveFramesActivated / (float) buildupTime;
//			diameter = (int) (diameter * scale);
//		}
//		
		if(consecutiveFramesActivated % 3 == 0)
		{
			AnimationManager.add(new AnimCircleGrow(getOwner(), diameter, 100));
		}
	}
	
	protected void activation() 
	{
		ArrayList<Resource> resources = ResourceManager.getResources();
		
		for(Resource r : resources)
		{
			float d = getOwner().getDistance(r.getPosition());
			
			if(d < getMaxRange()  && getOwner().hasCapacity())
			{
				r.pull(getOwner(), (float) (PULL_ACC / Math.pow(d, 2)));

				//r.pull(getOwner(), (float) (PULL_ACC / Math.pow(d, 2)));
			}
			
			if(d < PICKUP_RADIUS && getOwner().hasCapacity() && !r.wasPickedUp())
			{
				if(r.getGameNumber() != Game.getGameNumber())
				{
					DisplayManager.addMessage("Error: " + this + " is trying to target an entity from a previous game.", 800);
					DisplayManager.addMessage("If you have copied the resources array, make sure to clear it every game.", 800);
				}
				
	
				getOwner().collect(r);
			}
		}
		

	}

	public void onAddition()
	{
		super.onAddition();
		getOwner().increaseCapacity(CAPACITY);
	}


}
