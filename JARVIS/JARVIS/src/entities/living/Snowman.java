package entities.living;

import core.Engine;
import entities.projectiles.nonphysical.Carrot;
import entities.projectiles.nonphysical.Snowball;
import managers.ImageManager;
import support.Utility;

public class Snowman extends Enemy {
	int shotCooldown;
	int groupCooldown;
	int carrotCooldown;
	
	public Snowman (float x, float y) {
		super(x, y);
		width = 1;
		height = 2;
		
		this.maxHealth = 20;
		this.curHealth = maxHealth;
		
		shotCooldown = 0;
		groupCooldown = 0;
		carrotCooldown = 0;
		contactDmg = 10;
		aggroRange = 40;
	
		this.sprite = ImageManager.getImage("snowmanRight");
		
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}
	
	public void update() {
		super.update();
		shotCooldown++;
		groupCooldown++;
		carrotCooldown++;
	}
	
	public boolean getPastDirection() {
		if (Engine.game.getPlayer().getPosition().getX() > this.getPosition().getX()) {
			return true;
		}
		if (Engine.game.getPlayer().getPosition().getX() < this.getPosition().getX()) {
			return false;
		}
		return false;
	}
	
	public void ai (Player p) {
		if(Utility.getDistance(this, p) <= aggroRange) {
			
			if (groupCooldown % 80 == 0) {
				shotCooldown = 0;
			}
			
			if (shotCooldown == 0 || shotCooldown == 4 || shotCooldown == 8) {
				new Snowball(this, Engine.game.getPlayer().getPosition());
			}
			
			if (carrotCooldown % 200 == 0) {
				new Carrot(this, Engine.game.getPlayer().getPosition());
			}
			
		}
	}
}
