package conditions.instant;

import conditions.Instant;

public class RestoreShield extends Instant
{	
	private float amount;

	public RestoreShield(float amount)
	{
		super(1);
		this.amount = amount;
	}
	
	public RestoreShield(float amount, int delay)
	{
		super(delay);
		this.amount = amount;
	}
	
	public void begin()
	{	
		getOwner().regainShield(amount);
	}
			

}
