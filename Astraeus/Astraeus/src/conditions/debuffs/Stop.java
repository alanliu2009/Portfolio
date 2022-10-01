package conditions.debuffs;

import conditions.Debuff;

public class Stop extends Debuff
{				
	public Stop(int duration, int delay)
	{
		super(duration, delay);
		
		locksPosition = true;
	}

}
