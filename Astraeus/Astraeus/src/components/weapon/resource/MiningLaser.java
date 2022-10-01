package components.weapon.resource;

import animations.AnimationManager;
import animations.beams.AnimBeamConstant;
import animations.effects.Smoke;
import animations.effects.Sparks;
import components.DamageType;
import components.MovementPenalty;
import components.weapon.WeaponTargetEntity;
import components.weapon.WeaponType;
import conditions.instant.Damage;
import engine.Settings;
import engine.Utility;
import engine.states.Game;
import objects.entity.Entity;
import objects.entity.node.Asteroid;
import objects.entity.node.Derelict;
import objects.entity.node.Node;
import objects.entity.unit.Unit;
import ui.display.DisplayManager;
import ui.sound.Sounds;

public class MiningLaser extends WeaponTargetEntity
{
	public static final int SIZE = 2;
	public static final int MINERAL_COST = 2;
	public static final int MAX_RANGE = 250;
	public static final int MIN_RANGE = 50;

	public static final float DAMAGE = 1f;
	public static final int USE_TIME = 1;
	public static final int COOLDOWN = 3;
	public static final WeaponType WEAPON_TYPE = WeaponType.RESOURCE;
	public static final DamageType DAMAGE_TYPE = DamageType.TRUE;
	public static final MovementPenalty MOVEMENT_PENALTY = MovementPenalty.MEDIUM;
	public static final float ACCURACY = 1.00f;

	public static final int ANIM_BEAM_WIDTH = 4;
	public static final int ANIM_BEAM_DURATION = USE_TIME+COOLDOWN;
	
	private float animTargetXOffset;
	private float animTargetYOffset;
	private boolean resetOffset;
	
	public MiningLaser(Unit owner) 
	{
		super(owner, SIZE, MINERAL_COST);
	}

	public float getDamage() 						{	return DAMAGE;							}
	public int getMinRange() 						{	return (int) (MIN_RANGE);				}
	public int getMaxRange() 						{	return (int) (MAX_RANGE);				}
	public int getCooldown() 						{	return (int) (COOLDOWN * getCooldownMultiplier());		}
	public int getUseTime()							{	return (int) (USE_TIME * getUseMultiplier());			}
	public WeaponType getWeaponType() 				{	return WEAPON_TYPE;						}
	public DamageType getDamageType()				{	return DAMAGE_TYPE;						}
	public MovementPenalty getMovementPenalty()		{	return MOVEMENT_PENALTY;				}
	public float getAccuracy()						{	return (int) (ACCURACY * getAccuracyMultiplier());	}

	public void update()
	{
		super.update();
		
		if(Game.getTime() % Utility.random(80, 120) == 0)
		{
			resetOffset = true;
		}
		else
		{
			resetOffset = false;
		}
		
		animTargetXOffset += Utility.random(-1, 1);
		animTargetYOffset += Utility.random(-1, 1);
	}
	
	protected void playAudio()
	{
		if(Math.random() < .01)
		{
			Sounds.mining.play(getOwner().getPosition(), .6f, .8f);

		}
	}
	
	protected void animation(Entity target) 
	{
		if(resetOffset)
		{
			setOffset(target);
		}
		
		if(Settings.showAnimations && Math.random() < .1)
		{
			if(target instanceof Asteroid)
			{
				AnimationManager.add(new Smoke(target.getCenterX() + animTargetXOffset, target.getCenterY() + animTargetYOffset, 24));
			}
			else if(target instanceof Derelict)
			{
				for(int i = 0; i < 3; i++)
				{
					AnimationManager.add(new Sparks(target.getCenterX() + animTargetXOffset, target.getCenterY() + animTargetYOffset, 12, 25));
				}

			}
		}
		
		if(target instanceof Node)
		{
			AnimationManager.add(new AnimBeamConstant(getOwner(), target, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION, animTargetXOffset, animTargetYOffset));		
		}
		else if(target instanceof Unit)
		{
			AnimationManager.add(new AnimBeamConstant(getOwner(), target, ANIM_BEAM_WIDTH, ANIM_BEAM_DURATION, animTargetXOffset/4, animTargetYOffset/4));		
		}
		
	}
	
	protected void activation(Entity target) 
	{
		if(target.getGameNumber() != Game.getGameNumber())
		{
			DisplayManager.addMessage("Error: " + this + " is trying to target an entity from a previous game.", 800);
			DisplayManager.addMessage("If you have copied the nodes array, make sure to clear it every game.", 800);
		}
		if(target instanceof Unit)
		{
			if(((Unit) target).rollToHit(getAccuracy()))
			{
				target.addCondition(new Damage(getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE));
			}
		}
		else
		{
			target.addCondition(new Damage(getModifiedDamage(DAMAGE, DAMAGE_TYPE), DAMAGE_TYPE));
		}
	}
	
	
	protected void setOffset(Entity target)
	{
		float variance = target.getSize()/4;		
		animTargetXOffset = Utility.random(-variance,  variance);
		animTargetYOffset = Utility.random(-variance,  variance);
	}

	

	


}
