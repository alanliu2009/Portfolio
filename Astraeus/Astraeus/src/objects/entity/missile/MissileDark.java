package objects.entity.missile;

import animations.AnimationManager;
import animations.effects.BoomDark;
import components.DamageType;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class MissileDark extends Missile
{
	public MissileDark(Unit owner, Entity target, boolean locked, int range, float damage, DamageType damageType, int radius) {
		super(owner, target, locked, range, damage, damageType, radius);
	}

	public void addExplosionEffect()
	{
		AnimationManager.add(new BoomDark(getCenterX(), getCenterY(), radius * EXPLOSION_IMAGE_SCALING * getScale()));
	}
	
}
