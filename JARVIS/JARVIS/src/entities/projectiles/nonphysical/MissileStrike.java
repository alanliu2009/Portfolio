package entities.projectiles.nonphysical;

import org.lwjgl.Sys;

import entities.core.Coordinate;
import entities.living.Living;
import entities.projectiles.Projectile;
import entities.projectiles.physical.Bomb;
import managers.ImageManager;

public class MissileStrike extends Projectile {
	private static float baseSpeed = 15f;
	private static float StrikeCooldown = 1f;
	private static int radius = 5;
	
	private float lastShot;
	
	public MissileStrike(Living origin, Coordinate target) {
		super(origin);
		
		this.sprite = ImageManager.getImage("missile");
		
		this.damage = 20;
		this.width = 2.5f;
		this.height = 1f;
		
		
		this.angle = (float) Math.atan2(target.getY() - origin.getPosition().getY(), target.getX() - origin.getPosition().getX());
		this.xSpeed = (float) Math.cos(angle) * baseSpeed;
		this.ySpeed = (float) Math.sin(angle) * baseSpeed;
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
		
		this.lastShot = Sys.getTime();
	}
	
	public void projectileAI() {
		if(Sys.getTime() - lastShot > StrikeCooldown * 1000) {
			new Bomb(this);
			lastShot = Sys.getTime();
		}
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