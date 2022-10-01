package conditions.debuffs;

import animations.AnimationManager;
import animations.effects.Sparks;
import conditions.Debuff;
import engine.Utility;

public class Pulse extends Debuff
{	
	public Pulse(int duration)
	{
		super(duration);
		
		preventsShieldRecovery = true;
	}
	
	public Pulse(int duration, int delay)
	{
		super(duration, delay);
		
		preventsShieldRecovery = true;
	}
	
	public void updateFrame()
	{
		float offset = getOwner().getSize() / 3;
		int duration = 5;
		
		float x = 0;
		float y = 0;
		
		while(Utility.distance(getOwner().getCenterX(), getOwner().getCenterY(), x, y) > offset)
		{
			x = getOwner().getCenterX() + Utility.random(-offset, offset);
			y = getOwner().getCenterY() + Utility.random(-offset, offset);
		}
	
		AnimationManager.add(new Sparks(x, y,  Utility.random(1, 5),  duration));
	}	


}
