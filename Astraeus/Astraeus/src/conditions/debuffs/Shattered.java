package conditions.debuffs;

import animations.AnimationManager;
import animations.effects.ShatteredIcon;
import conditions.Debuff;

public class Shattered extends Debuff
{	
	public Shattered(float damageTakenScalar, int duration, int delay)
	{
		super(duration, delay);
		
		this.damageTakenScalar = damageTakenScalar;
	}
	
	public void updateFrame()
	{
		AnimationManager.add(new ShatteredIcon(getOwner()));
	}
			

}
