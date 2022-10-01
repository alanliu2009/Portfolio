package conditions.debuffs;

import animations.AnimationManager;
import animations.effects.YellowSparks;
import conditions.Debuff;
import engine.Utility;

public class Stun extends Debuff
{	
	public static final int DURATION = 120;
	
	public Stun(int duration, int delay)
	{
		super(duration, delay);
		
		stopsAction = true;
		stopsMovement = true;
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
	
		AnimationManager.add(new YellowSparks(x, y,  Utility.random(1, 5),  duration));
	}	

}
