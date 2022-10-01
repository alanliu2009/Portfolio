package conditions;

abstract public class Buff extends Condition
{
	protected Buff(int duration, int delay)
	{
		super(duration, delay);
	}
	
	protected Buff(int duration)
	{
		super(duration, 0);
	}

}
