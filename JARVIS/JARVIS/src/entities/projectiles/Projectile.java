package entities.projectiles;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import core.Engine;
import entities.core.Coordinate;
import entities.core.Entity;
import entities.living.Living;
import managers.ImageManager;

public class Projectile extends Entity {
	protected float damage;
	
	public Projectile(Entity origin){
		super(
				origin.getPosition().getX(), 
				origin.getPosition().getY()
			);
		
		this.entityType = Type.Projectile;
		
		// Setting Team
		this.team = origin.getTeam();
		
		// Adding Entity
		game.addEntity(Type.Projectile, this);
	}
	
	

	@Override // Overwritten update method
	public void update() {
		projectileAI();
		
		// Collision detection 
		checkCollisions();
		
		// Position updating
		position.update(xSpeed, ySpeed);
	}
	
	protected void projectileAI() {}
	
	@Override
	protected void entityCollisions() {
		ArrayList<Entity> entities = game.getEntities(Type.Living);
		
		for(Entity e: entities) {
			Living living = (Living) e;
			
			if(team != living.getTeam() && entityCollision(living)) {
				living.takeDamage((int) damage);
				this.remove = true;
			}
		}
		
	}; 
	
	@Override
	protected void onBlockCollision() {
		// Die when hitting a block
		this.remove = true;
	}
}