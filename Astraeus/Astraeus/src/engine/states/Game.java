package engine.states;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import animations.AnimationManager;
import engine.Settings;
import engine.Values;
import objects.ambient.AmbientManager;
import objects.entity.Entity;
import objects.entity.missile.Missile;
import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import player.Player;
import territory.TerritoryManager;
import ui.display.Camera;
import ui.display.DisplayManager;
import ui.input.InputManager;
import ui.sound.AudioManager;

public class Game extends BasicGameState 
{
	private int id;
	
	private static int gameNumber;
	private static boolean gameOver;
	private static boolean paused;
	private static int timer;
	private static int gameOverTimer;
	
	private static Player playerOne;
	private static Player playerTwo;
	private static BaseShip alpha;
	private static BaseShip beta;
	private static ArrayList<Unit> units;
	private static ArrayList<Unit> teamOneUnits;
	private static ArrayList<Unit> teamTwoUnits;
	private static ArrayList<Missile> missiles;
	
	private static float damageDealt;
	private static float damageTaken;
	private static int dodgeCount;
	private static float repairRecieved;
	private static float shieldRecieved;
	
	/****************** Constructor and Setup ******************/

	public Game(int id) 
	{
		this.id = id;
	}
	
	private void setPlayers() 
	{
		playerOne = Menu.getPlayers().get(0);
		playerTwo = Menu.getPlayers().get(1);
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) 
	{		
		// Overall audio volume modifiers
		gc.setSoundVolume(Settings.soundVolume / 2);
		gc.setMusicVolume(Settings.musicVolume / 5);
		gc.setShowFPS(false);
	}
	
	/****************** Accessors  ******************/
	
	public int getID()									{	return id;							}
	public static boolean isGamePaused() 				{	return paused;						}
	public static int getTime()							{	return timer;						}
	public static boolean isGameOver()					{	return gameOver;					}
	public static ArrayList<Unit> getUnits()			{	return units;						}
	public static ArrayList<Unit> getTeamOneUnits()		{	return teamOneUnits;				}
	public static ArrayList<Unit> getTeamTwoUnits()		{	return teamTwoUnits;				}
	public static ArrayList<Missile> getMissiles()		{	return missiles;					}
	public final ArrayList<Node> getAllNodes()			{	return NodeManager.getNodes();		}
	public final ArrayList<Resource> getAllResources()	{	return ResourceManager.getResources();	}
	
	public static float getTotalDamageDealt()			{	return damageDealt;						}
	public static float getTotalDamageTaken()			{	return damageTaken;						}
	public static int getTotalDodgeCount()				{	return dodgeCount;						}
	public static float getTotalRepairRecieved()		{	return repairRecieved;					}
	public static float getTotalShieldRecieved()		{	return shieldRecieved;					}
	
	public static int getMapWidth()						{	return Values.PLAYFIELD_WIDTH;			}
	public static int getMapHeight()					{	return Values.PLAYFIELD_HEIGHT;			}
	public static int getMapLeftEdge()					{	return -Values.PLAYFIELD_WIDTH / 2;		}
	public static int getMapRightEdge()					{	return Values.PLAYFIELD_WIDTH / 2;		}
	public static int getMapTopEdge()					{	return -Values.PLAYFIELD_HEIGHT / 2;	}
	public static int getMapBottomEdge()				{	return Values.PLAYFIELD_HEIGHT / 2;		}
	
	public static BaseShip getBaseShip(Player p)		{	return p == playerOne ? alpha : p == playerTwo ? beta : null; }
	public static Player getOpponent(Entity e) 			{ 	return getOpponent(e.getTeam()); 	}
	public static Player getOpponent(Player p) 			{ 	return getOpponent(p.getTeam()); 	}
	public static Player getOpponent(int team) 			{ 	return team == Values.TEAM_ONE_ID ? playerTwo : team == Values.TEAM_TWO_ID ? playerOne : null; }	
	
	public static Player getPlayerOne()					{	return playerOne;		}
	public static Player getPlayerTwo()					{	return playerTwo;		}

	public static int getGameNumber()					{	return gameNumber;		}

	/****************** Mutators ******************/
	
	public static void addDamageDealt(float amount)				{ 	 damageDealt += amount;			}
	public static void addDamageTaken(float amount)				{ 	 damageTaken += amount;			}
	
	public static void addTotalRepairRecieved(float amount)		{ 	 repairRecieved += amount;		}
	public static void addTotalShieldRecieved(float amount)		{ 	 shieldRecieved += amount;		}
	
	public static void addDodge()								{ 	 dodgeCount++;					}	
	
	public static void togglePause()							{	paused = !paused; 				}
	public static void addMissile(Missile m)					{	missiles.add(m);				}

	public void addUnit(Unit u) 					
	{		
		units.add(u);	
		if(u.getPlayer().getTeam() == Values.TEAM_ONE_ID)
		{
			teamOneUnits.add(u);
		}
		else
		{
			teamTwoUnits.add(u);
		}
	}
	
	public static ArrayList<Entity> getEntities()		
	{
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		entities.addAll(Game.getUnits());
		entities.addAll(Game.getMissiles());
		entities.addAll(NodeManager.getNodes());
		
		return entities;						
	}

	
	
	public void enter(GameContainer gc, StateBasedGame sbg) 
	{
		Settings.loadPresets();
		
		gameNumber++;
		damageDealt = 0;
		damageTaken = 0;
		dodgeCount = 0;
		repairRecieved = 0;
		shieldRecieved = 0;
		gameOver = false;
		gameOverTimer = 0;
		timer = 0;
		
		// Initialize Arrays
		units = new ArrayList<Unit>();
		teamOneUnits = new ArrayList<Unit>();
		teamTwoUnits = new ArrayList<Unit>();
		missiles = new ArrayList<Missile>();

		InputManager.setup(gc, sbg);
		Camera.setup();

		AmbientManager.setup(gc.getGraphics());
		AnimationManager.setup(gc.getGraphics());
		
		setPlayers();
		TerritoryManager.setup();	
		DisplayManager.setup(gc.getGraphics());
		
		playerOne.initialize();
		playerTwo.initialize();

		alpha = new BaseShip(playerOne);
		beta = new BaseShip(playerTwo);

		units.add(alpha);
		teamOneUnits.add(alpha);
		units.add(beta);
		teamTwoUnits.add(beta);
	}

	public void leave(GameContainer gc, StateBasedGame sbg) 
	{
		units.clear();
		teamOneUnits.clear();
		teamTwoUnits.clear();
		missiles.clear();
		
		DisplayManager.leave();
		InputManager.leave();
		TerritoryManager.leave();
		AnimationManager.leave();
		AmbientManager.leave();
		AudioManager.leave();
		
		alpha = null;
		beta = null;
		playerOne = null;
		playerTwo = null;
		paused = false;

		gameOver = false;
		gameOverTimer = 0;
		timer = 0;
		Settings.gameSpeed = 2;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
	{
		DisplayManager.render();
	}

	public void keyPressed(int key, char c) 
	{
		InputManager.keyPressed(key, c);
	}
	
	public void keyReleased(int key, char c) 
	{
		InputManager.keyReleased(key, c);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
	{
		if (!paused) 
		{
			for (int j = 0; j < Settings.gameSpeed; j++) 
			{
				// PLAYER ONE TURN
				long startTime;
				long duration;

				startTime = System.nanoTime();
				playerOne.update();
				duration = (System.nanoTime() - startTime) / 1000;
				
				playerOne.updateMissiles();	
				
				if (timer > 1)
				{
					playerOne.addLatency((int) duration);
				}
				
				// PLAYER TWO TURN
				startTime = System.nanoTime();
				playerTwo.update();
				duration = (System.nanoTime() - startTime) / 1000;
				
				playerTwo.updateMissiles();	

				if (timer > 1)
				{
					playerTwo.addLatency((int) duration);
				}
				
				timer++;
				cleanUp();
		
				TerritoryManager.update();
				AmbientManager.update();
				AnimationManager.update();

				AudioManager.update();
				DisplayManager.updateCanPause();

			}
		
		}
	
		
		DisplayManager.update();

		Camera.update(gc);

	}

	public void cleanUp() 
	{
		for (int i = 0; i < units.size(); i++) 
		{
			Unit a = units.get(i);
			
			if (a.isDead()) 
			{
				units.remove(i);
				
				if(a.getPlayer().getTeam() == Values.TEAM_ONE_ID)
				{
					teamOneUnits.remove(a);
				}
				else
				{
					teamTwoUnits.remove(a);
				}
//				a.die();
				i--;
			}

		}
		
		for (int i = 0; i < missiles.size(); i++) 
		{
			Missile m = missiles.get(i);
			
			if (m.isDead()) 
			{
				missiles.remove(i);
				//m.die();
				i--;
			}

		}
		
	
		TerritoryManager.cleanUp();
		
		if (playerOne.isDefeated() && !gameOver) 
		{
			gameOver = true;
			gameOverTimer = 0;
			Settings.gameSpeed = 1;
			playerOne.removeAllUnits();
		} 
		else if (playerTwo.isDefeated() && !gameOver) 
		{
			gameOver = true;
			gameOverTimer = 0;
			Settings.gameSpeed = 1;
			playerTwo.removeAllUnits();
		}

		if (gameOverTimer == Values.FRAMES_PER_SECOND) 
		{
			paused = true;
			gameOverTimer = 0;
		} 
		else if (gameOver && !paused) 
		{
			gameOverTimer++;
		}
	}

	public void mouseWheelMoved(int change)
	{
		Camera.zoom(change);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		InputManager.mousePressed(button, x, y);
 	}
		
	public void mouseMoved(int oldX, int oldY, int newX, int newY) 
	{
		Camera.panCamera(newX - oldX, newY - oldY);
	}

	public void mouseDragged(int oldX, int oldY, int newX, int newY) 
	{
		Camera.panCamera(newX - oldX, newY - oldY);
	}
	



}
