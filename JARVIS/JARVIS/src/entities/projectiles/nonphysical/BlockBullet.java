package entities.projectiles.nonphysical;

import java.util.HashMap;

import entities.core.Coordinate;
import entities.living.Living;
import entities.projectiles.Projectile;

public class BlockBullet extends Projectile {
	private static float baseDamage = 20f;
	private static float baseSpeed = 50f;
	
	public BlockBullet(Living origin, Coordinate target, float scaling, int id) {
		super(origin);
		
		this.width = 0.5f;
		this.height = 0.5f;
		
		this.damage = baseDamage * scaling;
		this.sprite = game.displayManager.getBlockSprite(id);
		
		double theta = Math.atan2(target.getY() - origin.getPosition().getY(), target.getX() - origin.getPosition().getX());
		this.xSpeed = (float) Math.cos(theta) * baseSpeed;
		this.ySpeed = (float) Math.sin(theta) * baseSpeed;
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
}