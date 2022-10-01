package objects.entity.unit;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import animations.AnimationManager;
import animations.effects.Afterimage;
import animations.effects.Boom;
import animations.effects.Smoke;
import components.Component;
import components.ComponentSystem;
import components.DamageType;
import components.upgrade.Upgrade;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import components.weapon.resource.Collector;
import conditions.buffs.Fortified;
import engine.Settings;
import engine.Utility;
import engine.Values;
import engine.states.Game;
import objects.entity.Entity;
import objects.entity.missile.Missile;
import objects.entity.missile.MissileShadowflight;
import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.resource.Minerals;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import objects.resource.Scrap;
import player.Boon;
import player.Player;
import ui.display.Images;
import ui.display.healthbar.Healthbar;
import ui.display.healthbar.UnitHealthbar;
import ui.sound.Sounds;

public abstract class Unit extends Entity implements Values 
{

	/***************************** DATA ***************************************/

	// Public Constants
	public final static int BASE_SPIN_RATE = 5;
	public final static int SPAWN_BORDER = 60;
	public final static float RESOURCE_DROP_CHANCE = .1f;
	public static final int TRANSFER_RANGE = 150;
	public final static float BASE_MIN_RANGE_MULTIPLIER = .1f;

	
	protected Image imageSecondary;
	protected Image imageAccent;
	protected Image imageMove;
	
	// Private Data
//	private final Player p;
	private int cost;
	private boolean drawFlash;
	private boolean drawFlashInvuln;
	private ComponentSystem components;
	private int minerals;
	private int scrap;
	private float capacity;
	private Frame frame;
	private Healthbar healthbar;

	private Weapon weaponOne;
	private Weapon weaponTwo;
	
	private float useMultiplier = 1;
	private float cooldownMultiplier = 1;
	private float maxRangeMultiplier = 1;
	private float accuracyMultiplier = 1;
	private float minRangeMultiplier = 0.1f;
	
	public Unit(Player p) 
	{
		super(0, 0, p.getTeam());

		setPlayer(p);

		Point pos = getSpawn(p);
		this.x = pos.getX();
		this.y = pos.getY();
		components = new ComponentSystem(this);
		healthbar = new UnitHealthbar(this);
		
	}

	public void setStyle(Style style)
	{
		int id = style.getID();
		
		if(getFrame() == Frame.LIGHT)
		{
			sheet = Images.light[id];
		}
		if(getFrame() == Frame.MEDIUM)
		{
			sheet = Images.medium[id];
		}
		if(getFrame() == Frame.HEAVY)
		{
			sheet = Images.heavy[id];
		}
		if(getFrame() == Frame.ASSAULT)
		{
			sheet = Images.assault[id];
		}
		setImage();
	}

	// Abstract Methods

	abstract public void action();
	abstract public void design();
	abstract public void draw(Graphics g);

	/***************************** ACCESSOR METHODS ***************************************/


	public final int getValue() 				{	return cost;										}

	public final Frame getFrame()				{	return frame;											}
	public final Weapon getWeaponOne()			{	return weaponOne;												}
	public final Weapon getWeaponTwo()			{	return weaponTwo;												}
	public final boolean hasWeaponOne()			{	return weaponOne != null;										}
	public final boolean hasWeaponTwo()			{	return weaponTwo != null;										}	
	
	public final int getCargo()					{	return minerals + scrap;								}
	public final int getMinerals()				{	return minerals;										}
	public final int getScrap()					{	return scrap;											}

	public final int getCapacity()				{	return Math.round(capacity);							}
	public final int getOpenCapacity()			{	return getCapacity() - getCargo();						}
	public final boolean hasCapacity()			{	return getCargo() < getCapacity();						}
	public final boolean isFull()				{	return getCargo() == getCapacity();						}
	public final boolean isEmpty()				{	return getCargo() == 0;									}
	public final float getCargoPercent()		{	return (float) getCargo() / (float) getCapacity();		}

	public final float getStructureSlowPercent()	{	return(getPercentStructure() / 2 + .5f);					}
	// Actions and Movement

	public final boolean canMove() 				{	return super.canMove() && !isUseLocked();						}
	public final boolean isUseLocked() 			{	return (hasWeaponOne() && weaponOne.inUse()) || (hasWeaponTwo() && weaponTwo.inUse());	}
	public final float getRotation()			{	return theta;													}

	// Weapons
	public final boolean hasEnergyDamage()		{	return hasDamageType(DamageType.ENERGY);				}
	public final boolean hasKineticDamage()		{	return hasDamageType(DamageType.KINETIC);				}
	public final boolean hasExplosiveDamage()	{	return hasDamageType(DamageType.EXPLOSIVE);				}

	// Cosmetic
	public final Color getColorPrimary()		{	return getPlayer().getColorPrimary();					}
	public Color getColorSecondary()			{	return getPlayer().getColorSecondary();					}
	public Color getColorAccent()				{	return getPlayer().getColorAccent();					}
	
	public final Image getImageSecondary()		{	return imageSecondary;									}
	public final Image getImageAccent()			{	return imageAccent;										}
	public final Image getImageMove()			{	return imageMove;										}						
	
	public final boolean hasComponentSlotOpen()				{ return getComponentSlotsOpen() >= 1;			}
	public final boolean hasComponentSlotsOpen(int i)		{ return getComponentSlotsOpen() >= i;			}
	public final boolean canAddComponent(Component c)	{ return components.canAdd(c);					}
	public final int getComponentSlotsUsed()			{ return components.getComponentSlotsUsed();		}
	public final int getComponentSlotsOpen()			{ return components.getComponentSlotsOpen();		}

	public final float getCooldownTimeMultiplier()		{ return cooldownMultiplier;		}
	public final float getUseTimeMultiplier()			{ return useMultiplier;				}
	public final float getAccuracyMultiplier()			{ return accuracyMultiplier;			}
	public final float getMaxRangeMultiplier()			{ return maxRangeMultiplier;			}
	public final float getMinRangeMultiplier()			{ return minRangeMultiplier;		}
	
	public final float getDodgeChance(float acc)
	{
		return getDodgeChance() - acc * getDodgeChance();
	}
	
	public final float getHitChance(float acc)
	{
		return 1 - getDodgeChance(acc);
	}
	
	public final boolean rollToHit(float acc)
	{
		double roll = Math.random();
		boolean isHit = roll > getDodgeChance(acc);
		
		if(Settings.dbgShowDodge && !isHit)
		{
			dbgMessage("Dodge!", 60);			
		}
		
		if(!isHit)
		{
			getPlayer().addDodge();
			Game.addDodge();
		}
		
		
		getPlayer().addDodgeAttempt();
	
	//	System.out.println(getPlayer().getDodgeAttempts());
		
//		if(getConditions().preventsDamage())
//		{
//			System.out.println(this + " rolled " + (int) (roll * 100)  + " but shielded it (Acc " + acc + " / " + (int) (getDodgeChance(acc) * 100) + "% dodge chance)");
//		}
//		else if(isHit)
//		{
//			System.out.println(this + " rolled " + (int) (roll * 100)  + " and was hit (Acc " + acc + " / " + (int) (getDodgeChance(acc) * 100) + "% dodge chance)");
//		}
//		else
//		{
//			System.out.println(this + " rolled " + (int) (roll * 100)  + " and dodged  (Acc " + acc + " / "  + (int) (getDodgeChance(acc) * 100) + "% dodge chance)");
//		}

		return isHit;
	}

	public final void takeDamage(float amount, DamageType type)
	{
		if(isInvulnerable())
		{
			drawFlashInvuln = true;
		}
		else
		{
			if(amount >= 1)
			{
				drawFlash = true;
			}

			super.takeDamage(amount, type);
		}
	}

	public final int getMaxRange()
	{
		if(hasWeaponOne() && hasWeaponTwo())
		{
			return Math.max(getWeaponOne().getMaxRange(), getWeaponTwo().getMaxRange());
		}
		else if(hasWeaponOne())
		{
			return weaponOne.getMaxRange();
		}
		else if(hasWeaponTwo())
		{
			return weaponTwo.getMaxRange();
		}
		else
		{
			return 0;
		}
	}
		
	public final int getMinRange()
	{
		if(hasWeaponOne() && hasWeaponTwo())
		{
			return Math.min(getWeaponOne().getMinRange(), getWeaponTwo().getMinRange());
		}
		else if(hasWeaponOne())
		{
			return weaponOne.getMinRange();
		}
		else if(hasWeaponTwo())
		{
			return weaponTwo.getMinRange();
		}
		else
		{
			return 0;
		}
	}


	public final boolean hasDamageType(DamageType type)
	{
		return (hasWeaponOne() && weaponOne.getDamageType().isType(type)) || (hasWeaponTwo() && weaponTwo.getDamageType().isType(type));
	}

	public final boolean hasComponent(Class<? extends Component> clazz)
	{
		return components.has(clazz);
	}

	public final boolean hasWeapon(Class<? extends Weapon> clazz)
	{
		return hasComponent(clazz);
	}
	
	public final boolean hasWeapon(WeaponType type)
	{
		return components.has(type);
	}
	
	public final Component getComponent(Class<? extends Component> clazz)
	{
		return components.get(clazz);
	}
	
	public final Component getComponent(int i)
	{
		return components.get(i);
	}
	
	public final ArrayList<Component> getComponents()
	{
		return components.getAll();
	}
	
	public final int countComponents()
	{
		return components.getAll().size();
	}
	
	public final Weapon getWeapon(Class<? extends Weapon> clazz)
	{
		return (Weapon) getComponent(clazz);
	}



	public final Point getSpawn(Player p) 
	{
		BaseShip b = Game.getBaseShip(p);
		if (b == null)
		{
			return new Point(0, 0);
		}
		Rectangle zone = new Rectangle(b.getX() + SPAWN_BORDER, b.getY() + SPAWN_BORDER, b.getWidth() - SPAWN_BORDER,b.getHeight() - SPAWN_BORDER);
		return new Point(Utility.random(zone.getX(), zone.getX() + zone.getWidth()), Utility.random(zone.getY(), zone.getY() + zone.getHeight()));
	}

	public final BaseShip getHomeBase() 
	{
		return getPlayer().getMyBase();
	}

	public final BaseShip getEnemyBase() 
	{
		return getPlayer().getEnemyBase();
	}

	public final ArrayList<Unit> getUnits(Player p)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		ArrayList<Unit> allUnits = Game.getUnits();
		for(Unit u : allUnits)
		{
			if(u.getPlayer() == p)
			{
				units.add(u);
			}
		}
		return units;
	}

	public final ArrayList<Unit> getEnemies()
	{
		return getUnits(getOpponent());
	}
	
	public final ArrayList<Unit> getEnemiesExcludeBaseShip()
	{
		ArrayList<Unit> enemyUnits = new ArrayList<Unit>();
		ArrayList<Unit> enemies = getEnemies();

		for(Unit u : enemies)
		{
			if(!(u instanceof BaseShip))
			{
				enemyUnits.add(u);
			}
		}

		return enemyUnits;
	}


	public final ArrayList<Unit> getAllies()
	{
		return getUnits(getPlayer());
	}


	public final ArrayList<Unit> getAlliesExcludeBaseShip()
	{
		ArrayList<Unit> alliedUnits = new ArrayList<Unit>();
		ArrayList<Unit> allies = getUnits(getPlayer());

		for(Unit u : allies)
		{
			if(!(u instanceof BaseShip))
			{
				alliedUnits.add(u);
			}
		}

		return alliedUnits;
	}

	// Find the nearest unit owned 
	public final Unit getNearestUnit()
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  Game.getUnits();

		for(Unit u : units)
		{
			if(this != u && getDistance(u) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);
			}
		}

		return nearestUnit;
	}
	
	// Find the nearest unit owned by specified player than is belongs to the specified class
	public final Unit getNearestUnit(Player p, Class<? extends Unit> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  Game.getUnits();

		for(Unit u : units)
		{
			if(this != u && u.getPlayer() == p && clazz.isInstance(u) && getDistance(u) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);
			}
		}

		return nearestUnit;
	}

	// Find the nearest unit owned by specified player than is NOT of the specified class
	public final Unit getNearestUnitExclude(Player p, Class<? extends Unit> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  Game.getUnits();

		for(Unit u : units)
		{
			if(this != u && u.getPlayer() == p && !clazz.isInstance(u) && getDistance(u) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);

			}
		}

		return nearestUnit;
	}

	public final Unit getNearestEnemy()
	{
		Unit u = getNearestEnemyUnit();

		if(u == null)
		{
			u = getPlayer().getEnemyBase();
		}

		return u;
	}

	public final Unit getNearestEnemyUnit()
	{
		return getNearestUnitExclude(getPlayer().getOpponent(), BaseShip.class);
	}

	public final Unit getNearestAlly()
	{
		Unit u = getNearestAllyUnit();

		if(u == null)
		{
			u = getPlayer().getMyBase();
		}

		return u;
	}

	public final Unit getNearestAlly(Class<? extends Unit> clazz)
	{
		return getNearestUnit(getPlayer(), clazz);
	}

	public final Unit getNearestAllyUnit()
	{
		return getNearestUnitExclude(getPlayer(), BaseShip.class);
	}



	// ENEMY

	//	public Unit nearestEnemy() {
	//		return nearestEnemy(Unit.class);
	//
	//	}
	//
	//	public Unit nearestEnemy(Class<? extends Unit> clazz) {
	//		if (enemies == null || enemies.isEmpty())
	//			return null;
	//
	//		float nearestDistance = Float.MAX_VALUE;
	//		Unit nearestTarget = null;
	//
	//		for (Unit a : enemies) {
	//			float d = Utility.distance(this, a);
	//
	//			if (d < nearestDistance && clazz.isInstance(a)) {
	//				nearestDistance = d;
	//				nearestTarget = a;
	//			}
	//		}
	//
	//		return nearestTarget;
	//
	//	}
	//
	//	public Unit nearestEnemyExclude(Class<? extends Unit> clazz) {
	//		if (enemies == null || enemies.isEmpty())
	//			return null;
	//
	//		float nearestDistance = Float.MAX_VALUE;
	//		Unit nearestTarget = null;
	//
	//		for (Unit a : enemies) {
	//			float d = Utility.distance(this, a);
	//
	//			if (d < nearestDistance && !clazz.isInstance(a) && a != this && !(a instanceof BaseShip)) {
	//				nearestDistance = d;
	//				nearestTarget = a;
	//			}
	//		}
	//
	//		return nearestTarget;
	//
	//	}
	//
	//	public ArrayList<Unit> getEnemies(Class<? extends Unit> clazz) {
	//		if (enemies == null || enemies.isEmpty())
	//			return null;
	//
	//		ArrayList<Unit> allEnemies = new ArrayList<Unit>();
	//		for (Unit e : enemies) {
	//			if (clazz.isInstance(e)) {
	//				allEnemies.add(e);
	//			}
	//		}
	//		return allEnemies;
	//	}
	//
	
	public final int countAlliesInRadius(float radius) 
	{
		ArrayList<Unit> allies = getAllies();
		if (allies == null || allies.isEmpty())
		{
			return 0;
		}
		return getAlliesInRadius(radius, Unit.class).size();
	}
	
	public final ArrayList<Unit> getAlliesInRadius(float radius)
	{
		return getAlliesInRadius(radius, Unit.class);
	}

	public final ArrayList<Unit> getAlliesInRadius(float radius, Class<? extends Unit> clazz) 
	{
		return getAlliesInRadius(radius, clazz, getPosition());
	}
	
	public final ArrayList<Unit> getAlliesInRadius(float radius, Class<? extends Unit> clazz, Point p) 
	{
		ArrayList<Unit> allies = getAllies();
		ArrayList<Unit> radiusAllies = new ArrayList<Unit>();

		if (allies != null) 
		{
			for (Unit a : allies) 
			{
				if (this != a && a.getDistance(p) <= radius && clazz.isInstance(a)) 
				{
					radiusAllies.add(a);
				}
			}
		}

		return radiusAllies;
	}
	

	public final ArrayList<Unit> getAlliesInRadiusExclude(float radius, Class<? extends Unit> clazz, Point p) 
	{
		ArrayList<Unit> allies = getAllies();
		ArrayList<Unit> radiusAllies = new ArrayList<Unit>();

		if (allies != null) 
		{
			for (Unit a : allies) 
			{
				if (this != a && a.getDistance(p) <= radius && !clazz.isInstance(a)) 
				{
					radiusAllies.add(a);
				}
			}
		}

		return radiusAllies;
	}
	
	public final int countEnemiesInRadius(float radius) 
	{
		ArrayList<Unit> enemies = getEnemies();
		if (enemies == null || enemies.isEmpty())
		{
			return 0;
		}
		return getEnemiesInRadius(radius, Unit.class).size();
	}

	public final ArrayList<Unit> getEnemiesInRadius(float radius)
	{
		return getEnemiesInRadius(radius, Unit.class);
	}

	public final ArrayList<Unit> getEnemiesInRadius(float radius, Class<? extends Unit> clazz) 
	{
		return getEnemiesInRadius(radius, clazz, getPosition());
	}
	
	public final ArrayList<Unit> getEnemiesInRadius(float radius, Class<? extends Unit> clazz, Point p) 
	{
		ArrayList<Unit> enemies = getEnemies();
		ArrayList<Unit> radiusEnemies = new ArrayList<Unit>();

		if (enemies != null) 
		{
			for (Unit e : enemies) 
			{
				if (e.getDistance(p) <= radius && clazz.isInstance(e)) 
				{
					radiusEnemies.add(e);
				}
			}
		}

		return radiusEnemies;
	}

	public final ArrayList<Unit> getNearestEnemiesInRadius(int radius, int number)
	{
		ArrayList<Unit> enemyUnits = getEnemiesInRadius(radius);
		ArrayList<Unit> nearestUnits = new ArrayList<Unit>();;	

		// Loop through all enemy units within radius
		for(Unit u : enemyUnits)
		{
			boolean added = false;
			float distance = getDistance(u);

			// First one always added
			if(nearestUnits.isEmpty())
			{
				nearestUnits.add(u);
			}

			// Otherwise, check all units
			else
			{	

				for(int i = 0; i < nearestUnits.size(); i++)
				{
					// If it's closer than the current unit, put it in front
					Unit n = nearestUnits.get(i);

					if(distance <= getDistance(n))
					{
						nearestUnits.add(i, u);
						added = true;
						break;
					}
				}

				// If it's under target, put it at the end
				if(nearestUnits.size() < number && !added)
				{
					nearestUnits.add(u);
					added = true;
				}

				// If it's too long, remove the last one
				else if(nearestUnits.size() > number)
				{
					nearestUnits.remove(nearestUnits.size() - 1);
				}

			}

		}

		return nearestUnits;
	}



	public final Node getNearestNode(Class<? extends Node> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Node nearestNode = null;
		ArrayList<Node> nodes =  NodeManager.getNodes();

		for(Node n : nodes)
		{
			if(n.isInBounds() && getDistance(n.getPosition()) < nearestDistance && clazz.isInstance(n))
			{
				nearestNode = n;
				nearestDistance = getDistance(n.getPosition());
			}
		}

		return nearestNode;
	}
	
	public final Node getNearestNode()
	{
		return getNearestNode(Node.class);
	}

	public final Resource getNearestResource(Class<? extends Resource> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Resource nearestResource = null;
		ArrayList<Resource> resources = ResourceManager.getResources();

		for(Resource r : resources)
		{
			if(r.isInBounds() && getDistance(r.getPosition()) < nearestDistance  && clazz.isInstance(r))
			{
				nearestResource = r;
				nearestDistance = getDistance(r.getPosition());
			}
		}

		return nearestResource;
	}
	
	public final Resource getNearestResource()
	{
		return getNearestResource(Resource.class);
	}

	/***************************** MUTATOR METHODS ***************************************/

	public final void setImageSecondary(Image i)	{		imageSecondary = i;	}
	public final void setImageAccent(Image i)		{		imageAccent = i;	}
	public final void setImageMove(Image i)		{		imageMove = i;		}

	public final void setFrame(Frame frame)
	{
		this.frame = frame;
		addMineralCost(frame.getMineralCost());
	
		setStructure((int) (frame.getStructure()));
		setSpeed(frame.getSpeed());
		setAcceleration(frame.getAcc());
	
		applyBoonToStartingAttributes();
	}
	
	public final void applyBoonToStartingAttributes()
	{
		if(!(this instanceof BaseShip))
		{					
			if(getPlayer().hasBoon(Boon.SCOUT))
			{
				increaseSpeedByPercent(Boon.SCOUT_SPEED_BONUS);
				increaseAcceleration(frame.getAcc() * Boon.SCOUT_SPEED_BONUS);
			}
			
			if(getPlayer().hasBoon(Boon.TRADER))
			{
				increaseSpeedByPercent(Boon.TRADER_SPEED_BONUS);
			}
			
			if(getPlayer().hasBoon(Boon.MECHANIC))
			{
				increaseHullRepairEfficiency(Boon.MECHANIC_REPAIR_EFFICIENCY_BONUS);
			}
			
			if(getPlayer().hasBoon(Boon.SNIPER))
			{
				applyMaxRangeMultiplier(Boon.SNIPER_MAX_RANGE_MULTIPLIER_BONUS);
				applyMinRangeMultiplier(Boon.SNIPER_MIN_RANGE_MULTIPLIER_PENALTY);
			}
		}
	}

	public final void deposit()
	{
		if(getDistance(getHomeBase()) < 100)
		{
			float resources = getMinerals() + getScrap();
			resources *= getPlayer().getDifficultyRating();
			getPlayer().addMinerals(resources);
			
			minerals = 0;
			scrap = 0;
		}

	}

	public final void transfer(Unit u)
	{
		while(getDistance(u) <= TRANSFER_RANGE && u.hasCapacity() && minerals > 0)
		{
			u.collect(new Minerals(x, y));
			minerals--;
		}

		while(getDistance(u) <= TRANSFER_RANGE && u.hasCapacity() && scrap > 0)
		{
			u.collect(new Scrap(x, y));
			scrap--;
		}
	}

	public final void dump()
	{
		for(int i = 0; i < minerals; i++)
		{
			float xPos = x + Utility.random(w);
			float yPos = y + Utility.random(h);
			ResourceManager.add(new Minerals(xPos, yPos, xSpeed, ySpeed));
		}
		minerals = 0;

		for(int i = 0; i < scrap; i++)
		{
			float xPos = x + Utility.random(w);
			float yPos = y + Utility.random(h);
			ResourceManager.add(new Scrap(xPos, yPos, xSpeed, ySpeed));
		}
		scrap = 0;
	}

	public final void collect(Resource r)
	{
		if(r != null && hasWeapon(Collector.class) && getDistance(r) <= Collector.PICKUP_RADIUS && hasCapacity())
		{
			if(r instanceof Minerals)
			{
				r.setForRemovalFromMap();
				minerals++;
			}
			else if(r instanceof Scrap)
			{
				r.setForRemovalFromMap();
				scrap++;
			}
		}
	}

	public final void setCapacity(float amount)
	{
		capacity = amount;
	}

	public final void increaseCapacity(float amount)
	{
		if(getPlayer().hasBoon(Boon.TRADER))
		{
			capacity += amount * Boon.TRADER_CAPACITY_MULTIPLIER;
		}
		else
		{
			capacity += amount;
		}
	}
	
	
	public final void applyCooldownTimeMultiplier(float amount)	{	cooldownMultiplier = Math.max(0, cooldownMultiplier * amount);	}
	public final void applyUseMultiplier(float amount)			{	useMultiplier = Math.max(0, useMultiplier * amount);			}
	public final void applyAccuracyMultiplier(float amount)		{	accuracyMultiplier = Math.max(0, accuracyMultiplier * amount);	}
	public final void applyMaxRangeMultiplier(float amount)		{	maxRangeMultiplier = Math.max(0, maxRangeMultiplier * amount);	}
	public final void applyMinRangeMultiplier(float amount)		{	minRangeMultiplier = Math.max(0, minRangeMultiplier * amount);	}
	
	
//	public final float getCooldownTimeMultiplier()		{ return cooldownTimeMultiplier;		}
//	public final float getUseTimeMultiplier()			{ return useTimeMultiplier;				}


	public final void addWeapon(Weapon w)
	{
		if(components.add(w))
		{
			if(weaponOne == null)
			{
				weaponOne = w;
			}
			else if(weaponTwo == null)
			{
				weaponTwo = w;
			}
		}
	}

	public final void addUpgrade(Upgrade u)
	{
		components.add(u);
	}	

	public final float getDodgeChance()
	{
		if(getPlayer().hasBoon(Boon.PILOT))
		{
			return getCurSpeed() * Boon.PILOT_DODGE_MULTIPLIER / Values.SPEED / Values.DODGE_FULL;
		}
		else
		{
			return getCurSpeed() / Values.SPEED / Values.DODGE_FULL;
		}
	}

	public void update() 
	{	
		super.update();

		components.update();	

		
		updateSmoke();
		
		

//		
		action();

		if(Settings.dbgShowUnitCargo)	{	dbgMessage(getCargo() + " / " + getCapacity());		}

		// When stunned, spin out
		if(getConditions().stopsAction() && getConditions().stopsMovement())
		{
			rotate(getTheta() + getCurSpeed() * spinDirection * BASE_SPIN_RATE);
		}
		
		if (!isInBounds()) 
		{
			takeDamage(Values.OUT_OF_BOUNDS_DAMAGE_PER_FRAME, DamageType.TRUE);
		}
	}
	
	private final void updateSmoke()
	{
		//dbgMessage((getStructureMultiplier()));
		int smokeFreq = (int) (50 * Math.pow(getStructureSlowPercent(), 2));
		if(!(this instanceof BaseShip) && getStructureSlowPercent() < 1 && Game.getTime() % smokeFreq == 0)
		{
			float scale = 1-getStructureSlowPercent();
			float x = getCenterX() + Utility.random(-getSize() * scale, getSize() * scale);
			float y = getCenterY() + Utility.random(-getSize() * scale, getSize() * scale);
			float size = Utility.random(getSize() * .65, getSize()* .9);
			AnimationManager.add(new Smoke(x, y, size));
		}
	}

	protected final void updateSpeed()
	{
		float maxSpeedAfterConditions = getMaxSpeedBase();
		float accAfterConditions = getAccelerationBase();
		
		if(getConditions().modifiesSpeed())
		{			
			maxSpeedAfterConditions = getConditions().getModifiedSpeed(getMaxSpeedBase());
			accAfterConditions = getConditions().getModifiedSpeed(getAccelerationBase());
		}
		
		setSpeedCurrent(maxSpeedAfterConditions * getStructureSlowPercent());
		setAccelerationCurrent(accAfterConditions * getStructureSlowPercent());
		
		changeSpeed(0, theta);
	}
	
	public final void addMineralCost(int amount)
	{
		cost += amount;
	}



	public final void setImage()
	{		
		setImage(sheet.getSprite(0, 0));	
		setImageSecondary(sheet.getSprite(1, 0));	
		setImageAccent(sheet.getSprite(2, 0));	

		if(this instanceof BaseShip || !isMoving())
		{
			setImageMove(null);
		}
		else if(atMaxSpeed())
		{
			setImageMove(sheet.getSprite(4, 0));
		}
		else
		{
			setImageMove(sheet.getSprite(3, 0));
		}

		updateWidthAndHeightToScale();

	}
	


	public final void renderUnit(Graphics g)
	{



		renderPrimary(g);
		renderSecondary(g);
		renderAccent(g);
		renderMove(g);
		renderConditions(g);

	}

	public final void renderConditions(Graphics g)
	{
		// When moving faster than normal, show animation
		if(getConditions().increasesSpeed() && timer % Afterimage.FREQUENCY == 0)
		{
			AnimationManager.add(new Afterimage(this, getX(), getY()));	
		}

	}

	public final void renderPrimary(Graphics g)
	{
		if (image != null) 
		{
			Image tmp = image.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());
			//g.drawImage(tmp, x, y, p.getColorPrimary());
			if(drawFlash)
			{
				tmp.drawFlash(x, y);
				drawFlash = false;
			}
			else
			{
				tmp.draw(x, y, getColorPrimary());
			}
		}
	}

	public final void renderSecondary(Graphics g)
	{
		if (imageSecondary != null) 
		{
			Image tmp = imageSecondary.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());
			if(drawFlash)
			{
				tmp.drawFlash(x, y);
				drawFlash = false;
			}
			else
			{
				tmp.draw(x, y, getColorSecondary());
			}
		}
	}

	public final void renderAccent(Graphics g)
	{
		if (imageAccent != null) 
		{
			Image tmp = imageAccent.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());

			if(drawFlash)
			{
				tmp.drawFlash(x, y);
				drawFlash = false;
			}
			else
			{
				tmp.draw(x, y, getColorAccent());
			}
		}
	}

	public final void renderMove(Graphics g)
	{
		if (imageMove != null) 
		{
			Image tmp = imageMove.getScaledCopy(getScale());
			tmp.setCenterOfRotation(tmp.getWidth() / 2 * getScale(), tmp.getHeight() / 2 * getScale());
			tmp.setRotation(getTheta());
			g.drawImage(tmp, x, y);
		}
	}


	public final void renderShield(Graphics g)
	{
		if (getCurShield() > 0 || isInvulnerable() || getConditions().containsType(Fortified.class)) 
		{
			// Shield Position
			float width = (float) (w * 1.85);
			float height = (float) (h * 1.85);
			float x = getCenterX() - width / 2;
			float y = getCenterY() - height / 2;

			// Shield Colors and Transparency
			int alpha = (int) (50 * (getCurShield() / getMaxShield()));

			// Modify values for aegis  shield
			if(isInvulnerable() || getConditions().containsType(Fortified.class))
			{
				alpha = Utility.random(115, 130);
			}

			// Set normal colors
			Color fill = getColorPrimary();
			fill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), alpha);

			// Make the shield flicker when invulnerability blocks damage
			if(drawFlashInvuln)
			{
				fill = new Color(255,255,255, alpha);
				drawFlashInvuln = false;
			}



			g.setColor(fill);
			g.fillOval(x+1, y+1, width-2, height-2);
			
//			g.setColor(border);
			//g.drawOval(x, y, width, height);


			g.resetLineWidth();

		}
	}

	public void render(Graphics g) 
	{
		super.render(g);

		setImage();
		renderUnit(g);
		renderShield(g);

		healthbar.render(g);

		if(isSelected())
		{
			g.setColor(Color.white);
			g.drawRect(x-w/2, y-h/2-4, w*2, h*2);
		}

		if (getTeam() == Values.TEAM_ONE_ID && Settings.showPlayerOneInfo) 
		{
			draw(g);
		}

		if (getTeam() == Values.TEAM_TWO_ID && Settings.showPlayerTwoInfo) 
		{
			draw(g);
		}



	}

	
	public void onDeath()
	{
		// This method can be overriden by players who wish to have an on death effect
	}

	public final void die() 
	{
		dropResourcesOnDeath();
		onDeath();
		
		super.die();

		if(this instanceof BaseShip)
		{
			AnimationManager.add(new Boom(getCenterX(), getCenterY(), 300 * getScale()));
			getPlayer().loseGame();
		}
		else
		{
			AnimationManager.add(new Boom(getCenterX(), getCenterY(), getFrame().getImageSize() * getScale()));
		}

		float unitScale = this.getFrame().getMineralCost() / 24;		// assumes 8 to 20 costs
		Sounds.boom.play(getPosition(), 1.2f - unitScale, unitScale + .2f);

	}

	public final void dropResourcesOnDeath()
	{
		if(isAlive())
		{
			scrap /= 2;
			minerals /= 2;
			
			dump();

			for(int i = 0; i < getFrame().getScrapAmountOnDeath(); i++)
			{
				ResourceManager.spawnScrapNear(x,  y,  xSpeed, ySpeed, getSize());
			}
		}

	}

	public final static Unit getNearestUnit(Point point, Class<? extends Unit> clazz)
	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  Game.getUnits();

		for(Unit u : units)
		{
			if(clazz.isInstance(u) && u.getDistance(point) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = u.getDistance(point);
			}
		}

		return nearestUnit;
	}
	
	public Missile getNearestEnemyMissile()
	{
		ArrayList<Missile> missiles = Game.getMissiles();
		float nearestMissileDistance = Float.MAX_VALUE;
		Missile nearestMissile = null;
		
		for(Missile m : missiles)
		{
			// Eventually shadowflight will be cloaked.  This is a temporary solution.
			if(m instanceof MissileShadowflight)
			{
				continue;
			}
			
			if(getDistance(m) < nearestMissileDistance && m.getOwner().getPlayer() == getOpponent())
			{
				nearestMissileDistance = getDistance(m);
				nearestMissile = m;
			}
		}
		
		return nearestMissile;
	}




}
