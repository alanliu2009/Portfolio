package objects.entity.missile;

import org.newdawn.slick.Color;

import animations.AnimationManager;
import animations.effects.BoomDark;
import components.DamageType;
import components.weapon.explosive.ShadowflightMissile;
import conditions.debuffs.Pulse;
import objects.entity.Entity;
import objects.entity.unit.Unit;

public class MissileShadowflight extends Missile
{
	
	
	public MissileShadowflight(Unit owner, Entity target, boolean locked, int range, float damage, DamageType damageType, int radius) {
		super(owner, target, locked, range, damage, damageType, radius);
		smokeSize = 8;
	}

	public void addExplosionEffect()
	{
		AnimationManager.add(new BoomDark(getCenterX(), getCenterY(), radius * EXPLOSION_IMAGE_SCALING * getScale()));
	}
			
	public void directHit(Unit u, float damage, DamageType damageType)
	{
		super.directHit(u, damage, damageType);

		if(u.hasCondition(Pulse.class))
		{
			if(u.getCondition(Pulse.class).getDuration() < ShadowflightMissile.DURATION)
			{
				u.getCondition(Pulse.class).setDuration(ShadowflightMissile.DURATION);
			}
		}
		else
		{
			u.addCondition(new Pulse(ShadowflightMissile.DURATION));
		}
		
	}
	
	public Color getColorSecondary()
	{
		return new Color(200, 0, 255);
	}
	
}
