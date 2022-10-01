package objects.entity.missile;

import components.DamageType;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class MissileBig extends Missile
{

	public MissileBig(Unit owner, Entity target, boolean locked, int range, float damage, DamageType damageType, int radius) 
	{
		super(owner, target, locked, range, damage, damageType, radius);
		image = image.getScaledCopy(1.5f);
		imageSecondary = imageSecondary.getScaledCopy(1.5f);
		imageMove = imageMove.getScaledCopy(1.5f);
		smokeSize = 16;

	}
		
}
