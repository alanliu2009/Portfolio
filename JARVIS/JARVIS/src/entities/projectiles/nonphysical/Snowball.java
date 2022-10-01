package entities.projectiles.nonphysical;

import entities.core.Coordinate;
import entities.living.Living;
import entities.projectiles.Projectile;
import managers.ImageManager;

public class Snowball extends Projectile {
	
	private float baseSpeed = 10f;
	private double theta;
	private Living origin;
	private Coordinate target;
	private int timer;
	
	public Snowball (Living origin, Coordinate target) {
		super(origin);
		this.origin = origin;
		this.target = target;
		
		this.damage = 10;
		
		theta = Math.atan2(target.getY() - origin.getPosition().getY(), target.getX() - origin.getPosition().getX());
		this.xSpeed = (float) Math.cos(theta) * baseSpeed;
		this.ySpeed = (float) Math.sin(theta) * baseSpeed;
		
		this.sprite = ImageManager.getImage("snowball");
		
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
	
	public void update() {
		super.update();
		timer++;
	}
	
	public void projectileAI() {
		super.projectileAI();
		
		//homing
//		if (timer < 100000) {
//			theta = Math.atan2(target.getY() - origin.getPosition().getY(), target.getX() - origin.getPosition().getX());
//			this.xSpeed = (float) Math.cos(theta) * baseSpeed;
//			this.ySpeed = (float) Math.sin(theta) * baseSpeed;
//		}
		
	}
	
}
