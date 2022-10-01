package entities.living;

import core.Engine;
import core.Values;
import entities.core.Entity;
import managers.SoundManager;

public class Living extends Entity {	
	/*
	 * Stat Variables - Unused, but we can implement them later
	 */	
	protected int curHealth, maxHealth;
	protected float percentageHealth;
	
	protected boolean alive;
	
	protected int attack;
	protected int defense;
	
	protected int iFrames;
	protected int iDuration;
	protected boolean healthRegen;
	protected int regenTimer;
	protected int timeLastHit;
	protected int regenRate;
	protected int regenInc;
	
	protected int jumps; // Determines how many jumps are left
	
	public Living(float InitX, float InitY) {
		super(InitX, InitY);

		// Invincibility
		this.iFrames = 0;
		this.iDuration = 30; //how long invulnerability will last after taking damage
		
		alive = true;
		
		// Health
		curHealth = 50;
		maxHealth = 50;
		percentageHealth = 1f;
		
		// Regen
		regenTimer = 120;
		healthRegen = false;
		regenRate = 30;
		regenInc = 0;
		
		// Other
		timeLastHit = 0;
		
		jumps = 0;
		
		this.entityType = Type.Living;
		
		// Add Entity
		game.addEntity(Type.Living, this);
	}
	
	public float getPercentHealth() { return this.percentageHealth; }
	public void setXSpeed(float acceleration) {
		xSpeed += acceleration;
	}
	public void jump(float speed) {
		if(jumps > 0) {
			this.ySpeed = speed;
			
			jumps--;
		}
	}
	public void fall() {
		this.ySpeed -= Values.Acceleration_of_Gravity;
	}

	public void takeDamage(int dmg, boolean i) { //boolean for iFrames cause for certain piercing attacks that don't trigger them
		//this mimics the mechanics in Terraria
		if(iFrames == 0) {
			SoundManager.playSound("Breh"); // Testing sound effects
			
			if(healthRegen) {
				timeLastHit = 0;
				regenInc = 0;
			}
			
			dmg -= defense;
			if(dmg <= curHealth) {
				if(dmg <= 0) { //if defense is higher than dmg taken you will just take 1 dmg
					curHealth -= 1;
				} else {
					curHealth -= dmg;
				}
			}else {
				curHealth = 0;
			}
			if(curHealth <= 0) {
				alive = false;
			}
			else if(i) {
				setIFrames(iDuration);
			}
			
		}
	}
	public void regen() {
		if(healthRegen && timeLastHit >= regenTimer && curHealth < maxHealth) {
			regenInc++;
			if(regenInc % regenRate == 0) { curHealth++; }	
		}
	}

	//gives entity number of iframs that will automatically start ticking down each frame in update()
	public void setIFrames(int frames) { iFrames = frames; }
	public boolean isAlive() { return alive; }
	public boolean getInvincible() { return iFrames > 0; }
	
	public void takeDamage(int damage) {
		curHealth -= damage;
		if (curHealth < 0) {
			curHealth = 0;
			alive = false;
		}
	}
		
	protected void onBlockYCollision () { 
		this.jumps = (int) -Math.signum(ySpeed) * 2; 
		super.onBlockYCollision();
	}

	public void update() {
		if(curHealth < 0) {
			this.remove = true;
			return;
		}
		
		// Update Stats
		percentageHealth = ((float) curHealth) / ((float) maxHealth); // Update health
		
		if(iFrames > 0) { //timer that ticks down invincibility frames
			iFrames --;
		}
		if(healthRegen && timeLastHit < 240) {
			timeLastHit++;
		}
		regen();
		
		super.update();
	}
}