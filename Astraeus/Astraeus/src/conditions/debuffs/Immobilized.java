package conditions.debuffs;

import conditions.Debuff;

public class Immobilized extends Debuff
{	
	public static final int DURATION = 120;
	
	public Immobilized(int duration, int delay)
	{
		super(duration, delay);
		
		stopsMovement = true;
	}
			

}
