package entities.projectiles.nonphysical;

import entities.core.Coordinate;
import entities.living.Living;
import entities.projectiles.Projectile;

public class BlockBomb extends Projectile {
	private static float baseSpeed = 50f;
	private static int baseRadius = 4;
	
	private int radius;
	
	public BlockBomb(Living origin, Coordinate target, float scaling, int id) {
		super(origin);
		
		this.width = 0.5f;
		this.height = 0.5f;
		
		this.sprite = game.displayManager.getBlockSprite(id);
		
		this.radius = (int) (baseRadius * scaling);
		
		double theta = Math.atan2(target.getY() - origin.getPosition().getY(), target.getX() - origin.getPosition().getX());
		this.xSpeed = (float) Math.cos(theta) * baseSpeed;
		this.ySpeed = (float) Math.sin(theta) * baseSpeed;
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
	
	@Override
	protected void entityCollisions() {}
	
	protected void onBlockCollision() {
		// Blow up blocks
		int centerX = (int) position.getX();
		int centerY = (int) position.getY();
		
		for(int i = -radius; i < radius; i++)
		{
			for(int j = -radius; j < radius; j++)
			{
				game.getWorld().destroyBlock(centerX + i, centerY + j);
			}
		}
		
		this.remove = true;
	}
}