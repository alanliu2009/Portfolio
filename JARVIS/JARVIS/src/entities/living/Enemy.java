package entities.living;

import core.Engine;
import core.Values;
import managers.ImageManager;
import support.Utility;

public class Enemy extends Living {
	protected int contactDmg;
	
	protected Player target;
	protected int aggroRange;
	protected float moveSpeed;
	
	public Enemy(float x, float y) 
	{
		super(x,y); 
		
		this.team = Team.Enemy;
		target = Engine.game.getPlayer();

		
		this.sprite = ImageManager.getImage("mushroom");
		
		this.maxHealth = 5;
		this.curHealth = maxHealth;
		
		contactDmg = 5;
		aggroRange = 20;
		width = 1f;
		height = 1f;
		healthRegen = false;
		
		this.jumps = 0;
	}
	// Overwritten update method
	@Override
	public void update() {
		if(this.position.magDisplacement(target.getPosition()) > Values.Entity_Despawn_Distance 
				|| curHealth <= 0) { 
			this.remove = true;
			return;
		}
		
		ai(target);
		super.update();
	}

	// Overwritten entity collision method
	protected void entityCollisions() {
		if(this.entityCollision(target)) {
			target.takeDamage(contactDmg, true);
		}
	}
	
	public void ai(Player p) {
		if(Utility.getDistance(this, p) <= aggroRange) {
			moveToPlayerX(p, 6.5f);
			if(Utility.random(0,100) < 25) {
				jump(15f);
			}
			
		}
	}
	
	public void moveToPlayerX(Player p, float speed) {
		if(Utility.changeX(this, p) > 0) {
			setXSpeed(speed);
		}else if(Utility.changeX(this, p) < 0) {
			setXSpeed(-speed);
		}
	}
	

}
