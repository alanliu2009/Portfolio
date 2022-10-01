package conditions.instant;

import conditions.Instant;

public class Repair extends Instant
{	
	private float amount;

	public Repair(float amount)
	{
		super(1);
		this.amount = amount;
	}
	
	public Repair(float amount, int delay)
	{
		super(delay);
		this.amount = amount;
	}
	
	public void begin()
	{	
		getOwner().repairHull(amount);
	}
			

}
