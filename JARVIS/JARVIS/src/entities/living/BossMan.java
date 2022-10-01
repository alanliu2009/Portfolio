package entities.living;

import org.lwjgl.Sys;

import core.Engine;
import entities.core.Coordinate;
import entities.core.Entity.Team;
import entities.projectiles.nonphysical.MissileStrike;
import entities.projectiles.physical.Boomerang;
import managers.ImageManager;

public class BossMan extends Enemy {
	public float attackCooldown;
	
	public BossMan(float x, float y) {
		super(x,y);

		this.sprite = ImageManager.getImage("mushroom").getScaledCopy(1f);
		this.sprite.setImageColor(0.63f, 0.13f, 0.94f);
		
		this.contactDmg = 500;
		
		this.maxHealth = 1000;
		this.curHealth = maxHealth;
		
		this.width = 3.5f;
		this.height = 3.5f;
		this.healthRegen = true;
		
		this.hitbox.setWidth(width);
		this.hitbox.setHeight(height);
		
		attackCooldown = Sys.getTime();
	}
	
	public void ai(Player p) {
		if(position.magDisplacement(p.getPosition()) > 40f) {
			position.setXPos(p.getX() + (Math.signum(p.getXSpeed()) * ((float) Math.random() * 3f + 2.5f)));
			position.setYPos(p.getY() + ((float) Math.random() * 2.5f + 7.5f));
		}
				
		if(Sys.getTime() - attackCooldown > 3.5f * 1000) {
			new Boomerang(this, p.getPosition());
			new MissileStrike(this, new Coordinate(p.getX(), p.getY()));
			
			attackCooldown = Sys.getTime();
		}
	}
}