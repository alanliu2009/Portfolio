package conditions.buffs;

import conditions.Buff;

public class Fortified extends Buff
{	
	public Fortified(float damageTakenScalar, int duration)
	{
		super(duration);
		this.damageTakenScalar = damageTakenScalar;
	}
	
	public Fortified(float damageTakenScalar, int duration, int delay)
	{
		super(duration, delay);
		this.damageTakenScalar = damageTakenScalar;
	}
	
}
