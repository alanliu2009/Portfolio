package objects.entity.missile;


import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

import animations.AnimationManager;
import animations.effects.Boom;
import animations.effects.SmokeWhite;
import components.DamageType;
import engine.Utility;
import engine.Values;
import engine.states.Game;
import objects.entity.Entity;
import objects.entity.unit.Unit;
import player.Player;
import ui.display.Images;
import ui.sound.Sounds;

public class Missile extends Entity
{
	public static float TRIGGER_DISTANCE = 25;
	protected static float EXPLOSION_IMAGE_SCALING = .35f;
	
	protected int smokeSize = 12;
	private Unit owner;
	private Entity target;
	private Point missPoint;
	private boolean locked;
	private float damage;
	private DamageType damageType;
	
	protected Image imageSecondary;
	protected Image imageMove;
	protected int radius;
	private int duration;
	private int time;

	public Missile(Unit owner, Entity target, boolean locked, int range, float damage, DamageType damageType, int radius) 
	{
		super(owner.getCenterX(), owner.getCenterY(), owner.getTeam());
		this.owner = owner;
		this.target = target;
		this.locked = locked;
		image = Images.missile.getSprite(0,0);
		imageSecondary = Images.missile.getSprite(0,1);
		imageMove = Images.missile.getSprite(0, 2);
		
		setSpeed(100 * Values.SPEED);
		setAcceleration(750 * Values.ACC);
		this.damage = damage;
		this.damageType = damageType;
		this.radius = radius;
		this.duration = (int) (range / getMaxSpeed() * 2.5);

		this.setStructure((int)(damage/2));
		
		//if(!locked)
		{
			float missAmount = owner.getDistance(target) / 2;

			while(missPoint == null || target.getDistance(missPoint) > missAmount)
			{
				float x = target.getCenterX() + Utility.random(-missAmount, missAmount);
				float y = target.getCenterY() + Utility.random(-missAmount, missAmount);
				
				missPoint = new Point(x, y);
				

			}
			
			turnTo(missPoint);
		}
	}
	
	public void update()
	{
		super.update();
		time++;
		
		if(locked && time > 50 && target != null && target.isAlive())
		{

			moveTo(target);	
		}
		else 
		{
			move();
		}
		
		Unit u = getNearestEnemy();
			
		// Explode on trigger of being near a unit
		if(u != null && getDistance(u) < TRIGGER_DISTANCE)
		{
			dealAreaDamage();
			die();
			return;
		}

		// Explode after too much time has passed
		if(time == duration)
		{
			dealAreaDamage();
			die();
			return;
		}
		
		
		if(Game.getTime() % 4 == 0)
		{
			makeSmoke();
		}
		
	}
	
	public void makeSmoke()
	{
		float x = getCenterX();
		float y = getCenterY();
		AnimationManager.add(new SmokeWhite(x, y, 12));
	}
	
	public void dealAreaDamage()
	{
		ArrayList<Unit> radiusEnemies = getEnemiesInRadius(radius);
		
		for(Unit u : radiusEnemies)
		{
			// Always deal full damage to the target
			if(u == target && locked)
			{
				directHit(u, damage, damageType);
			}
			
			else
			{
				splashDamage(u, damage, damageType);
			}
		}
	}
	
	public void directHit(Unit u, float damage, DamageType damageType)
	{
		//System.out.println("Hit the target!  Dealing " + damage + "damage");
		u.takeDamage(damage, damageType);
	}
	
	public void splashDamage(Unit u, float damage, DamageType damageType)
	{
		// Splash damage is up to 100% damage, based on distance from the explosion
		float percentAway = Math.max(Math.min(Utility.distance(this,  u) / (radius-TRIGGER_DISTANCE), 1), 0);
		float actualDamage = damage * (1-percentAway);
		u.takeDamage(actualDamage, damageType);
		
		//System.out.println("Splash damage!  Dealing " + actualDamage + "damage.  Full would be: " + damage);

	}
	
	public void die()
	{
		super.die();		
		addExplosionEffect();
		Sounds.boom.play(getPosition(), .8f, .5f);
	}
		
	public void addExplosionEffect()
	{
		AnimationManager.add(new Boom(getCenterX(), getCenterY(), radius * EXPLOSION_IMAGE_SCALING * getScale()));
	}
	
	public Unit getOwner()
	{
		return owner;
	}
	
	public Player getPlayer()
	{
		return getOwner().getPlayer();
	}
	
	public void render(Graphics g)
	{
		super.render(g);
		renderPrimary(g);
		renderSecondary(g);
		renderMove(g);
	}
	
	public Color getColorPrimary()
	{
		return getOwner().getColorPrimary();
	}
	
	public Color getColorSecondary()
	{
		 return getOwner().getColorSecondary();
	}
	

	
	public void renderPrimary(Graphics g)
	{
		if (image != null) 
		{
			Image tmp = image.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());
			tmp.draw(x, y, getColorPrimary());
		}
	}
	
	public void renderSecondary(Graphics g)
	{
		if (imageSecondary != null) 
		{
			Image tmp = imageSecondary.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());
			tmp.draw(x, y, getColorSecondary());
		}
	}
	
	public void renderMove(Graphics g)
	{
		if (imageMove != null) 
		{
			Image tmp = imageMove.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());
			tmp.draw(x, y);
		}
	}

	public Unit getNearestEnemy()
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  Game.getUnits();

		for(Unit u : units)
		{
			if(u.getPlayer() != getPlayer() && getDistance(u) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);
			}
		}

		return nearestUnit;
	}
	
	
	public ArrayList<Unit> getEnemiesInRadius(float radius) 
	{
		ArrayList<Unit> units = Game.getUnits();
		ArrayList<Unit> radiusEnemies = new ArrayList<Unit>();

		if (units != null) 
		{
			for (Unit e : units) 
			{
				if (e.getPlayer() != getPlayer() && getDistance(e) <= radius) 
				{
					radiusEnemies.add(e);
				}
			}
		}

		return radiusEnemies;
	}

	


}
