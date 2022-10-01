package player;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import engine.Settings;
import engine.Values;
import engine.states.Game;
import objects.entity.missile.Missile;
import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import ui.display.Images;
import ui.display.message.PlayerMessage;

public abstract class Player 
{

	private final static int MAX_FLEET_SIZE = 50;
	public final static float MIN_COLOR_DIFF = .18f;		// Swap primary / secondary if colors are too similar
	
	public final static int EXTREME_LATENCY = 800;
	public final static int HIGH_LATENCY = 600;
	public final static int MEDIUM_LATENCY = 400;
	
	/******* Data *******/

	protected int timer;
	
	private Game g;
	
	private float difficultyRating = 1.0f;
	private float minerals;
	private float mineralsMined;
	private float mineralsLost;
	
	private String name;
	private ArrayList<PlayerMessage> messages;
	private Color colorPrimary;
	private Color colorSecondary;
	private Color colorAccent;
	private Boon boonOne;
	private Boon boonTwo;
	private Image teamImage;
	
	private int team;
	private boolean isDefeated;		
	private float damageDealt;
	private float damageTaken;
	private float repairRecieved;
	private float shieldRecieved;
	private int dodgeCount;
	private int dodgeAttempts;
	private ArrayList<Integer> latencies;
	
	public void opening()
	{
		
	}

	/******* Constructor *******/

	public Player(int team, Game g) 
	{
		this.team = team;
		this.g = g;
		
		setBoonOne(Boon.MINER);
		setBoonTwo(Boon.SNIPER);

		teamImage = Images.defaultLogo;
	
		// Default colors
		if(team == 0)
		{
			colorPrimary = new Color(6, 180, 224);
			colorSecondary = Color.white;
			colorAccent = Color.white;
	
		}
		else if(team == 1)
		{
			colorPrimary = new Color(235, 45, 50);
			colorSecondary = Color.white;
			colorAccent = Color.white;
		}
	}
	
	public void initialize()
	{
		latencies = new ArrayList<Integer>();
		messages = new ArrayList<PlayerMessage>();
		setStartingValues();
	}
	
	protected final void setTeamImage(String filename)
	{
		try 
		{
			teamImage = new Image(filename);
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}

	public final void setStartingValues()
	{
		isDefeated = false;

		timer = 0;
		damageDealt = 0;
		damageTaken = 0;
		dodgeCount = 0;
		dodgeAttempts = 0;
		repairRecieved = 0;
		shieldRecieved = 0;
		minerals = 0;
		mineralsMined = 0;
		mineralsLost = 0;
		addMinerals(Values.STARTING_MINERALS);
		isDefeated = false;
		timer = 0;
		flipColorsIfNeeded();
	}

	public abstract void draw(Graphics g);

	public abstract void strategy() throws SlickException;

	/******* Accessor Methods *******/

	public final String getName() 							{	return name;								}
	public final boolean isDefeated()						{	return isDefeated;							}
	public final ArrayList<PlayerMessage> getMessages()		{	return messages;							}
	public final PlayerMessage getMessage(int index)		{	return messages.get(index);					}
	public final float getDamageDealt()						{	return damageDealt;							}
	public final float getDamageTaken()						{	return damageTaken;							}
	public final int getDodgeCount()						{	return dodgeCount;							}
	public final int getDodgeAttempts()						{	return dodgeAttempts;						}
	public final float getRepairRecieved()					{	return repairRecieved;						}
	public final float getShieldRecieved()					{	return shieldRecieved;						}
	
	public final float getMinerals() 						{	return minerals;							}
	public final float getMineralsMined() 					{	return mineralsMined;						}
	public final float getMineralsLost() 					{	return mineralsLost;						}

	public final int getMaxFleetSize()						{ 	return MAX_FLEET_SIZE;						}
	public final int getPercentFleetSize()					{	return countMyUnits() / getMaxFleetSize();	} 
	
	public final Color getColorPrimary()					{	return colorPrimary;						}
	public final Color getColorSecondary()					{	return colorSecondary;						}
	public final Color getColorAccent()						{	return colorAccent;							}
		
	public final Player getOpponent() 						{	return Game.getOpponent(this);				}
	public final float getDifficultyRating()				{	return difficultyRating;					}
	public final int getTeam() 								{	return team;								}
	public final Image getTeamImage()						{ 	return teamImage;							}
	public final boolean isLeftPlayer()						{	return Game.getPlayerOne() == this;			}
	public final boolean isRightPlayer()					{	return Game.getPlayerTwo() == this;			}
		
	public final Boon getBoonOne()							{	return boonOne;								}
	public final Boon getBoonTwo()							{	return boonTwo;								}
	public final boolean hasBoonOne()						{	return boonOne != null;						}
	public final boolean hasBoonTwo()						{	return boonTwo != null;						}
	public final boolean hasBoon(Boon b)					{	return boonOne == b || boonTwo == b;		}
	
	// Counting

	public final int countAllUnits()									{	return Game.getUnits().size();				}
	public final int countUnit(Player p, Class<? extends Unit> clazz) 	{	return getUnits(p, clazz).size();			}
	public final int countMyUnits()										{	return getMyUnits().size();					}
	public final int countMyUnits(Class<? extends Unit> clazz) 			{	return countUnit(this, clazz);				}
	public final int countEnemyUnits() 									{	return getEnemyUnits().size();				}
	public final int countEnemyUnits(Class<? extends Unit> clazz) 		{	return countUnit(getOpponent(), clazz);		}

	public String getDifficultyRatingModifierString()
	{
		String message = "";
		int bonus = (Math.round((getDifficultyRating() - 1) * 100));
		
		if(bonus > 0)
		{
			message += "+";
		}
				
		return message + bonus + "%";
	}

	public int getWeightedAverageLatency() 				
	{
		return getWeightedAverageLatency(Values.LATENCY_SAMPLE_FREQUENCY);
	}
	
	public int getWeightedAverageLatency(int lastNumFrames) 				
	{	
		int total = 0;
		int start = Math.max(0,  latencies.size() - lastNumFrames);	
		int count = latencies.size() - start;
		int weightedCount = 0;
		float weight = 0;
		
		for(int i = start; i < latencies.size(); i++)
		{
			weight = (float) i / (float) count;
			weightedCount += weight;
			total += latencies.get(i) * weight;
		}
		
		if(weightedCount == 0)
		{
			return 0;						
		}
		else
		{
			return total / weightedCount;
		}
				
		
	}
	
	// Getting Units

	public final ArrayList<Unit> getAllUnits()							{	return Game.getUnits();	}
	public final ArrayList<Missile> getAllMissiles()					{	return Game.getMissiles();	}
	public final ArrayList<Node> getAllNodes()							{	return NodeManager.getNodes();	}
	public final ArrayList<Resource> getAllResources()					{	return ResourceManager.getResources();	}

	public final ArrayList<Unit> getUnits(Player p, Class<? extends Unit> clazz) 
	{
		ArrayList<Unit> playerUnits = p.getMyUnits();
		ArrayList<Unit> units = new ArrayList<Unit>();
				
		for(Unit u : playerUnits)
		{
			if(clazz.isInstance(u))
			{
				units.add(u);
			}
		}

		return units;
	}
	
	public final ArrayList<Missile> getMissiles(Player p) 
	{
		ArrayList<Missile> allMissiles = getAllMissiles();
		ArrayList<Missile> missiles = new ArrayList<Missile>();
				
		for(Missile m : allMissiles)
		{
			if(m.getOwner().getPlayer() == p)
			{
				missiles.add(m);
			}
		}

		return missiles;
	}

	public final ArrayList<Unit> getMyUnits(Class<? extends Unit> clazz) 
	{	
		return getUnits(this, clazz);
	}

	public final ArrayList<Unit> getMyUnits() 
	{	
		if(team == Values.TEAM_ONE_ID)	return Game.getTeamOneUnits();
		else							return Game.getTeamTwoUnits();
	}
	
	public final ArrayList<Missile> getMyMissiles()
	{
		return getMissiles(this);
	}
	
	public final ArrayList<Missile> getEnemyMissiles()
	{
		return getMissiles(getOpponent());
	}

	//	public ArrayList<Unit> getEnemyUnits(Class<? extends Unit> clazz) 
	//	{	
	//		return getUnits(getOpponent(), clazz);
	//	}

	public final ArrayList<Unit> getEnemyUnits() 
	{	
		return getOpponent().getMyUnits();
	}

	public final int getEnemyTeam() 
	{
		if(getTeam() == Values.TEAM_ONE_ID)
		{
			return Values.TEAM_TWO_ID;
		}
		else if(getTeam() == Values.TEAM_TWO_ID)
		{
			return Values.TEAM_ONE_ID;
		}
		else
		{
			return -1;
		}
	}
	
	public final int getFleetValue()			
	{
		int total = 0;
		ArrayList<Unit> units = getMyUnits();
		
		for(Unit u : units)
		{
			if(!(u instanceof BaseShip))
			{
				total += u.getValue();
			}
		}
		
		return total;
	}
		
	
	public final int getFleetValue(Class<? extends Unit> clazz)			
	{
		int total = 0;
		ArrayList<Unit> units = getMyUnits();
		
		for(Unit u : units)
		{
			if(clazz.isInstance(u))
			{
				total += u.getValue();
			}
		}
		
		return total;
	}
	
	public final float getFleetValuePercentage(Class<? extends Unit> clazz)
	{
		if(getFleetValue() == 0)
		{
			return 0;
		}
		else
		{
			return (float) getFleetValue(clazz) / (float) getFleetValue();
		}
	}

	/************** MUTATORS *************/
	
	protected final void setName(String n) 						{	name = n;										}
	public final void loseGame()								{	isDefeated = true;								}	



	public final void addMessage(PlayerMessage pm)				{	if(pm != null ) messages.add(pm);				}
	public final void addMessage(String s)						{	messages.add(new PlayerMessage(s));				}
	public final void addMessage(String s, Color c)				{	messages.add(new PlayerMessage(s, c));			}
	
	public final void setColorPrimary(int r, int g, int b)		{	setColorPrimary(new Color(r, g, b));			}
	public final void setColorSecondary(int r, int g, int b)	{	setColorSecondary(new Color(r, g, b));			}
	public final void setColorAccent(int r, int g, int b)		{	setColorAccent(new Color(r, g, b));				}
	public final void setColorPrimary(Color c)					{	colorPrimary = c;								}
	public final void setColorSecondary(Color c)				{	colorSecondary = c;								}
	public final void setColorAccent(Color c)					{	colorAccent = c;								}
	
	
	public final void setBoonOne(Boon b)						{	if(Game.getTime() < 2) boonOne = b;				}
	public final void setBoonTwo(Boon b)						{	if(Game.getTime() < 2) boonTwo = b;				}
	
	public final void addDamageDealt(float amount)				{ 	 damageDealt += amount;					}
	public final void addDamageTaken(float amount)				{ 	 damageTaken += amount;					}
	public final void addRepairRecieved(float amount)			{ 	 repairRecieved += amount;					}
	public final void addShieldRecieved(float amount)			{ 	 shieldRecieved += amount;					}
	public final void addDodge()								{ 	 dodgeCount++;							}
	public final void addDodgeAttempt()							{ 	 dodgeAttempts++;						}

	public void addLatency(int latency)							{ 	latencies.add(latency);			}
	
	public final void update()
	{
		if(Game.getTime() == 0)
		{
			opening();
		}		
		
//		if(this  == Game.getPlayerOne())
//		{
//			System.out.println("Weighted Latency " + getWeightedAverageLatency());
//
//		}
		
		applyLatencyPenalty();

		
		timer++;
		try
		{
			strategy();
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
		
		// Update units
		ArrayList<Unit> units = getMyUnits();
		for (Unit u : units) 
		{
			if (u.isAlive()) 
			{
				u.update();
			}
		}
		
		// Update missiles

//		System.out.println(Game.getTime() + " " + getName() + " " + getAverageLatency(30));
		
	}
	
	public final void updateMissiles()
	{
		ArrayList<Missile> missiles = getMyMissiles();
		for (Missile m : missiles) 
		{
			if (m.isAlive()) 
			{
				m.update();
			}
		}

	}
	
	public final void removeAllUnits()
	{
		ArrayList<Unit> units = getMyUnits();
		for (Unit u : units) 
		{
			u.die();
		}
	}
	/** Upgrades **/

	public final void setDifficultyRating(int percentage)
	{
		float difficultyRating = ((float) percentage) * .01f;
		if(difficultyRating >= 0.0f)
		{
			this.difficultyRating = difficultyRating;
		}
	}

	public final BaseShip getMyBase() 
	{
		return Game.getBaseShip(this);
	}

	public final BaseShip getEnemyBase() 
	{
		return Game.getBaseShip(getOpponent());
	}	

	public final void addMinerals(float amount) 
	{		
		float actualAmount = amount;
	
//		if(hasBoon(Boon.MINER))
//		{
//			actualAmount += amount * Boon.MINER_RESOURCE_BONUS_MULTIPLIER;
//		}
		
		minerals += actualAmount;
		mineralsMined += actualAmount;
		
	}

	public final void subtractMinerals(float amount) 
	{
		minerals -= amount;
		mineralsLost += amount;
	}	
	
	public final void render(Graphics g)
	{
		messages.clear();
		g.setColor(Color.red);
		
		if (Game.getTime() > 0 &&
				Settings.showPlayerOneInfo && this == Game.getPlayerOne() ||
			Settings.showPlayerTwoInfo && this == Game.getPlayerTwo()) 
		{
			draw(g);
			
			for (Unit u : getMyUnits()) 
			{
				u.draw(g);
			}
			
	
		}
	
	}
	
	// Building Units


	public final void buildUnit(Unit u) 
	{	
		u.design();

		if(minerals >= u.getValue() && countMyUnits() < MAX_FLEET_SIZE)
		{
			g.addUnit(u);
			minerals -= u.getValue();
		}
	}
	
	// Colors

	public final void flipColorsIfNeeded()
	{		
		int r1 = getColorPrimary().getRed();
		int g1 = getColorPrimary().getGreen();
		int b1 = getColorPrimary().getBlue();

		int r2 = getOpponent().getColorPrimary().getRed();
		int g2 = getOpponent().getColorPrimary().getGreen();
		int b2 = getOpponent().getColorPrimary().getBlue();

		float minDiff = MIN_COLOR_DIFF;

		float rDiff = (float) Math.abs(r1 - r2) / 255f;
		float gDiff = (float) Math.abs(g1 - g2) / 255f;
		float bDiff = (float) Math.abs(b1 - b2) / 255f;

		float aDiff = (rDiff + gDiff + bDiff)/3;

//		System.out.println("Avg Difference: " + (int)(aDiff*100) + "%");
//		System.out.println(" --> " + (int)(rDiff*100) + ", " + (int)(gDiff*100) + ", " + (int)(bDiff*100));
//		System.out.println("Swap if Under:  " + (int)(minDiff*100) + "%");


		if(aDiff < minDiff)
		{
		//	System.out.println("Colors too similar.  Changing to alternate.");

			flipColors();
		}
	}

	public final void flipColors()
	{
		Color tmp = getColorPrimary();
		setColorPrimary(getColorSecondary());
		setColorSecondary(tmp);
	}
	
	public final void applyLatencyPenalty()
	{
		if(Settings.penalizeLatency)
		{
			if(Game.getTime() < Values.LATENCY_GRACE_PERIOD)
			{
				return;
			}
			
			if(getWeightedAverageLatency() > Player.EXTREME_LATENCY)
			{
				subtractMinerals(1.0f/60.0f);
			}
		}
	}


	//	/** Utility Methods **/
	//	private Class<? extends Unit> baseUnitType(Class<? extends Unit> clazz) {
	//		if (Miner.class.isAssignableFrom(clazz)) {
	//			return Miner.class;
	//		} else if (Raider.class.isAssignableFrom(clazz)) {
	//			return Raider.class;
	//		} else if (Assault.class.isAssignableFrom(clazz)) {
	//			return Assault.class;
	//		} else if (Specialist.class.isAssignableFrom(clazz)) {
	//			return Specialist.class;
	//		} else if (Support.class.isAssignableFrom(clazz)) {
	//			return Support.class;
	//		}
	//		return null;
	//	}

	// Getters for My Units

	//	public int countMyUnitsInRadius(float x, float y, float radius) 
	//	{
	//		if (alliedUnitList == null || alliedUnitList.isEmpty())		return 0;		
	//		return getMyUnitsInRadius(x, y, radius, Unit.class).size();
	//	}
	//
	//	public ArrayList<Unit> getMyUnitsInRadius(float x, float y, float radius) {
	//		return getMyUnitsInRadius(x, y, radius, Unit.class);
	//	}
	//
	//	public ArrayList<Unit> getMyUnitsInRadius(float x, float y, float radius, Class<? extends Unit> clazz) 
	//	{
	//		if (alliedUnitList == null || alliedUnitList.isEmpty())		return null;		
	//
	//		ArrayList<Unit> radiusAllies = new ArrayList<Unit>();
	//
	//		for (Unit e : alliedUnitList) 
	//		{			
	//			if (clazz.isInstance(e) && Utility.distance(x, y, e.getCenterX(), e.getCenterY()) <= radius) {
	//				radiusAllies.add(e);
	//			}
	//		}
	//		return radiusAllies;
	//	}
	//
	//	public ArrayList<Unit> getMyUnitsInArea(Rectangle r) {
	//		return getMyUnitsInArea(r, Unit.class);
	//	}
	//
	//	public ArrayList<Unit> getMyUnitsInArea(Rectangle r, Class<? extends Unit> clazz) 
	//	{
	//		if (alliedUnitList == null || alliedUnitList.isEmpty())		return null;		
	//
	//		ArrayList<Unit> rectAllies = new ArrayList<Unit>();
	//
	//		for (Unit e : alliedUnitList) 
	//		{			
	//			if (clazz.isInstance(e) &&
	//					e.getCenterX() >= r.getX() && e.getCenterX() <= r.getX() + r.getWidth() &&
	//					e.getCenterY() >= r.getY() && e.getCenterY() <= r.getY() + r.getHeight())
	//			{
	//				rectAllies.add(e);
	//			}
	//		}
	//		return rectAllies;
	//	}
	//
	//
	//	// Getters for Enemy Units
	//
	//	public int countEnemyUnitsInRadius(float x, float y, float radius) 
	//	{
	//		if (enemyUnitList == null || enemyUnitList.isEmpty())		return 0;		
	//		return getEnemyUnitsInRadius(x, y, radius, Unit.class).size();
	//	}
	//
	//	public ArrayList<Unit> getEnemyUnitsInRadius(float x, float y, float radius) {
	//		return getEnemyUnitsInRadius(x, y, radius, Unit.class);
	//	}
	//
	//	public ArrayList<Unit> getEnemyUnitsInRadius(float x, float y, float radius, Class<? extends Unit> clazz) 
	//	{
	//		if (enemyUnitList == null || enemyUnitList.isEmpty())		return null;		
	//
	//		ArrayList<Unit> radiusEnemies = new ArrayList<Unit>();
	//
	//		for (Unit e : enemyUnitList) 
	//		{			
	//			if (clazz.isInstance(e) && Utility.distance(x, y, e.getCenterX(), e.getCenterY()) <= radius) {
	//				radiusEnemies.add(e);
	//			}
	//		}
	//		return radiusEnemies;
	//	}
	//
	//	public ArrayList<Unit> getEnemyUnitsInArea(Rectangle r) {
	//		return getEnemyUnitsInArea(r, Unit.class);
	//	}
	//
	//	public ArrayList<Unit> getEnemyUnitsInArea(Rectangle r, Class<? extends Unit> clazz) 
	//	{
	//		if (enemyUnitList == null || enemyUnitList.isEmpty())		return null;		
	//
	//		ArrayList<Unit> rectEnemies = new ArrayList<Unit>();
	//
	//		for (Unit e : enemyUnitList) 
	//		{			
	//			if (clazz.isInstance(e) &&
	//					e.getCenterX() >= r.getX() && e.getCenterX() <= r.getX() + r.getWidth() &&
	//					e.getCenterY() >= r.getY() && e.getCenterY() <= r.getY() + r.getHeight())
	//			{
	//				rectEnemies.add(e);
	//			}
	//		}
	//		return rectEnemies;
	//	}


}
