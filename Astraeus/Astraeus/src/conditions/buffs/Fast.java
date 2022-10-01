package conditions.buffs;

import conditions.Buff;
import engine.Utility;

public class Fast extends Buff
{	
	float startingScalar;
	
	public Fast(float scalar, int duration)
	{
		super(duration);
		startingScalar = scalar;
		mobilityScalar = scalar;
	}
	
	public void update()									
	{
		super.update();
		if(isActive())
		{			
			float percentComplete = (float) getTimeLeft() / (float) getDuration();
			mobilityScalar = Utility.scaleBetweenBounded(percentComplete, 1, startingScalar, 0, 1);
		}
	}
}
