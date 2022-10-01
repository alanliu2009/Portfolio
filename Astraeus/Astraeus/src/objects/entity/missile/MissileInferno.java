package objects.entity.missile;

import org.newdawn.slick.Color;

import animations.AnimationManager;
import animations.effects.BoomInferno;
import components.DamageType;
import conditions.debuffs.Burning;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class MissileInferno extends Missile
{
	int burnDuration;
	float burnDamageOverDuration;
	
	public MissileInferno(Unit owner, Entity target, boolean locked, int range, float damage, DamageType damageType, int radius, int burnDuration, float burnDamageOverDuration) 
	{
		super(owner, target, locked, range, damage, damageType, radius);
		this.burnDuration = burnDuration;
		this.burnDamageOverDuration = burnDamageOverDuration;
	}

	public void addExplosionEffect()
	{
		AnimationManager.add(new BoomInferno(getCenterX(), getCenterY(), radius * EXPLOSION_IMAGE_SCALING * getScale()));
	}
	
	public void directHit(Unit u, float damage, DamageType damageType)
	{
		super.directHit(u,  damage,  damageType);
		u.addCondition(new Burning(burnDuration, burnDamageOverDuration));
	}
	
	public Color getColorSecondary()
	{
		 return Color.red;
	}
}
