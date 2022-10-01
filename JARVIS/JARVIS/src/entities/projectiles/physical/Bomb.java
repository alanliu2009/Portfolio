package entities.projectiles.physical;

import entities.core.Entity;
import managers.ImageManager;

public class Bomb extends PhysicalProjectile {
	private static int radius = 5;
	
	public Bomb(Entity origin) {
		super(origin);
		
		this.width = 1f;
		this.height = 1f;
		
		this.xSpeed = 0f;
		this.ySpeed = 0f;
		
		this.sprite = ImageManager.getImage("bomb");
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
	
	@Override
	protected void onBlockCollision() {
		super.onBlockCollision();
		
		// Blow up blocks
		int centerX = (int) getX();
		int centerY = (int) getY();
		
		for(int i = -radius; i < radius; i++)
		{
			for(int j = -radius; j < radius; j++)
			{
				game.getWorld().destroyBlock(centerX + i, centerY + j);
			}
		}
	}
	
}