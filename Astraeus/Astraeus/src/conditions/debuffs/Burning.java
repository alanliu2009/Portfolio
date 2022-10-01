package conditions.debuffs;

import animations.AnimationManager;
import animations.effects.Flames;
import components.DamageType;
import conditions.Debuff;
import engine.Utility;

public class Burning extends Debuff
{	
	float damagePercentOverDuration;
	
	public Burning(int duration, float damagePercentOverDuration)
	{
		super(duration, 0);
		this.damagePercentOverDuration = damagePercentOverDuration;
		preventsRepair = true;
	}
	
	public Burning(int duration, float damagePercentOverDuration, int delay)
	{
		super(duration, delay);
		this.damagePercentOverDuration = damagePercentOverDuration;
		preventsRepair = true;

	}

	public void updateFrame()
	{
		float size = getOwner().getSize();
		AnimationManager.add(new Flames(getOwner().getCenterX() + Utility.random(-size/6, size/6), getOwner().getCenterY() + Utility.random(-size/6, size/6), Utility.random(8, 15)));
	}
	
	public void updateTick()
	{
		float durationInTicks = (float) getDuration() / 60.0f;
		float damagePerTick = damagePercentOverDuration / durationInTicks;
		float actualDamagePerTick = getOwner().getMaxStructure() * damagePerTick;
	//	System.out.println(Game.getTime() + ": " + getOwner() + " burned for + " + actualDamagePerTick);
		getOwner().takeDamage(actualDamagePerTick, DamageType.STRUCTURE_ONLY);
	}

}
