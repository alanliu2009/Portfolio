package objects.entity.missile;

import animations.AnimationManager;
import animations.effects.Boom;
import components.DamageType;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class BlastCharge extends Missile {

	public BlastCharge(Unit owner, Entity target, boolean locked, int range, float damage, DamageType damageType,
			int radius) {
		super(owner, target, locked, range, damage, damageType, radius);
		// TODO Auto-generated constructor stub
	}
	
	public void addExplosionEffect()
	{
		AnimationManager.add(new Boom(getCenterX(), getCenterY(), 50 * getScale()));
	}

}
