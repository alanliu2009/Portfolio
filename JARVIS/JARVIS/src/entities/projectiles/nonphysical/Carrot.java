package entities.projectiles.nonphysical;

import entities.core.Coordinate;
import entities.living.Living;
import entities.projectiles.Projectile;
import managers.ImageManager;

public class Carrot extends Projectile {
	
	private float baseSpeed = 12f;
	private double theta;
	private Living origin;
	private Coordinate target;
	private int timer;
	
	public Carrot (Living origin, Coordinate target) {
		super(origin);
		this.origin = origin;
		this.target = target;
		
		this.damage = 20;
		this.width = 1.5f;
		this.height = 0.5f;
		
		
		theta = Math.atan2(target.getY() - origin.getPosition().getY(), target.getX() - origin.getPosition().getX());
		this.xSpeed = (float) Math.cos(theta) * baseSpeed;
		this.ySpeed = (float) Math.sin(theta) * baseSpeed;
		
		try {
			this.sprite = ImageManager.getImage("carrot");
		} catch (Exception e) {}
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
	
	public void projectileAI() {
		timer++;
		
		//homing
		if (timer < 120) {
			angle = (float) Math.atan2(target.getY() - this.getPosition().getY(), target.getX() - this.getPosition().getX());
			this.xSpeed = (float) Math.cos(angle) * baseSpeed;
			this.ySpeed = (float) Math.sin(angle) * baseSpeed;
		}
		
	}
	
}
