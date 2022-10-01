package conditions.buffs;

import conditions.Buff;

public class Invulnerable extends Buff
{	

	public Invulnerable(int duration)
	{
		super(duration);
		preventsDamage = true;
	}
	
	public Invulnerable(int duration, int delay)
	{
		super(duration, delay);
		preventsDamage = true;
	}
	
}
