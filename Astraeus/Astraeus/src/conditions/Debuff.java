package conditions;

abstract public class Debuff extends Condition
{

	protected Debuff(int duration, int delay)
	{
		super(duration, delay);
	}
	
	protected Debuff(int duration)
	{
		super(duration, 1);
	}

}
