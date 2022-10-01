package conditions.debuffs;

import conditions.Debuff;

public class Slow extends Debuff
{	
	
	public Slow(int duration, float scalar)
	{
		super(duration);
		mobilityScalar = scalar;
	}
	
	public Slow(int duration, float scalar, int delay)
	{
		super(duration, delay);
		mobilityScalar = scalar;
	}
	
}
