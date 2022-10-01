package conditions.instant;

import components.DamageType;
import conditions.Instant;

public class Damage extends Instant
{	
	private float amount;
	private DamageType type;

	public Damage(float amount, DamageType type)
	{
		super(1);
		this.amount = amount;
		this.type = type;
	}
	
	public Damage(float amount, DamageType type, int delay)
	{
		super(delay);
		this.amount = amount;
		this.type = type;
	}
	
	public void begin()
	{	
		getOwner().takeDamage(amount, type);
	}
			

}
