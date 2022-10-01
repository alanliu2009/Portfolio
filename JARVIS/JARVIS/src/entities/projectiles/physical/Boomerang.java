package entities.projectiles.physical;

import java.util.ArrayList;

import entities.core.Coordinate;
import entities.core.Entity;
import entities.core.Entity.Type;
import entities.living.Living;
import entities.projectiles.Projectile;
import managers.ImageManager;

public class Boomerang extends Projectile {
	private static float acceleration = 0.25f;
	
	private Entity origin;
	
	public Boomerang(Entity origin, Coordinate target) {
		super(origin);
		
		this.origin = origin;

		// Default Size
		this.width = 1.5f;
		this.height = 1.5f;
		
		// Default Damage
		this.damage = 25;
		
		// Default Speed
		double theta = Math.atan2(origin.getY() - target.getY(), origin.getX() - target.getX());
		this.xSpeed = (float) Math.cos(theta) * 25f;
		this.ySpeed = (float) Math.sin(theta) * 25f;
		
		// Default Sprite 
		sprite = ImageManager.getImage("boomerang");
		
		this.entityType = Type.Projectile;
		
		// Setting Team
		this.team = origin.getTeam();
		
		// Adding Entity
		game.addEntity(Type.Projectile, this);
	}
	
	@Override
	protected void entityCollisions() {
		ArrayList<Entity> entities = game.getEntities(Type.Living);
		
		for(Entity e: entities) {
			Living living = (Living) e;
			
			if(team != living.getTeam() && entityCollision(living)) {
				living.takeDamage((int) damage);
			}
		}
		
	}; 
	
	@Override
	protected void onBlockCollision() {}
	
	protected void onBlockXCollision() { this.xSpeed = -xSpeed; }
	protected void onBlockYCollision() { this.ySpeed = -ySpeed; }
	
	@Override
	protected void projectileAI() {
		this.angle += 0.01f;
		
		double theta = Math.atan2(origin.getY() - getY(), origin.getX() - getX());
		this.xSpeed += Math.cos(theta) * acceleration;
		this.ySpeed += Math.sin(theta) * acceleration;
		
		if(origin.isMarked()){
			this.remove = true;
		}
	}
	
}