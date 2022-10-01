package entities.projectiles.physical;

import entities.core.Coordinate;
import entities.core.Entity;
import entities.living.Living;
import entities.projectiles.Projectile;

public class PhysicalProjectile extends Projectile {
	
	public PhysicalProjectile(Entity origin) {
		super(origin);
	}
	
	@Override
	public void update() {
		drag();
		gravity();
		
		super.update();
	}
}